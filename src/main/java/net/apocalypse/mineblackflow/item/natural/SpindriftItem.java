package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SpindriftItem extends AccessoryBase {
    public SpindriftItem() {
        super(0, "spindrift", FuncCase.INVENTORY);
    }

    public int getBaseEvaluation(){return 3;}

    public int getCost() {return 6;}

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvance) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvance);
    }

    private static final int[] nums = new int[]{-7, 9, 10, 11},
            scales = new int[]{0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4};
    private static final String TAG_LAST_MOVE = "last_move";

    private static double getLastMove(ItemStack stack){
        return stack.getOrCreateTag().getDouble(TAG_LAST_MOVE);
    }
    private static void setLastMove(ItemStack stack, double len){
        stack.getOrCreateTag().putDouble(TAG_LAST_MOVE, len);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected){
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity.tickCount % 10 == 0) {
            if (pEntity.moveDist >= getLastMove(pStack) + 4){
                boostEvaluation(pStack, nextChange(pLevel));
                setLastMove(pStack, pEntity.moveDist);
            }
            if (pEntity.moveDist < getLastMove(pStack)){
                setLastMove(pStack, pEntity.moveDist);
            }
        }
    }

    private static int nextChange(Level pLevel){
        if (pLevel.getRandom().nextFloat() < 0.0064f){
            return nums[Mth.nextInt(pLevel.getRandom(), 0, 3)];
        }
        return Mth.nextInt(pLevel.getRandom(), -6, 8);
    }
}
