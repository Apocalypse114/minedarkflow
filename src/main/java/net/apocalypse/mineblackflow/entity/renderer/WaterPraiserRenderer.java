package net.apocalypse.mineblackflow.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.apocalypse.mineblackflow.entity.WaterPraiserEntity;
import net.apocalypse.mineblackflow.entity.layer.WaterPraiserFurLayer;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class WaterPraiserRenderer extends SimpleGeoRenderer<WaterPraiserEntity> {
    public WaterPraiserRenderer(EntityRendererProvider.Context manager) {
        super(manager, 0.8f, new SimpleGeoModel<>("water_praiser", "water_praiser", "empty", "headAxis"), true);
        this.addRenderLayer(new WaterPraiserFurLayer(this));
    }

    @Override
    public void render(WaterPraiserEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        int t = pEntity.getTpTick();
        if (8 <= t && t <= 13) this.shadowRadius = 0;
        else this.shadowRadius = 0.8f;
    }
}
