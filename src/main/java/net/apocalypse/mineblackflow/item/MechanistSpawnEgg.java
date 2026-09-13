package net.apocalypse.mineblackflow.item;

import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.item.base.MultiStateSpawnEgg;
import net.minecraft.world.item.Rarity;

public class MechanistSpawnEgg extends MultiStateSpawnEgg {
    public MechanistSpawnEgg() {
        super(MBFEntities.MECHANIST_NPC, 0xffffff, 0xffffff, Rarity.UNCOMMON);
        addType(MBFEntities.MECHANIST_NPC, "mechanist_spawn_egg.t0");
    }
}
