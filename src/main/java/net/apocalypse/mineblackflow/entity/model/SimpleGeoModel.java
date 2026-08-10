package net.apocalypse.mineblackflow.entity.model;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.base.GeoBlackFlowMonster;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SimpleGeoModel<T extends GeoBlackFlowMonster> extends GeoModel<T> {
    public SimpleGeoModel(String anim, String model, String texture, String headSet){
        this.ANIM = MineBlackFlow.modLoc("animations/" + anim + ".animation.json");
        this.MODEL = MineBlackFlow.modLoc("geo/" + model + ".geo.json");;
        this.TEXTURE = MineBlackFlow.modLoc("textures/entity/" + texture + ".png");
        this.headSet = headSet;
    }
    public SimpleGeoModel(String res_id, String headSet){
        this(res_id, res_id, res_id, headSet);
    }
    public SimpleGeoModel(String res_id){
        this(res_id, res_id, res_id, "");
    }

    public static final String CONTROLLER_MOVE = "movement", CONTROLLER_ATTACK = "attack";

    public final ResourceLocation ANIM;
    public final ResourceLocation MODEL;
    public final ResourceLocation TEXTURE;
    public final String headSet;

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
        if (!headSet.isEmpty()) {
            CoreGeoBone head = getAnimationProcessor().getBone(headSet);
            if (head != null){
                var data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                head.setRotX(data.headPitch() * Mth.DEG_TO_RAD);
                head.setRotY(data.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }
    }
}