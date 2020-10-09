package bettercombat.mod.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;






public class ConfigurationHandler
{
  public static Configuration config;
  private static final int VERSION = 5;
  public static boolean requireFullEnergy = true;
  public static boolean hitSound = true;
  public static boolean critSound = true;
  public static boolean moreSprint = true;
  public static boolean widerAttack = true;
  public static boolean moreSweep = true;
  public static boolean enableOffHandAttack = true;
  public static float offHandEfficiency = 0.5F;
  public static float widerAttackWidth = 1.15F;
  
  private static String[] itemClassWhitelist = { 
		  "net.minecraft.item.ItemSword",
		  "net.minecraft.item.ItemAxe",
		  "net.minecraft.item.ItemSpade",
		  "net.minecraft.item.ItemPickaxe",
		  "net.minecraft.item.ItemHoe",
		  "slimeknights.tconstruct.library.tools.SwordCore",
		  "slimeknights.tconstruct.library.tools.AoeToolCore",
		  "com.oblivioussp.spartanweaponry.item.ItemSaber",
		  "com.oblivioussp.spartanweaponry.item.ItemRapier",
		  "com.oblivioussp.spartanweaponry.item.ItemGlaive",
		  "com.oblivioussp.spartanweaponry.item.ItemSpear",
		  "com.oblivioussp.spartanweaponry.item.ItemPike",
		  "com.oblivioussp.spartanweaponry.item.ItemHammer",
		  "com.oblivioussp.spartanweaponry.item.ItemMace",
		  "com.oblivioussp.spartanweaponry.item.ItemWarhammer",
		  "com.oblivioussp.spartanweaponry.item.ItemBattleaxe",
		  "com.oblivioussp.spartanweaponry.item.ItemCaestus",
		  "com.oblivioussp.spartanweaponry.item.ItemClub",
		  "com.oblivioussp.spartanweaponry.item.ItemGreatsword",
		  "com.oblivioussp.spartanweaponry.item.ItemKatana",
		  "com.oblivioussp.spartanweaponry.item.ItemLance",
		  "com.oblivioussp.spartanweaponry.item.ItemLongsword",
		  "com.oblivioussp.spartanweaponry.item.ItemQuarterstaff",
		  "com.oblivioussp.spartanweaponry.item.ItemHalberd",
		  };







  
  private static String[] itemInstWhitelist = new String[0];
  private static String[] entityBlacklist = { "net.minecraft.entity.passive.EntityHorse", "net.minecraft.entity.item.EntityArmorStand", "net.minecraft.entity.passive.EntityVillager", "com.torocraft.toroquest.entity.EntityToroVillager", "com.torocraft.toroquest.entity.EntityToroNpc" };




  
  private static final String[] ICW_DEF = (String[])Arrays.copyOf(itemClassWhitelist, itemClassWhitelist.length);
  private static final String[] IIW_DEF = (String[])Arrays.copyOf(itemInstWhitelist, itemInstWhitelist.length);
  private static final String[] EB_DEF = (String[])Arrays.copyOf(entityBlacklist, entityBlacklist.length);
  
  private static Class<?>[] itemClassWhiteArray;
  private static Item[] itemInstWhiteArray;
  private static Class<?>[] entityBlackArray;
  
  public static void init(File configFile) {
    if (config == null) {
      config = new Configuration(configFile, Integer.toString(4));
      loadConfiguration();
    } 
  }
  
  private static void loadConfiguration()
  {
    widerAttack = config.getBoolean("Wider Attack", "general", true, "Melee attacks hit in a wider area (easier to land hit)");
    requireFullEnergy = config.getBoolean("Attacks require full energy", "general", true, "You may only attack if your energy is full");
    moreSprint = config.getBoolean("Attack and Sprint", "general", true, "Attacking an enemy while sprinting will no longer interrupt your sprint");
    moreSweep = config.getBoolean("More swipe animation", "general", true, "Every items can spawn the swipe animation");
    hitSound = config.getBoolean("Additional hit sound", "general", true, "Add an additional sound when striking a target");
    critSound = config.getBoolean("Additional crit sound", "general", true, "Add an additional sound when a critical strike happens");
    itemClassWhitelist = config.getStringList("Item Class Whitelist", "general", ICW_DEF, "Whitelisted item classes for attacking.");
    itemInstWhitelist = config.getStringList("Item Whitelist", "general", IIW_DEF, "Whitelisted items in the format \"domain:itemname\" for attacking.");
    enableOffHandAttack = config.getBoolean("Enable Offhand Attacks", "general", true, "Enables the capability to attack with your off-hand");
    offHandEfficiency = config.getFloat("Offhand Efficiency", "general", 0.5F, 0.0F, 256.0F, "The efficiency of an attack with offhanded weapon in percent (attack damage * efficiency)");
    widerAttackWidth = config.getFloat("Wider Attack Width", "general", 1.15F, 0.0F, 64.0F, "How much bigger the hitbox will be extended for wider attacks. Vanilla is 0.5");
    entityBlacklist = config.getStringList("Entity Blacklist", "general", EB_DEF, "Blacklisted entity classes for attacking. You will not be able to attack any entity that extends this class! Please note that entities extending IEntityOwnable are by default blacklisted, when the entity is owned by the attacker.");
    
    if (config.hasChanged())
    {
      config.save();
    }
  }
  
  public static void createInstLists()
  {
    ArrayList<Class> classList = new ArrayList<Class>();
    for (String className : itemClassWhitelist)
    {
      try
      {
        classList.add(Class.forName(className));
      } catch (ClassNotFoundException classNotFoundException) {}
    } 
    itemClassWhiteArray = (Class[])classList.toArray(new Class[0]);
    
    classList.clear();
    for (String className : entityBlacklist) {
      try {
        classList.add(Class.forName(className));
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } 
    } 
    entityBlackArray = (Class[])classList.toArray(new Class[0]);
    
    List<Item> itemList = new ArrayList<Item>();
    for (String itemName : itemInstWhitelist)
    {
    	Item itm = Item.REGISTRY.getObject(new ResourceLocation(itemName));
    	//Item itm = (Item)Item.field_150901_e.func_82594_a(new ResourceLocation(itemName));
    	if (itm != null)
    	{
    	itemList.add(itm);
    	}
    } 
    itemInstWhiteArray = (Item[])itemList.toArray(new Item[0]);
  }
  
  public static boolean isItemAttackUsable(Item item) {
    if (Arrays.stream(itemInstWhiteArray).anyMatch(blItem -> (blItem == item))) {
      return true;
    }
    
    return Arrays.stream(itemClassWhiteArray).anyMatch(wlClass -> wlClass.isInstance(item));
  }

  
  public static boolean isEntityAttackable(Entity entity) { return Arrays.stream(entityBlackArray).noneMatch(eClass -> eClass.isInstance(entity)); }

  
  @SubscribeEvent
  public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
    if ("bettercombatmod".equalsIgnoreCase(event.getModID())) {
      loadConfiguration();
      createInstLists();
    } 
  }
}