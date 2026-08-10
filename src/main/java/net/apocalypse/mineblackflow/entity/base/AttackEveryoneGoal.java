package net.apocalypse.mineblackflow.entity.base;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class AttackEveryoneGoal extends NearestAttackableTargetGoal<PathfinderMob> {
    public AttackEveryoneGoal(Mob mob) {
        super(mob, PathfinderMob.class, true, m -> !(m instanceof IBlackFlowMonster));
    }
}
