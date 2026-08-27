package net.apocalypse.mineblackflow.entity.base;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.stalk.StalkInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class AttackEveryoneGoal extends NearestAttackableTargetGoal<PathfinderMob> {
    public AttackEveryoneGoal(Mob mob) {
        super(mob, PathfinderMob.class, true, m -> canAttack(mob, m));
    }

    public static boolean canAttack(Mob mob, LivingEntity living){
        return !(living instanceof IBlackFlowMonster
                || MBFUtil.isSameTeam(mob, living, false)
                || StalkInstance.isInStalk(living));

    }
}
