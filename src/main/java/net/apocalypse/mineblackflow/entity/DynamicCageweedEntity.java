package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.entity.base.AttackEveryoneGoal;
import net.apocalypse.mineblackflow.entity.base.ComplexMeleeAttackGoal;
import net.apocalypse.mineblackflow.entity.base.IBlackFlowMonster;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.UUID;

public class DynamicCageweedEntity extends AbstractCageweedEntity {

    public DynamicCageweedEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "chest_cageweed", 4);
    }
    public DynamicCageweedEntity(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.DYNAMIC_CAGEWEED.get(), world);
    }

    public static final EntityDataAccessor<Boolean> OPENED = SynchedEntityData.defineId(DynamicCageweedEntity.class, EntityDataSerializers.BOOLEAN);
    private int tickInSkill = 0, cooldown = 200;

    private static final UUID very_slow_uuid = new UUID(0xBA6242DDFB8853FDL, 0xEE8A680CD91072EFL);
    private static final AttributeModifier very_slow = new AttributeModifier(very_slow_uuid,"very_slow", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);

    public boolean opened(){return entityData.get(OPENED);}
    public void setOpened(boolean op){entityData.set(OPENED, op);}

    @Override
    public void tick(){
        super.tick();
        AttributeInstance moveSpeed = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (opened()) {
            if (moveSpeed != null && moveSpeed.hasModifier(very_slow)) moveSpeed.removeModifier(very_slow);
            if (tickInSkill > 0) {
                int tickSinceSkillStart = 45 - tickInSkill;
                if (tickSinceSkillStart == 19) doSkillAttack(3);
                if (tickSinceSkillStart == 34) doSkillAttack(2);
                tickInSkill--;
            } else {
                if (cooldown > 0) cooldown--;
                else if (checkSkillRelease()) doOnSkillRelease();
            }
        } else {
            if (moveSpeed != null && !moveSpeed.hasModifier(very_slow)) moveSpeed.addPermanentModifier(very_slow);
        }
    }

    @Override
    public void awardKillScore(@NotNull Entity pKilled, int pScoreValue, @NotNull DamageSource pSource){
        super.awardKillScore(pKilled, pScoreValue, pSource);
        if (pKilled instanceof Player player){
            Inventory inventory = player.getInventory();
            int left = 4;
            for(int i = 0; i<inventory.getContainerSize(); i++){
                ItemStack stack = inventory.getItem(i);
                if(stack.is(MBFItems.BLACKFLOWIUM_INGOT.get())){
                    if (stack.getCount() >= left){
                        stack.shrink(left);
                        return;
                    } else {
                        left -= stack.getCount();
                        stack.setCount(0);
                    }
                }
                if (left <= 0) break;
            }
        }

    }

    @Override
    protected void actuallyHurt(@NotNull DamageSource source, float amount){
        if (!opened()){
            setOpened(true);
            this.triggerAnim("anim", "start");
            amount *= 0.01f;
        }
        super.actuallyHurt(source, amount);
    }

    public void doSkillAttack(double range){
        Vec3 forward = new Vec3(this.getLookAngle().x, 0, this.getLookAngle().z).normalize().scale(range);
        float amt = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        double rangeSqr = (range+1) * (range+1);
        List<LivingEntity> livingEntities = this.level().getEntitiesOfClass(LivingEntity.class,
                new AABB(this.position().add(forward), this.position().add(forward)).inflate(range + 1),
                living -> IBlackFlowMonster.isValidTarget(this, living));
        livingEntities.forEach(living -> {
            if (this.distanceToSqr(living) <= rangeSqr){
                IBlackFlowMonster.dealCommonDamage(living, this, amt);
                Vec3 toMe = living.position().vectorTo(this.position()).normalize().scale(0.35);
                living.push(toMe.x, toMe.y, toMe.z);
            }
        });
    }

    public boolean checkSkillRelease(){
        LivingEntity target = this.getTarget();
        return target != null && target.isAlive() && this.distanceToSqr(target) <= 16;
    }

    private void doOnSkillRelease(){
        setAttackDuration(20);
        tickInSkill = 45;
        cooldown = 400;
        this.triggerAnim("anim", "attack2");
    }

    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(OPENED, false);
    }

    @Override
    public void registerGoals(){
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new ComplexMeleeAttackGoal(this, 1, false) {
            @Override
            public boolean canUse(){return super.canUse() && opened();}
            @Override
            public boolean canContinueToUse(){return super.canContinueToUse() && opened();}
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 4;
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true){
            @Override
            public boolean canUse(){return super.canUse() && opened();}
            @Override
            public boolean canContinueToUse(){return super.canContinueToUse() && opened();}
        });
        this.targetSelector.addGoal(4, new AttackEveryoneGoal(this){
            @Override
            public boolean canUse(){return super.canUse() && opened();}
            @Override
            public boolean canContinueToUse(){return super.canContinueToUse() && opened();}
        });
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1){
            @Override
            public boolean canUse(){return super.canUse() && opened();}
            @Override
            public boolean canContinueToUse(){return super.canContinueToUse() && opened();}
        });
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this){
            @Override
            public boolean canUse(){return super.canUse() && opened();}
            @Override
            public boolean canContinueToUse(){return super.canContinueToUse() && opened();}
        });
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pTarget){
        setAttackDuration(20);
        MineBlackFlow.queueServerWork(13, ()->{
            pTarget.invulnerableTime = 0;
            super.doHurtTarget(pTarget);
        });
        return true;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(moveController(this, 1, this::moveHandler));
        controllers.add(attackController(this, 0, this::attackingHandler));
        controllers.add(triggerableAnimController(this, "anim", 0, "start", "attack2"));
    }

    public PlayState moveHandler(AnimationState<?> event){
        if (this.isDeadOrDying()){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("die")));
        }
        if (!opened()) return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("idle_chest")));
        //if (inSKill()){event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("skill2")));}
        if(event.isMoving()){
            return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("move")));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("idle")));
    }
    private PlayState attackingHandler(AnimationState<?> event) {
        if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
            this.swinging = true;
            this.lastSwing = level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 20L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (getAttackDuration() > 0 && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("attack1")));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag){
        super.readAdditionalSaveData(tag);
        if (tag.contains("opened")) this.setOpened(tag.getBoolean("opened"));
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.putBoolean("opened", opened());
    }

    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(60, 9, 0.2, 5, 3, 0.4, 24);
    }
}
