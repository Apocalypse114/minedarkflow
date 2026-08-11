package net.apocalypse.mineblackflow.entity.base;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;

public abstract class RangedBlackFlowMonster extends GeoBlackFlowMonster implements RangedAttackMob {
    public RangedBlackFlowMonster(EntityType<? extends Monster> type, Level level, String resId) {
        super(type, level, resId);
    }

    public static final EntityDataAccessor<Integer> SHOOT_TICK = SynchedEntityData.defineId(RangedBlackFlowMonster.class, EntityDataSerializers.INT);

    @Override
    public void defineSynchedData(){
        super.defineSynchedData();
        this.entityData.define(SHOOT_TICK, 0);
    }
    @Override
    public void tick(){
        super.tick();
        int t = entityData.get(SHOOT_TICK);
        if (t > 0) entityData.set(SHOOT_TICK, t - 1);
    }

    public void startShootAnim(int len){
        entityData.set(SHOOT_TICK, len);
    }
    public boolean shouldPlayShootAnim(int duration){
        return entityData.get(SHOOT_TICK) > 0;
    }

}
