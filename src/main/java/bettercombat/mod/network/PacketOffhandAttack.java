package bettercombat.mod.network;

import bettercombat.mod.util.Helpers;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.util.EnumHand;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOffhandAttack implements IMessage
{
    private Integer entityId;

    public PacketOffhandAttack() {}

    public PacketOffhandAttack(int parEntityId) {
        this.entityId = parEntityId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        if( buf.readBoolean() ) {
            this.entityId = ByteBufUtils.readVarInt(buf, 4);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.entityId != null);
        if( this.entityId != null ) {
            ByteBufUtils.writeVarInt(buf, this.entityId, 4);
        }
    }

    public static class Handler
            implements IMessageHandler<PacketOffhandAttack, IMessage>
    {
        @Override
        public IMessage onMessage(final PacketOffhandAttack message, final MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private static void handle(PacketOffhandAttack message, MessageContext ctx)
        {
            EntityPlayerMP player = ctx.getServerHandler().player;
            if( message.entityId != null )
            {
                Entity theEntity = player.world.getEntityByID(message.entityId);
                if( theEntity != null )
                {
                	// ItemStack mh = player.getHeldItemMainhand();
                    // player.setHeldItem(EnumHand.MAIN_HAND, player.getHeldItemOffhand());
                    Helpers.attackTargetEntityItem(player, theEntity, true);
                    // player.swingArm(EnumHand.OFF_HAND);
                    // player.setHeldItem(EnumHand.MAIN_HAND, mh);
                }
                player.swingArm(EnumHand.OFF_HAND);
            }
            else
            {
                ItemStack mh = player.getHeldItemMainhand();
                player.setHeldItem(EnumHand.MAIN_HAND, player.getHeldItemOffhand());
                player.swingArm(EnumHand.OFF_HAND);
                player.setHeldItem(EnumHand.MAIN_HAND, mh);
            }
        }
    }
}