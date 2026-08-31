package net.apocalypse.mineblackflow.item;

import net.apocalypse.mineblackflow.block.RedSetariaPlant;
import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class RedSetariaBlockItem extends BlockItem {
    public RedSetariaBlockItem() {
        super(MBFBlocks.RED_SETARIA.get(), new Properties());
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        BlockPos pos = pContext.getClickedPos();
        Level level = pContext.getLevel();
        BlockState blockState = level.getBlockState(pos);
        if (blockState.is(MBFBlocks.RED_SETARIA.get())){
            int count = blockState.getValue(RedSetariaPlant.COUNT);
            if (count < 4){
                level.setBlockAndUpdate(pos, blockState.setValue(RedSetariaPlant.COUNT, count + 1));
                Player player = pContext.getPlayer();
                if (player != null) {
                    level.playSound(pContext.getPlayer(), pContext.getPlayer().blockPosition(), SoundEvents.GRASS_PLACE,
                            SoundSource.BLOCKS, 1, level.getRandom().nextFloat()*0.8f + 0.2f);
                    level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, blockState));
                    if (!player.getAbilities().instabuild) {
                        pContext.getItemInHand().shrink(1);
                    }
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.useOn(pContext);
    }
}
