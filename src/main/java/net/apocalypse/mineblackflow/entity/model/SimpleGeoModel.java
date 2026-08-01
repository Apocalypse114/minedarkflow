package net.apocalypse.mineblackflow.entity.model;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.base.GeoBlackFlowMonster;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SimpleGeoModel<T extends GeoBlackFlowMonster> extends GeoModel<T> {
    public SimpleGeoModel(String anim, String model, String texture){
        this.ANIM = MineBlackFlow.modLoc("animations/" + anim + ".animation.json");
        this.MODEL = MineBlackFlow.modLoc("geo/" + model + ".geo.json");;
        this.TEXTURE = MineBlackFlow.modLoc("textures/entity/" + texture + ".png");;
    }
    public SimpleGeoModel(String res_id){
        this(res_id, res_id, res_id);
    }

    public static final String CONTROLLER_MOVE = "movement", CONTROLLER_ATTACK = "attack";

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

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState){
        super.setCustomAnimations(animatable, instanceId, animationState);
        AnimationController<T> controller = animationState.getController();
        if(CONTROLLER_MOVE.equals(controller.getName())){
            controller.setAnimationSpeed(T.getMoveSpeedSpeeder(animatable));
        }
    }
}