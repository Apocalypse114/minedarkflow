package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;

public class MotionlessCageweedEntity extends AbstractCageweedEntity{
    public MotionlessCageweedEntity(EntityType<? extends Monster> type, Level level, boolean aberrant) {
        super(type, level, "cageweed", aberrant? 5: 2);
    }
    public MotionlessCageweedEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, "cageweed", 2);
    }
    public MotionlessCageweedEntity(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.MOTIONLESS_CAGEWEED.get(), world);
    }

    @Override
    public void push(Entity entity){}
    @Override
    public void doPush(Entity entity){}
    @Override
    public void push(double x, double y, double z){super.push(0, y, 0);}
    @Override
    public void setDeltaMovement(Vec3 vec3){super.setDeltaMovement(new Vec3(0, vec3.y, 0));}

    @SuppressWarnings("deprecation")
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel,
                                        @NotNull DifficultyInstance pDifficulty,
                                        @NotNull MobSpawnType pReason,
                                        @Nullable SpawnGroupData pSpawnData,
                                        @Nullable CompoundTag pDataTag) {
        SpawnGroupData data = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        this.setYBodyRot(90 * Mth.nextInt(pLevel.getRandom(), 0, 3));
        return data;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers){
        controllers.add(moveController(this, 1, this::moveHandler));
    }

    public PlayState moveHandler(AnimationState<?> event){
        if (this.isDeadOrDying()){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("die")));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("idle")));
    }

    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(30, 1, 0, 2, 0, 1, 24);
    }
}
