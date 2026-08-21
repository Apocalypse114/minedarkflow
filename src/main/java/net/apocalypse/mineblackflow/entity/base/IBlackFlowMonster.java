package net.apocalypse.mineblackflow.entity.base;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.init.MBFDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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
    static boolean dealCommonDamage(Entity target, LivingEntity from, LivingEntity direct, float amount){
        return target.hurt(new DamageSource(MBFUtil.damageType(MBFDamageTypes.BLACKFLOW_COMMON, from.level()), from, direct), amount);
    }
    static boolean dealCommonDamage(Entity target, LivingEntity from, float amount){
        return target.hurt(new DamageSource(MBFUtil.damageType(MBFDamageTypes.BLACKFLOW_COMMON, from.level()), from), amount);
    }
    static boolean dealMagicDamage(Entity target, LivingEntity from, LivingEntity direct, float amount){
        return target.hurt(new DamageSource(MBFUtil.damageType(MBFDamageTypes.BLACKFLOW_MAGIC, from.level()), from, direct), amount);
    }
    static boolean dealMagicDamage(Entity target, LivingEntity from, float amount){
        return target.hurt(new DamageSource(MBFUtil.damageType(MBFDamageTypes.BLACKFLOW_MAGIC, from.level()), from), amount);
    }
}
