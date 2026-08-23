package net.apocalypse.mineblackflow.network;

import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.capability.data.PlayerData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataMessage {
    private final PlayerData data;

    public PlayerData get(){return data;}

    public PlayerDataMessage(FriendlyByteBuf buffer) {
        this.data = new PlayerData();
        this.data.deserializeNBT(buffer.readNbt());
    }

    public PlayerDataMessage(PlayerData data) {
        this.data = new PlayerData().syncFrom(data);
    }

    public static void buffer(PlayerDataMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.data.serializeNBT());
    }

    public static void handler(PlayerDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isServer()) {
                PlayerData variables = MBFCapabilities.getClientPlayerData();
                if (variables != null) {
                    PlayerData.syncFromMessage(variables, message);
                }
            }
        });
        context.setPacketHandled(true);
    }
}
