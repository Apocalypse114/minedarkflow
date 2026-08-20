package net.apocalypse.mineblackflow.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.HuntingDogProtoEntity;
import net.apocalypse.mineblackflow.entity.WindHunterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class HuntingDogProtoLayer extends SimpleGeoGlowMask<HuntingDogProtoEntity> {
    public HuntingDogProtoLayer(GeoRenderer<HuntingDogProtoEntity> entityRendererIn) {
        super(entityRendererIn, "huntingdog_proto_glowmask");
    }

    public static final ResourceLocation FULL = MineBlackFlow.modLoc("textures/entity/huntingdog_proto.png");

    @Override
    public void render(
            PoseStack poseStack, HuntingDogProtoEntity animatable, BakedGeoModel bakedModel, RenderType renderType,
            MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {

        float t = animatable.getCommonSkillTick() + partialTick, alpha = 1;

        if (animatable.inSkill()) {
            if (1 <= t && t <= 3) alpha = (3 - t) / 2;
            else if (205 <= t && t <= 208) alpha = (t - 205) / 3;
            else if (3 < t && t < 203) alpha = 0;
        }

        BakedGeoModel model = getDefaultBakedModel(animatable);
        RenderType typeFull = RenderType.entityTranslucent(FULL);
        VertexConsumer consumerFull = bufferSource.getBuffer(typeFull);
        getRenderer().reRender(model, poseStack, bufferSource, animatable, typeFull,
                consumerFull, partialTick, packedLight,
                packedOverlay, alpha, alpha, alpha, alpha);

        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }
}
