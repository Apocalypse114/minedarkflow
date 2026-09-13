package net.apocalypse.mineblackflow.network;

import net.apocalypse.mineblackflow.compat.curios.MBFCuriosUtil;
import net.apocalypse.mineblackflow.gui.menu.AccessoryBoxMenu;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SimpleKeyMessage {
    int type, msInPress;

    public SimpleKeyMessage(int type, int msInPress) {
        this.type = type;
        this.msInPress = msInPress;
    }
    public void doOnServer(Player entity, int type, int pressedms){
        if (type == 0) doOnPress(entity);
        else doOnRelease(entity, pressedms);
    }
    public void doOnPress(Player player){
    }
    public void doOnRelease(Player player, int msInPress){
    }

    public SimpleKeyMessage(FriendlyByteBuf buffer) {
        this.type = buffer.readInt();
        this.msInPress = buffer.readInt();
    }

    public static void buffer(SimpleKeyMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.type);
        buffer.writeInt(message.msInPress);
    }

    public static void handler(SimpleKeyMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            message.doOnServer(context.getSender(), message.type, message.msInPress);
        });
        context.setPacketHandled(true);
    }

    public static class TriggerAccessoryMessage extends SimpleKeyMessage{
        public TriggerAccessoryMessage(int type, int msInPress) {
            super(type, msInPress);
        }
        public TriggerAccessoryMessage(FriendlyByteBuf buffer){
            super(buffer);
        }
        @Override
        public void doOnRelease(Player player, int dms){
            if (dms > 1000) MBFCuriosUtil.triggerAccessory(player, 1);
            else MBFCuriosUtil.triggerAccessory(player, 0);
        }
    }
}

