package net.apocalypse.mineblackflow.network;

import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.entity.npc.NPCMechanist;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class IntButtonMessage {
    private final int index;

    public IntButtonMessage(FriendlyByteBuf buffer) {
        this.index = buffer.readInt();
    }

    public IntButtonMessage(int i) {
        this.index = i;
    }

    public static void buffer(IntButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.index);
    }

    public static void handler(IntButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player serverPlayer = context.getSender();
            if (serverPlayer != null) {
                message.doOnServerPress(serverPlayer, message.index);
            }

        });
        context.setPacketHandled(true);
    }

    public void doOnServerPress(Player presser, int index){

    }

    public static class BuyMessage extends IntButtonMessage {
        public BuyMessage(FriendlyByteBuf buffer) {super(buffer);}
        public BuyMessage(int index) {super(index);}
        @Override
        public void doOnServerPress(Player presser, int i){
            NPCMechanist npc = MBFCapabilities.getData(presser).getInteractingMechanist();
            if (npc != null){npc.getShop().putStackToBuy(i);}
        }
    }
    public static class ConfirmMessage extends IntButtonMessage {
        public ConfirmMessage(FriendlyByteBuf buffer) {super(buffer);}
        public ConfirmMessage() {super(-1);}
        @Override
        public void doOnServerPress(Player presser, int i){
            NPCMechanist npc = MBFCapabilities.getData(presser).getInteractingMechanist();
            if (npc != null){npc.getShop().confirmStackToBuy(presser);}
        }
    }
    public static class CancelMessage extends IntButtonMessage {
        public CancelMessage(FriendlyByteBuf buffer) {super(buffer);}
        public CancelMessage() {super(-1);}
        @Override
        public void doOnServerPress(Player presser, int i){
            NPCMechanist npc = MBFCapabilities.getData(presser).getInteractingMechanist();
            if (npc != null){npc.getShop().clearToBuy();}
        }
    }
    public static class RefreshMessage extends IntButtonMessage {
        public RefreshMessage(FriendlyByteBuf buffer) {super(buffer);}
        public RefreshMessage() {super(-1);}
        @Override
        public void doOnServerPress(Player presser, int i){
            NPCMechanist npc = MBFCapabilities.getData(presser).getInteractingMechanist();
            if (npc != null){npc.getShop().playerRefresh(presser);}
        }
    }
    public static class BreedMessage extends IntButtonMessage {
        public BreedMessage(FriendlyByteBuf buffer) {super(buffer);}
        public BreedMessage() {super(-1);}
        @Override
        public void doOnServerPress(Player presser, int i){
            NPCMechanist npc = MBFCapabilities.getData(presser).getInteractingMechanist();
            if (npc != null){npc.getShop().breedSeeds(presser);}
        }
    }
}
