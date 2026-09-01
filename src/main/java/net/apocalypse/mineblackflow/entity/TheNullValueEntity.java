package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.entity.base.AttackEveryoneGoal;
import net.apocalypse.mineblackflow.entity.base.GeoBlackFlowMonster;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class TheNullValueEntity extends GeoBlackFlowMonster {

    public TheNullValueEntity(EntityType<?> ignored, Level level) {
        super(MBFEntities.THE_NULL_VALUE.get(), level, "the_null_value");
    }

    public TheNullValueEntity(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.THE_NULL_VALUE.get(), world);
    }

    @Override
    public SoundEvent getAmbientSound(){
        return MBFSounds.DOG.ambient().get();
    }
    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source){
        return MBFSounds.DOG.hurt().get();
    }
    @Override
    public @NotNull SoundEvent getDeathSound(){
        return MBFSounds.DOG.die().get();
    }

    public void registerGoals(){
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.3, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 4;
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new AttackEveryoneGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(moveController(this, 1, this::moveHandler));
    }

    public PlayState moveHandler(AnimationState<?> event){
        if (isAnimAvailable()){
            if(event.isMoving()){
                if(this.isAggressive()) return event.setAndContinue(RawAnimation.begin().thenLoop("animation.the_null_value.run"));
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.the_null_value.walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.the_null_value.idle"));
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(20, 3, 0.25, 1, 24);
    }
}
