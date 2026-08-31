package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.block.*;
import net.apocalypse.mineblackflow.block.natural.BloodMushroomBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MBFBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, MineBlackFlow.MODID);

    public static final RegistryObject<Block> BLACKFLOWIUM_CLUSTER = REGISTRY.register("blackflowium_cluster", BlackflowiumClusterBlock::new);
    public static final RegistryObject<Block> BLACKFLOWIUM_BLOCK = REGISTRY.register("blackflowium_block", BlackflowiumBlock::new);
    public static final RegistryObject<Block> BLOOD_MUSHROOM_BLOCK = REGISTRY.register("blood_mushroom_plant", BloodMushroomBlock::new);
    public static final RegistryObject<Block> BLACKFLOW_DIRT = REGISTRY.register("blackflow_dirt",
            ()->new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(0.5F).sound(SoundType.GRAVEL)));
    public static final RegistryObject<Block> BLACKFLOW_GRASS_BLOCK = REGISTRY.register("blackflow_grass_block", BlackflowGrassBlock::new);
    public static final RegistryObject<Block> BLACKFLOW_STONE = REGISTRY.register("blackflow_stone", BlackflowStoneBlock::new);
    public static final NonCubeBuildingBlockSet BLACKFLOW_STONE_SET = NonCubeBuildingBlockSet.create("blackflow_stone", BLACKFLOW_STONE,
            BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 7.5F));
    public static final RegistryObject<Block> BLACKFLOW_GRASS = REGISTRY.register("blackflow_grass", BlackflowGrassPlant::new);
    public static final RegistryObject<Block> RED_SETARIA = REGISTRY.register("red_setaria", RedSetariaPlant::new);

    public record NonCubeBuildingBlockSet(RegistryObject<StairBlock> stair, RegistryObject<SlabBlock> slab, RegistryObject<WallBlock> wall){
        public static NonCubeBuildingBlockSet create(String name, RegistryObject<Block> base, BlockBehaviour.Properties properties){
            RegistryObject<StairBlock> stair = REGISTRY.register(name+"_stairs",
                    ()->new StairBlock(()->base.get().defaultBlockState(), properties));
            RegistryObject<SlabBlock> slab = REGISTRY.register(name+"_slab", ()->new SlabBlock(properties));
            RegistryObject<WallBlock> wall = REGISTRY.register(name+"_wall", ()->new WallBlock(properties.forceSolidOn()));
            return new NonCubeBuildingBlockSet(stair, slab, wall);
        }
    }
}
