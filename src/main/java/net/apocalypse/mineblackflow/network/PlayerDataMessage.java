package net.apocalypse.mineblackflow.network;

import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.capability.data.PlayerData;
import net.apocalypse.mineblackflow.entity.npc.NPCMechanist;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerDataMessage {
    private final PlayerData data;

    public PlayerData get(){return data;}

    public PlayerDataMessage(FriendlyByteBuf buffer) {
        this.data = new PlayerData();
        this.data.deserializeNBT(buffer.readNbt());
        int id = buffer.readInt();
        if (id >= 0 && Minecraft.getInstance().level != null){
            Entity entity = Minecraft.getInstance().level.getEntity(id);
            if (entity instanceof NPCMechanist npcMechanist) this.data.setInteractingMechanist(npcMechanist);
        }
    }

    public PlayerDataMessage(PlayerData data) {
        this.data = new PlayerData().syncFrom(data);
    }

    public static void buffer(PlayerDataMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.data.serializeNBT());
        Entity npc = message.data.getInteractingMechanist();
        buffer.writeInt(npc == null? -1: npc.getId());
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
