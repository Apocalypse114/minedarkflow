package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class ModEntityLootProvider extends EntityLootSubProvider {

    public ModEntityLootProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
    }

    @Override
    protected @NotNull Stream<EntityType<?>> getKnownEntityTypes() {
        return MBFEntities.REGISTRY.getEntries().stream().map(RegistryObject::get);
    }
}