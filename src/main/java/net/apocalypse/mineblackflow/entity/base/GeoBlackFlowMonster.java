package net.apocalypse.mineblackflow.entity.base;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class GeoBlackFlowMonster extends Monster implements BlackFlowMonster, GeoEntity {

    public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final String resource_id;
    public int animationOccupationTick = 0;

    protected int attackTick;

    protected GeoBlackFlowMonster(EntityType<? extends Monster> type, Level level, String resId) {
        super(type, level);
        this.resource_id = resId;
        this.attackTick = 0;
    }

    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
    public String animLoc(String animName){
        return "animation."+resource_id+"."+(animName.isEmpty() ? "idle": animName);
    }
    public void tick(){
        super.tick();
        if(attackTick > 0) attackTick--;
        if(animationOccupationTick > 0) animationOccupationTick--;
    }

    public PlayState simpleAttackHandler(AnimationState<?> event, String attackAnim, int attackAnimDuration){
        if (getAttackAnim(event.getPartialTick()) > 0 && !this.swinging){
            attackTick = attackAnimDuration;
            this.swinging = true;
        }
        if (this.swinging && attackTick <= 0) this.swinging = false;
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED && animationOccupationTick <= 0) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc(attackAnim)));
        }
        return PlayState.CONTINUE;
    }

    public static <T extends GeoBlackFlowMonster> AnimationController<T> moveController(
            T animatable, int transitionTime, AnimationController.AnimationStateHandler<T> handler){
        return new AnimationController<>(animatable, SimpleGeoModel.CONTROLLER_MOVE, transitionTime, handler);
    }

    public static <T extends GeoBlackFlowMonster> AnimationController<T> simpleAttackController(
            T animatable, int transitionTime, String attackAnim, int duration){
        return new AnimationController<>(animatable, SimpleGeoModel.CONTROLLER_ATTACK, transitionTime,
                event -> animatable.simpleAttackHandler(event, attackAnim, duration));
    }

    public static <T extends GeoBlackFlowMonster> AnimationController<T> triggerableAnimController(T animatable, String name, int transitionTime, String ... anim){
        AnimationController<T> controller = new AnimationController<>(animatable, name, transitionTime, evnt->PlayState.STOP);
        for (String singleAnim: anim){
            controller = controller.triggerableAnim(singleAnim, RawAnimation.begin().thenPlay(singleAnim));
        }
        return controller;
    }

    public static double getMoveSpeedSpeeder(LivingEntity living){
        AttributeInstance instance = living.getAttribute(Attributes.MOVEMENT_SPEED);
        if (instance != null){
            return instance.getValue() / instance.getBaseValue();
        }
        return 1;
    }
}
