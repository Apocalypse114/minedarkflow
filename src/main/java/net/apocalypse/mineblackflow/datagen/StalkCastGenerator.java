package net.apocalypse.mineblackflow.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.stalk.StalkCast;
import net.apocalypse.mineblackflow.core.stalk.StalkCastManager;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class StalkCastGenerator implements DataProvider {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final PackOutput packOutput;
    private final Map<String, StalkCast> toGen = new HashMap<>();

    public @NotNull String getName(){return "stalk_cast_provider";}

    public StalkCastGenerator(PackOutput packOutput){
        this.packOutput = packOutput;
    }
    public void gatherData(){
        toGen.put("blackflow_animals", StalkCast.create()
                .addEntry(MBFEntities.WATER_PRAISER.get(), 10, 60, 5, 15, 0.1f)
                .addEntry(MBFEntities.WIND_HUNTER.get(), 8, 80, 5, 15, 0.1f)
                .addEntry(MBFEntities.FORSAKEN_EARTHSHAKER.get(), 2, 180, 9, 16, 0.25f)
                .addEntry(MBFEntities.HUNTING_DOG_PROTO.get(), 4, 100, 3, 12, 0.1f));
        toGen.put("many_zombie", StalkCast.create()
                        .addEntry(EntityType.ZOMBIE, 5, 20, 5, 15, 0.1f)
                        .addEntry(EntityType.ZOMBIE_VILLAGER, 5, 80, 5, 15, 0.1f)
                        .addEntry(EntityType.STRAY, 5, 140, 5, 15, 0.1f));
    }

    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput pOutput) {
        gatherData();
        return CompletableFuture.runAsync(()-> {
            try {
                runData(pOutput);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void runData(CachedOutput cachedOutput) throws IOException {
        for (var entry: toGen.entrySet()){
            String id = entry.getKey();
            StalkCast cast = entry.getValue();

            JsonElement json = StalkCast.STALK_CAST_CODEC
                    .encodeStart(JsonOps.INSTANCE, cast)
                    .getOrThrow(false, msg -> {throw new RuntimeException(msg);});
            Path path = packOutput.getOutputFolder().resolve("data")
                    .resolve(MineBlackFlow.MODID).resolve(StalkCastManager.PATH)
                    .resolve(id+".json");
            DataProvider.saveStable(cachedOutput, json, path);
            Files.createDirectories(path.getParent());
            String jsonString = GSON.toJson(json); // 需
            Files.writeString(path, jsonString, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            DataProvider.LOGGER.info("gathered:{}", path);
        }
    }

}
