package net.apocalypse.mineblackflow.network;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.capability.data.LivingData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LivingDataMessage {
    private final LivingData data;
    private final int entityId;

    public LivingData get(){return data;}

    public LivingDataMessage(FriendlyByteBuf buffer) {
        this.data = new LivingData();
        this.data.deserializeNBT(buffer.readNbt());
        this.entityId = buffer.readInt();
    }

    public LivingDataMessage(LivingData data, int entity_id) {
        this.data = new LivingData().syncFrom(data);
        this.entityId = entity_id;
    }

    public static void buffer(LivingDataMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.data.serializeNBT());
        buffer.writeInt(message.entityId);
    }

    public static void handler(LivingDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isServer()) {
                if (Minecraft.getInstance().level != null) {
                    Entity clientEntity = Minecraft.getInstance().level.getEntity(message.entityId);
                    if (clientEntity instanceof LivingEntity living) {
                        LivingData livingData = MBFCapabilities.getLivingData(living);
                        LivingData.syncFromMessage(livingData, message);
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}
