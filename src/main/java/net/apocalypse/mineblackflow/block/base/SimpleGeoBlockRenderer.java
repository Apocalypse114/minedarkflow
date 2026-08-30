package net.apocalypse.mineblackflow.block.base;

import net.apocalypse.mineblackflow.block.entity.SimpleGeoBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SimpleGeoBlockRenderer<T extends SimpleGeoBlockEntity> extends GeoBlockRenderer<T> {
    public SimpleGeoBlockRenderer(GeoModel<T> model) {
        super(model);
    }

    @Override
    public RenderType getRenderType(T animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

}
