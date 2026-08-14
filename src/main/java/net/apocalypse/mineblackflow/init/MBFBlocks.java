package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.block.BlackflowiumBlock;
import net.apocalypse.mineblackflow.block.BlackflowiumClusterBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MBFBlocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(Registries.BLOCK, MineBlackFlow.MODID);

    public static final RegistryObject<Block> BLACKFLOWIUM_CLUSTER = REGISTRY.register("blackflowium_cluster", BlackflowiumClusterBlock::new);
    public static final RegistryObject<Block> BLACKFLOWIUM_BLOCK = REGISTRY.register("blackflowium_block", BlackflowiumBlock::new);
}
