package net.apocalypse.mineblackflow.entity.renderer;

import net.apocalypse.mineblackflow.entity.WaterPraiserEntity;
import net.apocalypse.mineblackflow.entity.layer.WaterPriaserFurLayer;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.model.GeoModel;

public class WaterPraiserRenderer extends SimpleGeoRenderer<WaterPraiserEntity>{
    public WaterPraiserRenderer(EntityRendererProvider.Context manager) {
        super(manager, 1,new SimpleGeoModel<>("water_praiser", "headAxis"), true);
        this.addRenderLayer(new WaterPriaserFurLayer(this));
    }
}
