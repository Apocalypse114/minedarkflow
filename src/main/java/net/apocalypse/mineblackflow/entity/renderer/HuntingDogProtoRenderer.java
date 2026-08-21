package net.apocalypse.mineblackflow.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.apocalypse.mineblackflow.entity.HuntingDogProtoEntity;
import net.apocalypse.mineblackflow.entity.layer.HuntingDogProtoLayer;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HuntingDogProtoRenderer extends SimpleGeoRenderer<HuntingDogProtoEntity> {
    public HuntingDogProtoRenderer(EntityRendererProvider.Context manager) {
        super(manager, 1, new SimpleGeoModel<>("huntingdog_proto", "huntingdog_proto", "empty", "headAxis"), true);
        addRenderLayer(new HuntingDogProtoLayer(this));
    }

    @Override
    public void render(HuntingDogProtoEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        int t = pEntity.getCommonSkillTick();
        if (1 <= t && t <= 205) this.shadowRadius = 0;
        else this.shadowRadius = 1f;
        if (!(3 < t && t < 203)) super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }
}
