package net.apocalypse.mineblackflow.entity.base;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class GeoBlackFlowMonster extends Monster implements IBlackFlowMonster, GeoEntity {

    public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public final String resource_id;

    protected static final EntityDataAccessor<Integer> ATTACK_DURATION = SynchedEntityData.defineId(GeoBlackFlowMonster.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> GENERAL_DURATION = SynchedEntityData.defineId(GeoBlackFlowMonster.class, EntityDataSerializers.INT);
    public int getAttackDuration(){
        return this.entityData.get(ATTACK_DURATION);
    }
    public void setAttackDuration(int t){
        this.entityData.set(ATTACK_DURATION, Math.max(0, t));
    }
    public int getDuration(){
        return this.entityData.get(GENERAL_DURATION);
    }
    public boolean isAnimAvailable(){
        return getDuration() <= 0;
    }
    public void setDuration(int t){
        this.entityData.set(GENERAL_DURATION, Math.max(0, t));
    }
    protected long lastSwing = 0;

    public GeoBlackFlowMonster(EntityType<? extends Monster> type, Level level, String resId) {
        super(type, level);
        this.resource_id = resId;
    }

    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(ATTACK_DURATION, 0);
        this.entityData.define(GENERAL_DURATION, 0);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }
    public String animLoc(String animName){
        return "animation."+resource_id+"."+(animName.isEmpty() ? "idle": animName);
    }

    @Override
    public void tick(){
        super.tick();
        this.tickAttack();
        this.setDuration(this.getDuration() - 1);
    }
    public void tickAttack(){
        int d = this.getAttackDuration();
        if (d > 0) this.setAttackDuration(d - 1);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pTarget){
        pTarget.invulnerableTime = 0;
        return super.doHurtTarget(pTarget);
    }

    public static <T extends GeoBlackFlowMonster> AnimationController<T> moveController(
            T animatable, int transitionTime, AnimationController.AnimationStateHandler<T> handler){
        return new AnimationController<>(animatable, SimpleGeoModel.CONTROLLER_MOVE, transitionTime, handler);
    }

    public static <T extends GeoBlackFlowMonster> AnimationController<T> attackController(
            T animatable, int transitionTime, AnimationController.AnimationStateHandler<T> handler){
        return new AnimationController<>(animatable, SimpleGeoModel.CONTROLLER_ATTACK, transitionTime, handler);
    }

    public static <T extends GeoBlackFlowMonster> AnimationController<T> triggerableAnimController(T animatable, String name, int transitionTime, String ... anim){
        AnimationController<T> controller = new AnimationController<>(animatable, name, transitionTime, evnt->PlayState.STOP);
        for (String singleAnim: anim){
            controller = controller.triggerableAnim(singleAnim, RawAnimation.begin().thenPlay(animatable.animLoc(singleAnim)));
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
    public boolean isValidTarget(@NotNull LivingEntity entity){
        if (entity == this) return false;
        if (MBFUtil.isSameTeam(this, entity, false)) return false;
        if (entity instanceof GeoBlackFlowMonster mob) return this.getTarget() == entity || mob.getTarget() == this;
        return true;
    }
}
