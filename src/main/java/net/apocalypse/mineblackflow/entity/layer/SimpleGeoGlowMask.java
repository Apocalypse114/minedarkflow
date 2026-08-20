package net.apocalypse.mineblackflow.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SimpleGeoGlowMask<T extends GeoEntity> extends GeoRenderLayer<T> {

    private final ResourceLocation LAYER;
    private final float lit;

    public SimpleGeoGlowMask(GeoRenderer<T> entityRendererIn, String layerLoc, float lit) {
        super(entityRendererIn);
        this.LAYER = MineBlackFlow.modLoc("textures/entity/glow_mask/" + layerLoc + ".png");
        this.lit = lit;
    }
    public SimpleGeoGlowMask(GeoRenderer<T> entityRendererIn, String layerLoc){
        this(entityRendererIn, layerLoc, 1);
    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType glowRenderType = RenderType.eyes(LAYER);
        getRenderer().reRender(
                getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType,
                bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                lit, lit, lit, 1);

        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }
}
