package net.apocalypse.mineblackflow.entity.renderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SimpleGeoRenderer<T extends Entity & GeoAnimatable> extends GeoEntityRenderer<T> {
    public final boolean withCustomDeathAnim;

    public SimpleGeoRenderer(
            EntityRendererProvider.Context manager, float shadowRadius,
            GeoModel<T> model, boolean customDeathAnim
    ){
        super(manager, model);
        this.shadowRadius = shadowRadius;
        this.withCustomDeathAnim = customDeathAnim;
    }

    public SimpleGeoRenderer(
            EntityRendererProvider.Context manager, float shadowRadius, GeoModel<T> model
    ){
        this(manager, shadowRadius, model, false);
    }

    @Override
    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return withCustomDeathAnim ? 0f : super.getDeathMaxRotation(animatable);
    }
}

