package net.apocalypse.mineblackflow.capability.data;

import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.core.stalk.StalkCast;
import net.apocalypse.mineblackflow.core.stalk.StalkInstance;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MBFDataHandler {
    public static final String TAG_TICKING_STALK = "ticking_stalk";
    public static void saveTickingStalks(DimensionData data, CompoundTag nbt){
        ListTag listTag = new ListTag();
        data.TICKING_STALK.forEach(st ->{
            if (!st.isDone()) listTag.add(st.serializeNBT());
        });
        nbt.put(TAG_TICKING_STALK, listTag);
    }
    public static void loadTickingStalks(DimensionData data, CompoundTag nbt){
        ListTag listTag = nbt.getList(TAG_TICKING_STALK, Tag.TAG_COMPOUND);
        for (Tag tag : listTag) {
            if (tag instanceof CompoundTag compoundTag){
                data.TICKING_STALK.add(StalkInstance.fromNBT(compoundTag));
            }
        }
    }
    public static void tickStalks(DimensionData data, ServerLevel serverLevel){
        for (StalkInstance stalkInstance: new ArrayList<>(data.TICKING_STALK)) {
            stalkInstance.tick(serverLevel);
            if (stalkInstance.isDone()) data.TICKING_STALK.remove(stalkInstance);
        }
        data.setDirty();
    }

    @NotNull
    public static StalkInstance startNewStalk(ServerLevel level, ResourceLocation id, BlockPos pos){
        StalkInstance instance = new StalkInstance(id, pos);
        if (instance.validInstance()) {
            DimensionData data = MBFCapabilities.getDimensionData(level);
            data.TICKING_STALK.add(new StalkInstance(id, pos));
            level.playSound(null, pos, MBFSounds.STALK_PURCHASE.get(), SoundSource.AMBIENT, 1.8f, 1);
            data.setDirty();
        }
        return instance;
    }
}
