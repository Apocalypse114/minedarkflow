package net.apocalypse.mineblackflow.entity;

import net.apocalypse.mineblackflow.entity.base.GeoBlackFlowMonster;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

public abstract class AbstractCageweedEntity extends GeoBlackFlowMonster {
    public AbstractCageweedEntity(EntityType<? extends Monster> type, Level level, String resId, int ingotDrop) {
        super(type, level, resId);
        this.ingotDrop = ingotDrop;
    }

    private final int ingotDrop;

    @Override
    public boolean removeWhenFarAway(double d){
        return false;
    }

    @Override
    public void tickDeath(){
        super.tickDeath();
        if (deathTime == 8 && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)){
            this.spawnAtLocation(new ItemStack(MBFItems.BLACKFLOWIUM_INGOT.get(), ingotDrop), 1);
        }
    }
}
