package net.apocalypse.mineblackflow.network;

import net.apocalypse.mineblackflow.capability.data.DimensionData;
import net.apocalypse.mineblackflow.capability.data.WorldData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SavedDataMessage {
    private final boolean global; //as type == 0
    private SavedData data;

    public SavedDataMessage(FriendlyByteBuf buffer) {
        this.global = buffer.readBoolean();
        CompoundTag nbt = buffer.readNbt();
        if (nbt != null) {
            this.data = global ? new WorldData() : new DimensionData();
            if (this.data instanceof WorldData worldData)
                worldData.read(nbt);
            else if (this.data instanceof DimensionData dimensionData)
                dimensionData.read(nbt);
        }
    }

    public SavedDataMessage(boolean global, SavedData data) {
        this.global = global;
        this.data = data;
    }

    public boolean isGlobal(){return global;}
    public SavedData getData(){return data;}

    public static void buffer(SavedDataMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.isGlobal());
        if (message.data != null)
            buffer.writeNbt(message.data.save(new CompoundTag()));
    }

    public static void handler(SavedDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (!context.getDirection().getReceptionSide().isServer() && message.data != null) {
                if (message.isGlobal())
                    WorldData.syncClient(message.data);
                else
                    DimensionData.syncClient(message.data);
            }
        });
        context.setPacketHandled(true);
    }
}
