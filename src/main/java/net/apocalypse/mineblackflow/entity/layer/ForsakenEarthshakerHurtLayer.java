package net.apocalypse.mineblackflow.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.ForsakenEarthshakerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ForsakenEarthshakerHurtLayer extends GeoRenderLayer<ForsakenEarthshakerEntity> {
    public ForsakenEarthshakerHurtLayer(GeoRenderer<ForsakenEarthshakerEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public static final ResourceLocation HURT_MASK_0 = MineBlackFlow.modLoc("textures/entity/forsaken_earthshaker_hurtmask_0.png");
    public static final ResourceLocation HURT_MASK_1 = MineBlackFlow.modLoc("textures/entity/forsaken_earthshaker_hurtmask_1.png");
    public static final ResourceLocation HURT_MASK_2 = MineBlackFlow.modLoc("textures/entity/forsaken_earthshaker_hurtmask_2.png");

    @Override
    public void render(PoseStack poseStack, ForsakenEarthshakerEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        int b = animatable.getBleeding();
        if (b >= 10) {
            RenderType glowRenderType = RenderType.eyes(hurtMaskLocation(b));
            float color = 0.6f + 0.2f * b * 0.01f;
            getRenderer().reRender(
                    getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, glowRenderType,
                    bufferSource.getBuffer(glowRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                    color, color, color, 1);
        }
    }
    private static ResourceLocation hurtMaskLocation(int b){
        if (b < 50) return HURT_MASK_0;
        else if (b < 80) return HURT_MASK_1;
        return HURT_MASK_2;
    }
}
