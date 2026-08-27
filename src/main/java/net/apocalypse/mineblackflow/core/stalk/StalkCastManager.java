package net.apocalypse.mineblackflow.core.stalk;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.datagen.StalkCastGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StalkCastManager extends SimpleJsonResourceReloadListener {
    public static final String PATH = "stalk_casts";

    private Map<ResourceLocation, StalkCast> LOADED_CAST = new ConcurrentHashMap<>();
    private List<StalkCast> CAST_LIST = new ArrayList<>();

    public @Nullable StalkCast getCast(ResourceLocation id){return LOADED_CAST.get(id);}
    public Map<ResourceLocation, StalkCast> getAll(){return LOADED_CAST;}
    public @Nullable StalkCast getRandomCast(Level pLevel){
        if (CAST_LIST.isEmpty()) return null;
        return CAST_LIST.get(Mth.nextInt(pLevel.getRandom(), 0, CAST_LIST.size()-1));
    }

    public StalkCastManager() {
        super(StalkCastGenerator.GSON, PATH);
    }

    protected void apply(@NotNull Map<ResourceLocation, JsonElement> map,
                         @NotNull ResourceManager pResourceManager,
                         @NotNull ProfilerFiller pProfiler){
        Map<ResourceLocation, StalkCast> parsed = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry: map.entrySet()){
            ResourceLocation key = entry.getKey();
            JsonElement value = entry.getValue();
            try {
                StalkCast cast = StalkCast.STALK_CAST_CODEC.parse(JsonOps.INSTANCE, value)
                        .getOrThrow(true, msg -> {
                            throw new IllegalStateException("Failed to parse StalkCast " + key.getPath() + ": " + msg);
                        });
                cast.setLocation(key);
                parsed.put(key, cast);
                MineBlackFlow.LOGGER.info("Stalk info read: {}", key);
            } catch (Exception e){
                MineBlackFlow.LOGGER.error(e.getMessage());
            }
        }
        LOADED_CAST = Map.copyOf(parsed);
        CAST_LIST = new ArrayList<>(LOADED_CAST.values());
    }
}
