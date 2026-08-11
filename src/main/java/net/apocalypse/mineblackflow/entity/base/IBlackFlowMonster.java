package net.apocalypse.mineblackflow.entity.base;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public interface IBlackFlowMonster {
    static boolean isValidTarget(@NotNull Mob blackFlowMonster, @NotNull LivingEntity pTarget){
        if (pTarget == blackFlowMonster) return false;
        if (MBFUtil.isSameTeam(blackFlowMonster, pTarget, false)) return false;
        if (pTarget instanceof Mob mob && mob instanceof IBlackFlowMonster)
            return blackFlowMonster.getTarget() == pTarget || mob.getTarget() == blackFlowMonster;
        return true;
    }
}
