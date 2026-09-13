package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.entity.base.IBlackFlowMonster;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class AberrantCageweedEntity extends MotionlessCageweedEntity{
    public AberrantCageweedEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, true);
    }
    public AberrantCageweedEntity(PlayMessages.SpawnEntity ignored, Level world) {
       this(MBFEntities.ABERRANT_CAGEWEED.get(), world);
    }

    @Override
    public void actuallyHurt(@NotNull DamageSource pSource, float amount){
        float h0 = this.getHealth();
        super.actuallyHurt(pSource, amount);
        float h1 = Math.max(0, this.getHealth()), reduction = h0 - h1;
        if (pSource.getEntity() instanceof LivingEntity living){
            IBlackFlowMonster.dealRealDamage(living, reduction * 0.4f);
        }
    }
}
