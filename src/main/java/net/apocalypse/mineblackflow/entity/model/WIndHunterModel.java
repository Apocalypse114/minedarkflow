package net.apocalypse.mineblackflow.entity.model;

import net.apocalypse.mineblackflow.entity.WindHunterEntity;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class WIndHunterModel extends SimpleGeoModel<WindHunterEntity> {
    public WIndHunterModel() {
        super("wind_hunter", "wind_hunter", "empty", "headAxis");
    }

    @Override
    public void setCustomAnimations(WindHunterEntity animatable, long instanceId, AnimationState<WindHunterEntity> animationState){
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone root = getAnimationProcessor().getBone("Root");
        if (root != null) {
            if (animatable.isReserve()) root.setScaleX(-1);
            else root.setScaleX(1);
        }
    }
}
