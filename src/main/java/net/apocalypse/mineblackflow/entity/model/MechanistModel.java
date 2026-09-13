package net.apocalypse.mineblackflow.entity.model;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.npc.AbstractMechanistEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MechanistModel<T extends AbstractMechanistEntity> extends GeoModel<T> {

    public static final ResourceLocation MODEL = MineBlackFlow.modLoc("geo/mechanist.geo.json"),
            ANIM = MineBlackFlow.modLoc("animations/mechanist.animation.json"),
            TEXTURE = MineBlackFlow.modLoc("textures/entity/mechanist.png");

    public ResourceLocation getModelResource(T animatable){
        return MODEL;
    }

    public ResourceLocation getTextureResource(T animatable){
        return TEXTURE;
    }

    public ResourceLocation getAnimationResource(T animatable){
        return ANIM;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState){
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = getAnimationProcessor().getBone("headAxis");
        if (head != null){
            var data = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(data.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(data.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
