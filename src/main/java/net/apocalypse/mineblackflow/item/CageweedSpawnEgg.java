package net.apocalypse.mineblackflow.item;

import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.item.base.MultiStateSpawnEgg;
import net.minecraft.world.item.Rarity;

public class CageweedSpawnEgg extends MultiStateSpawnEgg {
    public CageweedSpawnEgg() {
        super(MBFEntities.DYNAMIC_CAGEWEED, 0x6d624d, 0x6f9f3a, Rarity.COMMON);
        addType(MBFEntities.DYNAMIC_CAGEWEED, "cageweed_spawn_egg.t0");
        addType(MBFEntities.MOTIONLESS_CAGEWEED, "cageweed_spawn_egg.t1");
        addType(MBFEntities.ABERRANT_CAGEWEED, "cageweed_spawn_egg.t2");
    }
}
