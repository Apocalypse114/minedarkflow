package net.apocalypse.mineblackflow.entity.renderer;

import net.apocalypse.mineblackflow.entity.AberrantCageweedEntity;
import net.apocalypse.mineblackflow.entity.MotionlessCageweedEntity;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MotionlessCageweedRenderer{
    @Contract("_ -> new")
    public static @NotNull SimpleGeoRenderer<MotionlessCageweedEntity> common(EntityRendererProvider.Context manager){
        return new SimpleGeoRenderer<>(manager, 0.5f,
                new SimpleGeoModel<>("cageweed", "cageweed", "cageweed", ""), true);
    }
    @Contract("_ -> new")
    public static @NotNull SimpleGeoRenderer<AberrantCageweedEntity> aberrant(EntityRendererProvider.Context manager){
        return new SimpleGeoRenderer<>(manager, 0.5f,
                new SimpleGeoModel<>("cageweed", "cageweed", "aberrant_cageweed", ""), true);
    }
}
