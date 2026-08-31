package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.block.RedSetariaPlant;
import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider {
    public ModBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(MBFBlocks.BLACKFLOWIUM_CLUSTER.get());
        dropSelf(MBFBlocks.BLACKFLOWIUM_BLOCK.get());
        dropSelf(MBFBlocks.BLOOD_MUSHROOM_BLOCK.get());
        dropSelf(MBFBlocks.BLACKFLOW_DIRT.get());
        dropSelf(MBFBlocks.BLACKFLOW_STONE.get());
        add(MBFBlocks.BLACKFLOW_GRASS_BLOCK.get(), LootTable.lootTable());
        add(MBFBlocks.BLACKFLOW_GRASS.get(), LootTable.lootTable());
        add(MBFBlocks.RED_SETARIA.get(), LootTable.lootTable());
        //DONE UPPER

        genBuildingBlockSet(MBFBlocks.BLACKFLOW_STONE_SET);
    }

    private LootItemCondition.Builder redSetariaCondition(int count){
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(MBFBlocks.RED_SETARIA.get())
                .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                                .hasProperty(RedSetariaPlant.COUNT, count)
                );
    }
    private void genBuildingBlockSet(MBFBlocks.NonCubeBuildingBlockSet set){
        dropSelf(set.stair().get());
        dropSelf(set.wall().get());
        dropSelf(set.slab().get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return MBFBlocks.REGISTRY.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
