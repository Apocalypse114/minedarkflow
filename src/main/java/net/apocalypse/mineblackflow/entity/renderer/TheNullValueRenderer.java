package net.apocalypse.mineblackflow.entity.renderer;

import net.apocalypse.mineblackflow.entity.TheNullValueEntity;
import net.apocalypse.mineblackflow.entity.layer.TheNullValueLayer;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class TheNullValueRenderer extends SimpleGeoRenderer<TheNullValueEntity>{
    public TheNullValueRenderer(EntityRendererProvider.Context manager) {
        super(manager, 0.5f, new SimpleGeoModel<>("the_null_value"));
        this.addRenderLayer(new TheNullValueLayer(this));
    }
}
