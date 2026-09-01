package net.apocalypse.mineblackflow.block;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlackflowLogBlock extends RotatedPillarBlock {
    public BlackflowLogBlock(){
        super(Properties.of()
                .mapColor(MapColor.TERRACOTTA_GREEN)
                .instrument(NoteBlockInstrument.BASS)
                .strength(2.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava());
    }
}
