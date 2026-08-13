package net.apocalypse.mineblackflow.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.MBFMath;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.entity.WaterPraiserEntity;
import net.apocalypse.mineblackflow.item.base.UltraApocataItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WaterPraiserFurLayer extends GeoRenderLayer<WaterPraiserEntity> {
    public WaterPraiserFurLayer(GeoRenderer<WaterPraiserEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    public static final ResourceLocation FUR_VANILLA = MineBlackFlow.modLoc("textures/entity/water_praiser_vanilla.png");
    public static final ResourceLocation FUR = MineBlackFlow.modLoc("textures/entity/water_praiser_fur.png");
    public static final ResourceLocation FULL = MineBlackFlow.modLoc("textures/entity/water_praiser.png");

    @Override
    public void render(
            PoseStack poseStack, WaterPraiserEntity animatable, BakedGeoModel bakedModel, RenderType renderType,
            MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay){
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);

        int color;
        if (animatable.hasCustomName() && "jeb_".equals(animatable.getName().getString()))
            color = lerpRainbowColor((animatable.tickCount+partialTick) % 70);
        else if (animatable.hasCustomName() && "Apocata".equals(animatable.getName().getString())){
            float p = (animatable.tickCount + partialTick) % 40 / 20;
            color = MBFUtil.lerpColor(
                    UltraApocataItems.APOCATA_COLOR_1, UltraApocataItems.APOCATA_COLOR_2, p <= 1f ? p: 2 - p);
        }
        else color = animatable.getColor();

        BakedGeoModel model = getDefaultBakedModel(animatable);

        float t = animatable.getTpTick() - partialTick;
        float alpha = 1;
        if (t>0){
            if (12<t && t<17) alpha = (t - 12) / 5;
            else if (4<t && t<9) alpha = (9 - t) / 5;
            else if (9<=t && t<= 12) alpha = 0;
        }

        RenderType typeFull = RenderType.entityTranslucent(FULL);
        VertexConsumer consumerFull = bufferSource.getBuffer(typeFull);
        getRenderer().reRender(model, poseStack, bufferSource, animatable, typeFull,
                consumerFull, partialTick, packedLight,
                packedOverlay, 1, 1, 1, alpha);

        if (color == 0){
            RenderType type = RenderType.entityTranslucent(FUR_VANILLA);
            VertexConsumer consumer = bufferSource.getBuffer(type);
            getRenderer().reRender(model, poseStack, bufferSource, animatable, type,
                    consumer, partialTick, packedLight,
                    packedOverlay, 1, 1, 1, alpha);
        } else {
            RenderType type = RenderType.entityTranslucent(FUR);
            VertexConsumer consumer = bufferSource.getBuffer(type);
            float[] splitColor = MBFMath.splitRGB(color);
            getRenderer().reRender(model, poseStack, bufferSource, animatable, type,
                    consumer, partialTick, packedLight,
                    packedOverlay, splitColor[0], splitColor[1], splitColor[2], alpha);
        }
    }

    public static int lerpRainbowColor(float t){
        float partialT = t % 10 / 10;
        if (t<10) return MBFUtil.lerpColor(0xecdada, 0xece2da, partialT);
        else if (t<20) return MBFUtil.lerpColor(0xece2da, 0xececda, partialT);
        else if (t<30) return MBFUtil.lerpColor(0xececda, 0xdbecda, partialT);
        else if (t<40) return MBFUtil.lerpColor(0xdbecda, 0xdaecec, partialT);
        else if (t<50) return MBFUtil.lerpColor(0xdaecec, 0xdfdaec, partialT);
        else if (t<60) return MBFUtil.lerpColor(0xdfdaec, 0xe4daec, partialT);
        return MBFUtil.lerpColor(0xe4daec, 0xecdada, partialT);
    }
}
