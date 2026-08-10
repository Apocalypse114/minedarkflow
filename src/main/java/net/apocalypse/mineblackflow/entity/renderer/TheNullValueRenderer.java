package net.apocalypse.mineblackflow.entity.renderer;

import net.apocalypse.mineblackflow.entity.TheNullValueEntity;
import net.apocalypse.mineblackflow.entity.layer.TheNullValueLayer;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TheNullValueRenderer extends SimpleGeoRenderer<TheNullValueEntity>{
    public TheNullValueRenderer(EntityRendererProvider.Context manager) {
        super(manager, 0.5f, new SimpleGeoModel<>("the_null_value", "head"));
        this.addRenderLayer(new TheNullValueLayer(this));
    }
}
