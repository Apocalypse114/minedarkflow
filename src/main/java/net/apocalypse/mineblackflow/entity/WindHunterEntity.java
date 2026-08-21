package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.entity.base.AttackEveryoneGoal;
import net.apocalypse.mineblackflow.entity.base.ComplexMeleeAttackGoal;
import net.apocalypse.mineblackflow.entity.base.GeoBlackFlowMonster;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class WindHunterEntity extends GeoBlackFlowMonster {
    public WindHunterEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "wind_hunter");
    }
    public WindHunterEntity(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.WIND_HUNTER.get(), world);
    }

    public static final EntityDataAccessor<Boolean> MIRROR = SynchedEntityData.defineId(WindHunterEntity.class, EntityDataSerializers.BOOLEAN);
    private int COOLDOWN = 0;
    private static final EntityDataAccessor<Byte> DATA_TP_TICK = SynchedEntityData.defineId(WindHunterEntity.class, EntityDataSerializers.BYTE);

    public boolean canTp(){return COOLDOWN <= 0;}
    public void setCooldown(){COOLDOWN = 200;}
    public boolean isNotTeleporting(){
        return getTpTick() <= 0;
    }
    public byte getTpTick(){
        return this.entityData.get(DATA_TP_TICK);
    }
    public void setTpTick(byte t){
        this.entityData.set(DATA_TP_TICK, t);
    }
    private void tickCooldowns(){
        setTpTick((byte) Math.max(0, getTpTick() - 1));
        if (COOLDOWN > 0) COOLDOWN --;
    }
    @Override
    public boolean hurt(DamageSource source, float amount){
        return isNotTeleporting() && super.hurt(source, amount);
    }
    @Override
    public void actuallyHurt(DamageSource source, float amount){
        if (isNotTeleporting()) {
            super.actuallyHurt(source, amount);
            if (!this.isDeadOrDying() && amount > 0 && canTpToBb(source.getEntity()) && canTp()) {
                this.setTpTick((byte) 13);
                this.setCooldown();
                this.setDuration(13);
                MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.TP_START.get(), SoundSource.HOSTILE, 1, 0.2f);
            }
        }
    }

    @Override
    public SoundEvent getAmbientSound(){
        return MBFSounds.MOUSE.ambient().get();
    }
    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source){
        return MBFSounds.MOUSE.hurt().get();
    }
    @Override
    public @NotNull SoundEvent getDeathSound(){
        return MBFSounds.MOUSE.die().get();
    }

    @Override
    public void tick(){
        super.tick();
        tickCooldowns();
        int t = getTpTick();
        if (t == 6) tpToBb(this.getLastHurtByMob());
        if (t > 0){
            this.setDeltaMovement(new Vec3(0, this.getDeltaMovement().y, 0));
        }
    }

    private void tpToBb(@Nullable Entity entity){
        if (entity == null) return;
        Vec3 offset = entity.position().vectorTo(this.position());
        double dx = Math.abs(this.getX() - entity.getX()), targetDx = entity.getBbWidth() / 2;
        if (dx < 1e-3) {
            dx = Math.abs(this.getZ() - entity.getZ());
        }
        if (dx <= targetDx) return;
        offset = offset.scale(targetDx / dx);
        Vec3 pos = entity.position().add(offset);
        this.teleportTo(pos.x, entity.getY(), pos.z);
        MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.TP_DONE.get(), SoundSource.HOSTILE, 1, 0.2f);
    }
    private boolean canTpToBb(@Nullable Entity entity){
        if (entity == null) return false;
        double dx = Math.abs(this.getX() - entity.getX()), targetDx = entity.getBbWidth() / 2;
        if (dx < 1e-3) {
            dx = Math.abs(this.getZ() - entity.getZ());
        }
        return dx > targetDx;
    }

    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(MIRROR, false);
        this.entityData.define(DATA_TP_TICK, (byte)0);
    }
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag){
        super.readAdditionalSaveData(tag);
        if (tag.contains("mirror")) entityData.set(MIRROR, tag.getBoolean("mirror"));
        if (tag.contains("cooldown")) COOLDOWN = tag.getInt("cooldown");
        if (tag.contains("tpTick")) entityData.set(DATA_TP_TICK, tag.getByte("tpTick"));
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putBoolean("mirror", entityData.get(MIRROR));
        tag.putInt("cooldown", COOLDOWN);
        tag.putByte("tpTick", entityData.get(DATA_TP_TICK));
    }
    @Override
    public boolean doHurtTarget(@NotNull Entity pTarget){
        this.setAttackDuration(26);
        MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.MOUSE_ATTACK_PRE.get(), SoundSource.HOSTILE, 1, 0.1f);
        MineBlackFlow.queueServerWork(13, ()-> {
            if (!this.isDeadOrDying() && this.distanceToSqr(pTarget) <= 6.25){
                super.doHurtTarget(pTarget);
                MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.MOUSE_ATTACK_HIT.get(), SoundSource.HOSTILE, 1, 0.1f);
            }
        });
        return true;
    }
    @Override
    protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn) {
        return 1.55F;
    }
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(moveController(this, 1, this::moveHandler).setAnimationSpeed(1.5));
        controllers.add(attackController(this, 0, this::attackingHandler));
    }

    @Override
    public void registerGoals(){
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new ComplexMeleeAttackGoal(this, 1, false) {
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

    public PlayState moveHandler(AnimationState<?> event){
        if (this.isDeadOrDying()){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("die")));
        }
        if (this.getTpTick() > 0){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("skill")));
        }
        if (isAnimAvailable()){
            if(event.isMoving()){
                return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("move")));
            }
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("idle")));
    }
    private PlayState attackingHandler(AnimationState<?> event) {
        if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
            this.swinging = true;
            this.lastSwing = level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 26L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (getAttackDuration() > 0 && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("attack")));
        }
        return PlayState.CONTINUE;
    }
    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(60, 6, 0.25, 3, 1, 0, 27);
    }
    public boolean isReserve(){
        return this.entityData.get(MIRROR);
    }
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if(pLevel.getRandom().nextFloat() < 0.1f){
            entityData.set(MIRROR, true);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
}
