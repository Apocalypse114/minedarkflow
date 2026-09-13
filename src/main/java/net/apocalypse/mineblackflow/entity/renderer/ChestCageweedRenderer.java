package net.apocalypse.mineblackflow.entity.renderer;

import net.apocalypse.mineblackflow.entity.DynamicCageweedEntity;
import net.apocalypse.mineblackflow.entity.layer.SimpleGeoGlowMask;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChestCageweedRenderer extends SimpleGeoRenderer<DynamicCageweedEntity>{
    public ChestCageweedRenderer(EntityRendererProvider.Context manager) {
        super(manager, 0.6f, new SimpleGeoModel<>("chest_cageweed", ""), true);
        this.addRenderLayer(new SimpleGeoGlowMask<>(this, "chest_cageweed_glowmask", 0.5f));
    }
}
