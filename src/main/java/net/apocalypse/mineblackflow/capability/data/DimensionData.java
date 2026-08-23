package net.apocalypse.mineblackflow.capability.data;

import net.apocalypse.mineblackflow.init.MBFNetwork;
import net.apocalypse.mineblackflow.network.SavedDataMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class DimensionData extends SavedData {
    public static final String ID = "mbf_dimension_data";

    public static DimensionData load(CompoundTag tag) {
        DimensionData data = new DimensionData();
        data.read(tag);
        return data;
    }

    public void read(CompoundTag nbt) {
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
        return nbt;
    }

    public void syncData(LevelAccessor world) {
        this.setDirty();
        if (world instanceof Level level && !level.isClientSide())
            MBFNetwork.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(level::dimension),
                    new SavedDataMessage(false, this));
    }

    public static void syncClient(SavedData data){
        if (data instanceof DimensionData d) CLIENT_DATA = d;
    }

    private static DimensionData CLIENT_DATA = new DimensionData();

    public static DimensionData get(LevelAccessor world) {
        if (world instanceof ServerLevel level) {
            return level.getDataStorage().computeIfAbsent(DimensionData::load, DimensionData::new, ID);
        } else {
            return CLIENT_DATA;
        }
    }
}
