package net.apocalypse.mineblackflow.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.WaterPraiserEntity;
import net.apocalypse.mineblackflow.entity.WindHunterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WindHunterLayer extends GeoRenderLayer<WindHunterEntity> {
    public WindHunterLayer(GeoRenderer<WindHunterEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public static final ResourceLocation FULL = MineBlackFlow.modLoc("textures/entity/wind_hunter.png");

    @Override
    public void render(
            PoseStack poseStack, WindHunterEntity animatable, BakedGeoModel bakedModel, RenderType renderType,
            MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        float t = animatable.getTpTick() - partialTick, alpha = 1;

        if (3<=t && t<=5) alpha = (t - 3)/2;
        else if (7<=t && t<=9) alpha = (9 - t)/2;
        else if (5<t && t<7) alpha = 0;

        BakedGeoModel model = getDefaultBakedModel(animatable);
        RenderType typeFull = RenderType.entityTranslucent(FULL);
        VertexConsumer consumerFull = bufferSource.getBuffer(typeFull);
        getRenderer().reRender(model, poseStack, bufferSource, animatable, typeFull,
                consumerFull, partialTick, packedLight,
                packedOverlay, alpha, alpha, alpha, alpha);
    }
}
