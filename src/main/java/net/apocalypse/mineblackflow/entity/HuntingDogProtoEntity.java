package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.ManiaInjury;
import net.apocalypse.mineblackflow.core.ManiaInjurySource;
import net.apocalypse.mineblackflow.entity.base.AttackEveryoneGoal;
import net.apocalypse.mineblackflow.entity.base.ComplexMeleeAttackGoal;
import net.apocalypse.mineblackflow.entity.base.GeoBlackFlowMonster;
import net.apocalypse.mineblackflow.entity.base.IBlackFlowMonster;
import net.apocalypse.mineblackflow.entity.technical.ProtoSignEntity;
import net.apocalypse.mineblackflow.init.MBFEffects;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFParticleTypes;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.UUID;

public class HuntingDogProtoEntity extends GeoBlackFlowMonster {
    public HuntingDogProtoEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "huntingdog_proto");
    }
    public HuntingDogProtoEntity(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.HUNTING_DOG_PROTO.get(), world);
    }

    private static final EntityDataAccessor<Integer> SKILL_TICK = SynchedEntityData.defineId(HuntingDogProtoEntity.class, EntityDataSerializers.INT);
    private int skillCooldown = 200;
    private Entity attachedEntity = null;
    private Vec3 previousPos = Vec3.ZERO;
    private UUID attackedEntityUUID = null;

    public Vec3 posPrevious(){return previousPos;}

    public int getSkillTick(){
        return entityData.get(SKILL_TICK);
    }
    public int getCommonSkillTick(){
        return 211 - getSkillTick();
    }
    public void setSkillTick(int t){
        entityData.set(SKILL_TICK, t);
    }
    public boolean inSkill(){
        return getSkillTick() > 0;
    }

    @Override
    public void tick(){
        super.tick();
        if (!this.isDeadOrDying() && !this.level().isClientSide()) {
            if (inSkill()){
                if (attachedEntity == null && attackedEntityUUID != null && this.level() instanceof ServerLevel level){
                    attachedEntity = level.getEntity(attackedEntityUUID);
                }
                int t = getSkillTick();
                if (t > 0) setSkillTick(t - 1);
                this.setDeltaMovement(Vec3.ZERO);
                if (t >= 8 && t <= 208) this.setPos(attachedEntity.position());
                boolean isValidTarget = attachedEntity != null && attachedEntity.isAlive();
                if (t > 3 && t % 2 == 0
                        && isValidTarget
                        && attachedEntity instanceof LivingEntity living && !living.hasEffect(MBFEffects.DOG_PROTO_BITE.get())){
                    living.addEffect(new MobEffectInstance(MBFEffects.DOG_PROTO_BITE.get(), 20));
                }
                if (inSkillMainPart() && isValidTarget && (t - 3) % 10 == 5){
                    MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.DOG_PROTO_BITE.get(), SoundSource.HOSTILE, 0.75f, 0.2f);
                    hurtEnemy(attachedEntity, true);
                } else if (t == 8 || !isValidTarget){
                    MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.TP_DONE.get(), SoundSource.HOSTILE, 1, 0.1f);
                    this.setPos(previousPos);
                    if (t > 8) setSkillTick(8);
                }
            } else{
                if (skillCooldown > 0) skillCooldown--;
                else {
                    Entity target = this.getTarget();
                    if (target != null && target.isAlive() && this.distanceToSqr(target) <= 36){
                        setSkillTick(211);
                        skillCooldown = 600;
                        attachedEntity = target;
                        ProtoSignEntity.setup(this);
                        MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.DOG_PROTO_SKILL.get(), SoundSource.HOSTILE, 1, 0.1f);
                        previousPos = this.position();
                    }
                }
            }
        } else if (this.isDeadOrDying()){
            setSkillTick(0);
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount){
        if (inSkill() && source.is(DamageTypes.CRAMMING)) return false;
        return super.hurt(source, amount);
    }

    @Override
    public void actuallyHurt(@NotNull DamageSource source, float amount){
        if (MBFUtil.isNotRealDamage(source)){
            if (inSkillPostPart() || inSkillPrePart()) return;
            if (inSkill()) amount *= 0.2f;
        }
        super.actuallyHurt(source, amount);
    }

    @Override
    public void doPush(@NotNull Entity entity){
        if (!inSkill()) super.doPush(entity);
    }
    @Override
    public void push(@NotNull Entity entity){
        if (!inSkill()) super.push(entity);
    }

    @Override
    public SoundEvent getAmbientSound(){
        return MBFSounds.DOG_PROTO.ambient().get();
    }
    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source){
        return MBFSounds.DOG_PROTO.hurt().get();
    }
    @Override
    public @NotNull SoundEvent getDeathSound(){
        return MBFSounds.DOG_PROTO.die().get();
    }

    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(SKILL_TICK, 0);
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(moveController(this, 1, this::moveHandler));
        controllers.add(attackController(this, 0, this::attackingHandler));
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return inSkill() ? super.getDimensions(pPose).scale(0.1f): super.getDimensions(pPose);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pTarget){
        this.setAttackDuration(29);
        MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.DOG_PROTO_ATTACK.get(), SoundSource.HOSTILE, 1, 0.15f);
        MineBlackFlow.queueServerWork(16, ()-> {
            if (!this.isDeadOrDying() && this.distanceToSqr(pTarget) <= 16){
                hurtEnemy(pTarget, false);
            }
        });
        return true;
    }
    private void hurtEnemy(Entity enemy, boolean skill){
        float atk = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (skill) {
            atk *= 0.5f;
            IBlackFlowMonster.dealMagicDamage(enemy, this, atk);
        } else super.doHurtTarget(enemy);
        if (this.level() instanceof ServerLevel serverLevel){
            serverLevel.sendParticles(MBFParticleTypes.BOG_BITE.get(),
                    enemy.getX(), enemy.getY() + 1.5, enemy.getZ(), 1,
                    0.125, 0.125, 0.125, 0);
        }
        if (enemy instanceof LivingEntity living){
            ManiaInjury.dealManiaInjury(living, atk * 30, ManiaInjurySource.fromEntity(this));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag){
        super.readAdditionalSaveData(tag);
        if (tag.contains("cooldown")) skillCooldown = tag.getInt("cooldown");
        if (tag.contains("skillTick")) this.entityData.set(SKILL_TICK, tag.getInt("skillTick"));
        if (tag.contains("targetUUID")) attackedEntityUUID = tag.getUUID("targetUUID");
        if (tag.contains("prev_x")){
            previousPos = new Vec3(tag.getDouble("prev_x"), tag.getDouble("prev_y"), tag.getDouble("prev_z"));
        }
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putInt("cooldown", skillCooldown);
        tag.putInt("skillTick", this.entityData.get(SKILL_TICK));
        if (attachedEntity != null) tag.putUUID("targetUUID", attachedEntity.getUUID());
        tag.putDouble("prev_x", previousPos.x);
        tag.putDouble("prev_y", previousPos.y);
        tag.putDouble("prev_z", previousPos.z);
    }

    @Override
    public void registerGoals(){
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new ComplexMeleeAttackGoal(this, 1, false) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 9;
            }
            @Override
            public boolean canUse(){
                return super.canUse() && !HuntingDogProtoEntity.this.inSkill();
            }
            @Override
            public boolean canContinueToUse(){
                return super.canContinueToUse() && !HuntingDogProtoEntity.this.inSkill();
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new AttackEveryoneGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    private boolean inSkillPrePart(){
        int t = getCommonSkillTick();
        return inSkill() && 0 <= t && t <= 3;
    }
    private boolean inSkillPostPart(){
        int t = getCommonSkillTick();
        return inSkill() && 203 <= t && t <= 211;
    }
    private boolean inSkillMainPart(){
        int t = getCommonSkillTick();
        return 3 <= t && t <= 203;
    }

    public PlayState moveHandler(AnimationState<?> event){
        if (this.isDeadOrDying()){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("die")));
        }
        if (inSkillPrePart()) return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("skill_begin")));
        if (inSkillPostPart()) return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("skill_end")));
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
        if (this.swinging && this.lastSwing + 28L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (getAttackDuration() > 0 && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return this.getRandom().nextFloat() < 0.5f ?
                    event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("attack")))
                    : event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("attack_1")));
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(72, 4, 0.4, 3, 1, 0.9, 29);
    }
}
