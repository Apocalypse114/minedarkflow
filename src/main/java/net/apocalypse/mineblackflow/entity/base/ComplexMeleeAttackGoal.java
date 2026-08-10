package net.apocalypse.mineblackflow.entity.base;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class ComplexMeleeAttackGoal extends MeleeAttackGoal {
    private final GeoBlackFlowMonster monster;

    public ComplexMeleeAttackGoal(
            GeoBlackFlowMonster mob, double speed, boolean followIfNotSeen) {
        super(mob, speed, followIfNotSeen);
        this.monster = mob;
    }

    public boolean canUse(){
        return super.canUse() && monster.getAttackDuration() <= 0;
    }
    public boolean canContinueToUse(){
        return super.canContinueToUse() && monster.getAttackDuration() <= 0;
    }
}
