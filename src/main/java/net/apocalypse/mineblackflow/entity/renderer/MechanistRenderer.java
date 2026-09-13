package net.apocalypse.mineblackflow.entity.renderer;

import net.apocalypse.mineblackflow.entity.layer.SimpleGeoGlowMask;
import net.apocalypse.mineblackflow.entity.model.MechanistModel;
import net.apocalypse.mineblackflow.entity.npc.AbstractMechanistEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MechanistRenderer<T extends AbstractMechanistEntity> extends GeoEntityRenderer<T> {
    public MechanistRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MechanistModel<>());
        addRenderLayer(new SimpleGeoGlowMask<>(this, "mechanist_glowmask", 0.64f));
    }
    @Override
    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 0;
    }
}
