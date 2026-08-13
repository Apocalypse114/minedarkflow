package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.entity.base.AttackEveryoneGoal;
import net.apocalypse.mineblackflow.entity.base.ComplexMeleeAttackGoal;
import net.apocalypse.mineblackflow.entity.base.GeoBlackFlowMonster;
import net.apocalypse.mineblackflow.entity.base.IBlackFlowMonster;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ForsakenEarthshakerEntity extends GeoBlackFlowMonster {

    public ForsakenEarthshakerEntity(EntityType<?> ignored, Level level) {
        super(MBFEntities.FORSAKEN_EARTHSHAKER.get(), level, "forsaken_earthshaker");
        this.xpReward = 16;
        setMaxUpStep(1.5f);
    }
    public ForsakenEarthshakerEntity(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.FORSAKEN_EARTHSHAKER.get(), world);
    }

    private final List<LivingEntity> smashingEntityList = new ArrayList<>();
    private int speedModifierInAction = 0;
    private static final UUID uuid = UUID.fromString("6ab35f7e-e926-3999-b7db-e550d68b0add");

    @Override
    public void tick() {
        super.tick();
        if(this.tickCount % 5 == 0 && this.level() instanceof ServerLevel level) {
            elephantCCB(level);
        }
        if (this.tickCount % 20 == 0) destroyBlocks();
        float p = getBleedPercentage();
        if (!this.isDeadOrDying() && p > 0) {
            float nextHealth = this.getHealth() - this.getMaxHealth() * p;
            if (nextHealth > 0) this.setHealth(nextHealth);
        }
        int b = getBleeding() / 10;
        AttributeInstance instance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (b > 0 && instance != null){
            if (speedModifierInAction != b){
                speedModifierInAction = b;
                instance.removeModifier(uuid);
                instance.addTransientModifier(new AttributeModifier(uuid, "elephant_speed", 0.1 * b, AttributeModifier.Operation.MULTIPLY_BASE));
            }
        }
    }
    private void elephantCCB(ServerLevel level){
        if (this.getDeltaMovement().lengthSqr() > 1e-6) {
            float amount = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.5f;
            List<LivingEntity> entityInBound = level.getEntitiesOfClass(LivingEntity.class,
                    this.getBoundingBox().inflate(1, 0, 1), e -> isValidTarget(e) && e.getY() < this.getY() + 2);
            for (LivingEntity living : entityInBound) {
                if (!smashingEntityList.contains(living)) {
                    smashingEntityList.add(living);
                    living.hurt(this.level().damageSources().mobAttack(this), amount);
                }
            }
            smashingEntityList.removeIf(entity -> !entityInBound.contains(entity) || !entity.isAlive());
            if(this.getAttackDuration() > 30) {
                List<LivingEntity> entityInRange = level.getEntitiesOfClass(LivingEntity.class,
                        this.getBoundingBox().inflate(8, 0, 8), this::isValidTarget);
                entityInRange.forEach(e -> {
                    double d = this.distanceToSqr(e);
                    if (!e.isDeadOrDying() && d > 4 && d < 100) {
                        e.setDeltaMovement(e.position().vectorTo(this.position()).normalize().scale(0.5));
                    }
                });
            }
        }
    }
    private void destroyBlocks(){
        if (!this.level().isClientSide() && this.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            Iterable<BlockPos> pos = BlockPos.betweenClosed(this.blockPosition().offset(-2, 0, -2),
                    this.blockPosition().offset(2, 4, 2));
            for(BlockPos p: pos){
                BlockState state = this.level().getBlockState(p);
                float hardness = state.getDestroySpeed(this.level(), p);
                if (hardness>0 && hardness<5 && !state.getCollisionShape(this.level(), p).isEmpty()){
                    Block.dropResources(state, this.level(), p);
                    this.level().destroyBlock(p, false);
                }
            }
        }
    }
    @Override
    public void tickDeath(){
        deathTime++;
        if (deathTime == 46){
            MBFUtil.playDifferedSoundAtEntity(this, SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE,
                    1, 0.4f);
        }
        if (deathTime >= 50){
            this.remove(RemovalReason.KILLED);
            this.dropExperience();
        }
    }
    @Override
    public SoundEvent getAmbientSound(){
        return MBFSounds.ELEPHANT_AMBIENT.get();
    }
    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source){
        return MBFSounds.ELEPHANT_HURT.get();
    }
    @Override
    public @NotNull SoundEvent getDeathSound(){
        return MBFSounds.ELEPHANT_DIE.get();
    }

    private static final EntityDataAccessor<Integer> DATA_BLEEDING = SynchedEntityData.defineId(ForsakenEarthshakerEntity.class, EntityDataSerializers.INT);
    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(DATA_BLEEDING, 0);
    }
    public int getBleeding(){
        return this.entityData.get(DATA_BLEEDING);
    }
    public void setBleeding(int bleed){
        this.entityData.set(DATA_BLEEDING, bleed);
    }
    @Override
    public void actuallyHurt(@NotNull DamageSource source, float amount){
        int b = getBleeding(), l = b / 10;
        if (!(source.is(DamageTypeTags.BYPASSES_EFFECTS)
                ||source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)
                ||source.is(DamageTypeTags.BYPASSES_RESISTANCE)))
            amount *= 1 - l * 0.09f;
        super.actuallyHurt(source, amount);
        if (!this.isDeadOrDying() && amount > 0){
            if (b < 100) setBleeding(b+1);
        }
    }
    private static final float BASE_PERC_PER_TICK = 0.000125f;
    private float getBleedPercentage(){
        int l = getBleeding() / 10;
        return l * BASE_PERC_PER_TICK;
    }

    @Override
    public int getHeadRotSpeed() {
        return 3;
    }

    public void registerGoals(){
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new ComplexMeleeAttackGoal(this, 1.5, false) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 16;
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(4, new AttackEveryoneGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }
    @Override
    public boolean doHurtTarget(@NotNull Entity pTarget){
        this.setAttackDuration(95);
        MineBlackFlow.queueServerWork(65, ()-> attackTarget(pTarget));
        return true;
    }
    private void attackTarget(Entity pEntity){
        if(!this.isDeadOrDying() && pEntity instanceof LivingEntity living && this.distanceToSqr(pEntity) <= 32.5){
            final float amount = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * getBleeding() / 10;
            living.hurt(this.level().damageSources().mobAttack(this), amount);
            living.push(0, 1, 0);
            if (this.level() instanceof ServerLevel serverLevel) {
                List<LivingEntity> entityInBound = serverLevel.getEntitiesOfClass(LivingEntity.class,
                        MBFUtil.aabbOnEntity(pEntity, 3), e-> isValidTarget(e) && e != pEntity);
                entityInBound.forEach(e->{
                    if (this.distanceToSqr(e) <= 6.25){
                        e.hurt(this.level().damageSources().mobAttack(this), amount);
                        e.push(0, 0.75, 0);
                    }
                });
            }
        }
    }
    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(moveController(this, 1, this::moveHandler));
        controllers.add(attackController(this, 0, this::attackingHandler));
    }

    public PlayState moveHandler(AnimationState<?> event){
        if (this.isDeadOrDying()){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("die")));
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
        if (this.swinging && this.lastSwing + 94L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (getAttackDuration() > 0 && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("attack")));
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(480, 20, 0.15, 20, 20, 1, 24);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag){
        super.readAdditionalSaveData(tag);
        if (tag.contains("bleed")) this.setBleeding(tag.getInt("bleed"));
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putInt("bleed", getBleeding());
    }
    @Override
    public void doPush(@NotNull Entity entity){
        if (entity instanceof IBlackFlowMonster) super.doPush(entity);
    }
    @Override
    public void push(@NotNull Entity entity){
        if (entity instanceof IBlackFlowMonster) super.push(entity);
    }
}
