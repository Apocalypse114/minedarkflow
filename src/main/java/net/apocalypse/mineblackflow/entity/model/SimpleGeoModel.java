package net.apocalypse.mineblackflow.entity.model;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class SimpleGeoModel<T extends GeoAnimatable> extends GeoModel<T> {
    public SimpleGeoModel(String anim, String model, String texture){
        this.ANIM = MineBlackFlow.modLoc("animations/" + anim + ".animation.json");
        this.MODEL = MineBlackFlow.modLoc("geo/" + model + ".geo.json");;
        this.TEXTURE = MineBlackFlow.modLoc("textures/entity/" + texture + ".png");;
    }

    public final ResourceLocation ANIM;
    public final ResourceLocation MODEL;
    public final ResourceLocation TEXTURE;

    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return ANIM;
    }

    @Override
    public ResourceLocation getModelResource(T entity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return TEXTURE;
    }
}