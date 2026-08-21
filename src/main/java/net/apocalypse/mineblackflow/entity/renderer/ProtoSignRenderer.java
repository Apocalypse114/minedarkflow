package net.apocalypse.mineblackflow.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.client.model.ProtoSignModel;
import net.apocalypse.mineblackflow.entity.technical.ProtoSignEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ProtoSignRenderer extends EntityRenderer<ProtoSignEntity> {
    private final ProtoSignModel model;

    public ProtoSignRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ProtoSignModel(pContext.bakeLayer(ProtoSignModel.LAYER_LOCATION));
    }

    public void render(ProtoSignEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight){
        float t = pEntity.tickCount + pPartialTicks;
        VertexConsumer buffer = pBuffer.getBuffer(RenderType.entityCutout(getTextureLocation(pEntity)));
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(t * 6));
        pPoseStack.translate(0, 0.25 + 0.25 * Mth.sin(t * 10 * Mth.DEG_TO_RAD), 0);
        model.setupAnim(pEntity, 0, 0, t, 0, 0);
        model.renderToBuffer(
                pPoseStack,
                buffer,
                0x0f00f0,
                OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    protected int getSkyLightLevel(@NotNull ProtoSignEntity pEntity, @NotNull BlockPos pPos) {
        return 15;
    }

    protected int getBlockLightLevel(@NotNull ProtoSignEntity pEntity, @NotNull BlockPos pPos) {
        return 15;
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull ProtoSignEntity pEntity){
        return MineBlackFlow.modLoc("textures/entity/proto_sign.png");
    }
}
