package net.apocalypse.mineblackflow.capability.data;

import net.apocalypse.mineblackflow.init.MBFNetwork;
import net.apocalypse.mineblackflow.network.SavedDataMessage;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;

public class WorldData extends SavedData{
    public static final String ID = "mbf_world_data";
    public BlockState global_map_bstst_example = Blocks.AIR.defaultBlockState();
    public double global_world_example = 0;
    public double player_persistent_example = 0;

    public static WorldData load(CompoundTag tag) {
        WorldData data = new WorldData();
        data.read(tag);
        return data;
    }

    public void read(CompoundTag nbt) {
        global_map_bstst_example = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), nbt.getCompound("global_map_bstst_example"));
        global_world_example = nbt.getDouble("global_world_example");
        player_persistent_example = nbt.getDouble("player_persistent_example");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.put("global_map_bstst_example", NbtUtils.writeBlockState(global_map_bstst_example));
        nbt.putDouble("global_world_example", global_world_example);
        nbt.putDouble("player_persistent_example", player_persistent_example);
        return nbt;
    }

    public void syncData(LevelAccessor world) {
        this.setDirty();
        if (world instanceof Level && !world.isClientSide())
            MBFNetwork.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SavedDataMessage(true, this));
    }
    public static void syncClient(SavedData data){
        if (data instanceof WorldData d) CLIENT_DATA = d;
    }

    private static WorldData CLIENT_DATA = new WorldData();

    public static WorldData get(LevelAccessor world) {
        if (world instanceof ServerLevelAccessor serverLevelAcc) {
            ServerLevel level = serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD);
            if (level == null) return new WorldData();
            return level.getDataStorage().computeIfAbsent(WorldData::load, WorldData::new, ID);
        } else {
            return CLIENT_DATA;
        }
    }
}
