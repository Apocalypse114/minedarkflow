package net.apocalypse.mineblackflow.entity.npc;

import net.apocalypse.mineblackflow.entity.model.SimpleGeoModel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractMechanistEntity extends PathfinderMob implements GeoEntity {
    public AbstractMechanistEntity(EntityType<? extends PathfinderMob> type, Level level){
        super(type, level);
    }

    public AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public AnimatableInstanceCache getAnimatableInstanceCache(){
        return cache;
    }

    protected long lastSwing = 0;
    protected static final EntityDataAccessor<Integer> ATTACK_DURATION = SynchedEntityData.defineId(AbstractMechanistEntity.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> GENERAL_DURATION = SynchedEntityData.defineId(AbstractMechanistEntity.class, EntityDataSerializers.INT);
    public int getAttackDuration(){return this.entityData.get(ATTACK_DURATION);}
    public void setAttackDuration(int t){
        this.entityData.set(ATTACK_DURATION, Math.max(0, t));
    }
    public int getDuration(){
        return this.entityData.get(GENERAL_DURATION);
    }
    public boolean isActionsAvailable(){
        return getDuration() <= 0;
    }
    public void setDuration(int t){
        this.entityData.set(GENERAL_DURATION, Math.max(0, t));
    }

    @Override
    public void tick(){
        super.tick();
        this.setAttackDuration(this.getAttackDuration() - 1);
        this.setDuration(this.getDuration() - 1);
    }

    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(ATTACK_DURATION, 0);
        this.entityData.define(GENERAL_DURATION, 0);
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(moveController(this, 1, this::moveHandler));
        controllers.add(attackController(this, 0, this::attackingHandler));
    }

    public static <T extends GeoEntity> AnimationController<T> moveController(
            T animatable, int transitionTime, AnimationController.AnimationStateHandler<T> handler){
        return new AnimationController<>(animatable, SimpleGeoModel.CONTROLLER_MOVE, transitionTime, handler);
    }

    public static <T extends GeoEntity> AnimationController<T> attackController(
            T animatable, int transitionTime, AnimationController.AnimationStateHandler<T> handler){
        return new AnimationController<>(animatable, SimpleGeoModel.CONTROLLER_ATTACK, transitionTime, handler);
    }

    public abstract PlayState moveHandler(AnimationState<?> event);
    public abstract PlayState attackingHandler(AnimationState<?> event);
    public String animLoc(String animName){
        return "animation.mechanist."+animName;
    }
}
