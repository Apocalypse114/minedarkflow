package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.block.BlackflowPropaguleBlock;
import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
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
        genBuildingBlockSet(MBFBlocks.BLACKFLOW_STONE_SET);
        dropSelf(MBFBlocks.BLACKFLOW_LOG.get());
        dropSelf(MBFBlocks.BLACKFLOW_AERIAL_ROOT.get());
        //DONE UPPER

        add(MBFBlocks.BLACKFLOW_LEAVE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().when(HAS_SILK_TOUCH).add(LootItem.lootTableItem(MBFItems.BLACKFLOW_LEAVE.get())))
                .withPool(LootPool.lootPool().when(HAS_NO_SILK_TOUCH)
                        .add(LootItem.lootTableItem(Items.STICK).setWeight(25))
                        .add(LootItem.lootTableItem(MBFItems.BLACKFLOW_PROPAGULE.get()).setWeight(25))
                        .add(LootItem.lootTableItem(Items.AIR).setWeight(50))
                        .setRolls(UniformGenerator.between(1,3)))
        );
        add(MBFBlocks.BLACKFLOW_PROPAGULE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .when(intCondition(3, BlackflowPropaguleBlock.AGE, MBFBlocks.BLACKFLOW_PROPAGULE.get()))
                        .add(LootItem.lootTableItem(MBFItems.BUCKET_APOCATA.get()))));
    }

    private LootItemCondition.Builder intCondition(int count, IntegerProperty property, Block block){
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(
                        StatePropertiesPredicate.Builder.properties()
                                .hasProperty(property, count)
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
