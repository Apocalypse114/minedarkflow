package net.apocalypse.mineblackflow.network;

import net.apocalypse.mineblackflow.entity.npc.NPCMechanist;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ButtonMessage {
    private final int buttonID, entityId;

    public ButtonMessage(FriendlyByteBuf buffer) {
        this.buttonID = buffer.readInt();
        this.entityId = buffer.readInt();
    }

    public ButtonMessage(int buttonID, Entity boundEntity) {
        this.buttonID = buttonID;
        this.entityId = boundEntity.getId();
    }

    public static void buffer(ButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.buttonID);
        buffer.writeInt(message.entityId);
    }

    public static void handler(ButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player serverPlayer = context.getSender();
            if (serverPlayer != null) {
                Entity boundEntity = serverPlayer.level().getEntity(message.entityId);
                message.doOnServerPress(boundEntity);
            }

        });
        context.setPacketHandled(true);
    }

    public void doOnServerPress(Entity entity){

    }

    public static class MechanistShopRefreshMessage extends ButtonMessage{
        public MechanistShopRefreshMessage(FriendlyByteBuf buffer) {
            super(buffer);
        }
        public MechanistShopRefreshMessage(int buttonID, Entity boundEntity) {
            super(buttonID, boundEntity);
        }
        @Override
        public void doOnServerPress(Entity entity){
            if (entity instanceof NPCMechanist mechanist){
                mechanist.getShop().refreshGoods(mechanist.getRandom(), 0, 18);
            }
        }
    }
}
