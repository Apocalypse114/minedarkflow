package net.apocalypse.mineblackflow.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.apocalypse.mineblackflow.entity.WaterPraiserEntity;
import net.apocalypse.mineblackflow.entity.WindHunterEntity;
import net.apocalypse.mineblackflow.entity.layer.WindHunterLayer;
import net.apocalypse.mineblackflow.entity.model.WIndHunterModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;

public class WindHunterRenderer extends SimpleGeoRenderer<WindHunterEntity> {
    public WindHunterRenderer(EntityRendererProvider.Context manager) {
        super(manager, 0.75f, new WIndHunterModel(), true);
        this.addRenderLayer(new WindHunterLayer(this));
    }

    @Override
    public void render(WindHunterEntity pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
        int t = pEntity.getTpTick();
        if (4 <= t && t <= 7) this.shadowRadius = 0;
        else this.shadowRadius = 0.75f;
    }
}
