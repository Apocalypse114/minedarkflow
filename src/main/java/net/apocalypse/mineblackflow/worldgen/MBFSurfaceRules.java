package net.apocalypse.mineblackflow.worldgen;

import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.apocalypse.mineblackflow.worldgen.biome.MBFBiome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class MBFSurfaceRules {
    private static final SurfaceRules.RuleSource MBF_DIRT = defaultStateRule(MBFBlocks.BLACKFLOW_DIRT.get());
    private static final SurfaceRules.RuleSource MBF_GRASS_BLOCK = defaultStateRule(MBFBlocks.BLACKFLOW_GRASS_BLOCK.get());

    private static final SurfaceRules.RuleSource myGrass = SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, MBF_GRASS_BLOCK);
    private static final SurfaceRules.RuleSource myDirt = SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, MBF_DIRT);

    public static SurfaceRules.RuleSource shallowBlackflowRules() {
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.isBiome(MBFBiome.SHALLOW_BLACKFLOW),
                        SurfaceRules.sequence(myGrass, myDirt))
        );
    }
    private static SurfaceRules.RuleSource defaultStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
