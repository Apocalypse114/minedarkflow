package net.apocalypse.mineblackflow.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.TheNullValueEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TheNullValueLayer extends GeoRenderLayer<TheNullValueEntity> {
    public TheNullValueLayer(GeoRenderer<TheNullValueEntity> entityRendererIn) {
        super(entityRendererIn);
    }
    public static final ResourceLocation MASK = MineBlackFlow.modLoc("textures/entity/the_null_value_mask.png");
    public static final ResourceLocation MASK_1 = MineBlackFlow.modLoc("textures/entity/the_null_value_mask_1.png");

    public int recordedTick = 0;
    public float dx = 0, dy = 0;
    public float dx1 = 0, dy1 = 0;

    @Override
    public void render(
            PoseStack poseStack, TheNullValueEntity animatable, BakedGeoModel bakedModel, RenderType renderType,
            MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        int tick = (int) animatable.level().getGameTime() / 2;
        if (tick != recordedTick) {
            recordedTick = tick;
            dx = Mth.nextFloat(animatable.getRandom(), 0, 1);
            dy = Mth.nextFloat(animatable.getRandom(), 0, 1);
            dx1 = Mth.nextFloat(animatable.getRandom(), 0, 1);
            dy1 = Mth.nextFloat(animatable.getRandom(), 0, 1);
        }
        RenderType energySwirlType = RenderType.energySwirl(MASK, dx, dy);
        RenderType energySwirlType1 = RenderType.energySwirl(MASK_1, dx1, dy1);
        VertexConsumer energyConsumer = bufferSource.getBuffer(energySwirlType);
        BakedGeoModel model = getDefaultBakedModel(animatable);
        getRenderer().reRender(model, poseStack, bufferSource, animatable, energySwirlType,
                energyConsumer, partialTick, packedLight,
                OverlayTexture.NO_OVERLAY, 0.75f, 0.75f, 0.75f, 1);
        getRenderer().reRender(model, poseStack, bufferSource, animatable, energySwirlType1,
                energyConsumer, partialTick, packedLight,
                OverlayTexture.NO_OVERLAY, 0.85f, 0.85f, 0.85f, 1);
    }
}
