package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.entity.base.AttackEveryoneGoal;
import net.apocalypse.mineblackflow.entity.base.RangedBlackFlowMonster;
import net.apocalypse.mineblackflow.entity.projectile.WaterPraiserArrow;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class WaterPraiserEntity extends RangedBlackFlowMonster {
    public WaterPraiserEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "water_praiser");
    }
    public WaterPraiserEntity(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.WATER_PRAISER.get(), world);
    }

    private static final EntityDataAccessor<Byte> DATA_COLOR = SynchedEntityData.defineId(WaterPraiserEntity.class, EntityDataSerializers.BYTE);
    private int COOLDOWN = 0;
    private static final EntityDataAccessor<Byte> DATA_TP_TICK = SynchedEntityData.defineId(WaterPraiserEntity.class, EntityDataSerializers.BYTE);

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
    public int getColor(){
        return colorFromId(this.entityData.get(DATA_COLOR));
    }
    public void setColor(byte color){
        this.entityData.set(DATA_COLOR, color);
    }

    @Override
    public SoundEvent getAmbientSound(){
        return MBFSounds.SHEEP.ambient().get();
    }
    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source){
        return MBFSounds.SHEEP.hurt().get();
    }
    @Override
    public @NotNull SoundEvent getDeathSound(){
        return MBFSounds.SHEEP.die().get();
    }

    @Override
    public boolean hurt(DamageSource source, float amount){
        return isNotTeleporting() && super.hurt(source, amount);
    }
    @Override
    public void actuallyHurt(DamageSource source, float amount){
        if (isNotTeleporting()) {
            super.actuallyHurt(source, amount);
            if (!this.isDeadOrDying() && amount > 0 && canTp()) {
                this.setTpTick((byte) 20);
                this.setCooldown();
                this.setDuration(20);
                MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.TP_START.get(), SoundSource.HOSTILE,
                        1, 0.2f);
            }
        }
    }

    @Override
    public void tick(){
        super.tick();
        tickCooldowns();
        int t = getTpTick();
        if (t == 10) tpBackWards();
        if (t > 0){
            this.setDeltaMovement(new Vec3(0, this.getDeltaMovement().y, 0));
        }
    }

    private void tpBackWards(){
        Vec3 revFacing = this.getLookAngle().reverse();
        Vec3 revPointing = new Vec3(revFacing.x, 0, revFacing.z).normalize();
        if (revPointing.lengthSqr() < 1e-6) return;
        AABB aabb;
        Vec3 targetPos = this.position();
        for (int d = 3; d>0; d--){
            aabb = this.getBoundingBox().move(revPointing.scale(d * 2));
            if (this.level().noCollision(aabb)){
                targetPos = this.position().add(revPointing.scale(d * 2));
                break;
            }
        }
        MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.TP_DONE.get(), SoundSource.HOSTILE,
                1, 0.2f);
        this.teleportTo(targetPos.x, targetPos.y, targetPos.z);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        float r = pLevel.getRandom().nextFloat();
        byte c = 0;
        if (r >= 0.7f){
            if (r < 0.74f) c = 1;
            else if (r < 0.78f) c = 2;
            else if (r < 0.82f) c = 3;
            else if (r < 0.86f) c = 4;
            else if (r < 0.9f) c = 5;
            else if (r < 0.94f) c = 6;
            else if (r < 0.98f) c = 7;
            else if (r < 0.99f) c = 8;
            else c = 9;
        }
        this.setColor(c);
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public static int colorFromId(byte id){
        return switch (id){
            case 0 -> 0x000000; //vanilla
            case 1 -> 0xecdada; //red
            case 2 -> 0xece2da; //orange
            case 3 -> 0xececda; //yellow
            case 4 -> 0xdbecda; //green
            case 5 -> 0xdaecec; //blue
            case 6 -> 0xdfdaec; //indigo
            case 7 -> 0xe4daec; //violet
            case 8 -> 0xebcaeb; //pink
            case 9 -> 0xc8ece8; //cyan
            default -> 0xffffff;
        };
    }

    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR, (byte)0);
        this.entityData.define(DATA_TP_TICK, (byte)0);
    }

    public void performRangedAttack(@NotNull LivingEntity pTarget, float pVelocity){
        startShootAnim(28);
        MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.SHEEP_ATTACK_PRE.get(), SoundSource.HOSTILE, 1, 0.1f);
        MineBlackFlow.queueServerWork(15, ()-> WaterPraiserArrow.shootTo(this, pTarget));
    }

    @Override
    public void registerGoals(){
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1, 30, 50, 8){
            @Override
            public boolean canUse(){return super.canUse() && isNotTeleporting();}
            @Override
            public boolean canContinueToUse(){return super.canContinueToUse() && isNotTeleporting();}
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new AttackEveryoneGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1){
            @Override
            public boolean canUse(){return super.canUse() && isNotTeleporting();}
            @Override
            public boolean canContinueToUse(){return super.canContinueToUse() && isNotTeleporting();}
        });
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this){
            @Override
            public boolean canUse(){return super.canUse() && isNotTeleporting();}
            @Override
            public boolean canContinueToUse(){return super.canContinueToUse() && isNotTeleporting();}
        });
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(moveController(this, 1, this::moveHandler));
        controllers.add(attackController(this, 0, this::attackingHandler));
    }

    public PlayState moveHandler(AnimationState<?> event){
        if (this.isDeadOrDying()){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("die")));
        }
        if (this.getTpTick() > 0){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("dodge")));
        }
        if (isAnimAvailable()){
            if(event.isMoving()){
                return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("move")));
            }
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("idle")));
    }
    private PlayState attackingHandler(AnimationState<?> event) {
        if (this.shouldPlayShootAnim(28) && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("attack")));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag){
        super.readAdditionalSaveData(tag);
        if (tag.contains("color")) this.setColor(tag.getByte("color"));
        if (tag.contains("cooldown"))COOLDOWN = tag.getInt("cooldown");
        if (tag.contains("tpTick")) this.entityData.set(DATA_TP_TICK, tag.getByte("tpTick"));
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putInt("color", this.entityData.get(DATA_COLOR));
        tag.putInt("cooldown", COOLDOWN);
        tag.putByte("tpTick", this.entityData.get(DATA_TP_TICK));
    }

    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(32, 5, 0.2, 2, 16);
    }
}
