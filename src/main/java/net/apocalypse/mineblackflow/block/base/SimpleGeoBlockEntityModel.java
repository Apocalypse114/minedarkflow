package net.apocalypse.mineblackflow.block.base;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.block.entity.SimpleGeoBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SimpleGeoBlockEntityModel<T extends SimpleGeoBlockEntity> extends GeoModel<T> {
    public SimpleGeoBlockEntityModel(String anim, String model, String texture){
        this.ANIM = MineBlackFlow.modLoc("animations/block/" + anim + ".animation.json");
        this.MODEL = MineBlackFlow.modLoc("geo/block/" + model + ".geo.json");;
        this.TEXTURE = MineBlackFlow.modLoc("textures/entity/" + texture + ".png");
    }
    public SimpleGeoBlockEntityModel(String model, String texture){
        this("empty", model, texture);
    }
    public SimpleGeoBlockEntityModel(String res_id){
        this(res_id, res_id);
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
