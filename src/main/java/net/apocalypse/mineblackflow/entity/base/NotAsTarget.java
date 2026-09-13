package net.apocalypse.mineblackflow.entity.base;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Set;

public interface NotAsTarget {
    Set<EntityType<?>> whiteList();
    default boolean inWhiteList(Entity entity){
        return whiteList().contains(entity.getType());
    }
}
