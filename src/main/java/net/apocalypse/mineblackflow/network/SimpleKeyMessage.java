package net.apocalypse.mineblackflow.network;

import net.apocalypse.mineblackflow.compat.curios.MBFCuriosUtil;
import net.apocalypse.mineblackflow.gui.menu.AccessoryBoxMenu;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.function.Supplier;

public class SimpleKeyMessage {
    int type, msInPress;

    public SimpleKeyMessage(int type, int msInPress) {
        this.type = type;
        this.msInPress = msInPress;
    }
    public void doOnServer(Player entity, int type, int pressedms){
        if (type == 0) doOnPress(entity);
        else doOnRelease(entity, msInPress);
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

    public static class OpenBoxMessage extends SimpleKeyMessage{
        public OpenBoxMessage(int type, int msInPress) {
            super(type, msInPress);
        }
        public OpenBoxMessage(FriendlyByteBuf buffer){
            super(buffer);
        }
        @Override
        public void doOnPress(Player player){
            if (MBFCuriosUtil.isAccessoryEquipped(player, MBFItems.ACCESSORY_BOX.get())){
                player.openMenu(new AccessoryBoxMenu.Provider());
            }
        }
    }
}

