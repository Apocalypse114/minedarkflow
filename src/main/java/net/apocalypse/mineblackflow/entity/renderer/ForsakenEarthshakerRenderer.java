package net.apocalypse.mineblackflow.entity.renderer;

import net.apocalypse.mineblackflow.entity.ForsakenEarthshakerEntity;
import net.apocalypse.mineblackflow.entity.layer.ForsakenEarthshakerHurtLayer;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class ForsakenEarthshakerRenderer extends SimpleGeoRenderer<ForsakenEarthshakerEntity> {
    public ForsakenEarthshakerRenderer(EntityRendererProvider.Context manager) {
        super(manager, 2.5f, new SimpleGeoModel<>("forsaken_earthshaker", "HeadAxis"), true);
        this.addRenderLayer(new ForsakenEarthshakerHurtLayer(this));
    }
}
