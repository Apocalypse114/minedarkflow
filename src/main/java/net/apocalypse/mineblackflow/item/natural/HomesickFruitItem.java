package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import java.util.List;

public class HomesickFruitItem extends AccessoryBase {
    public HomesickFruitItem() {
        super(2, "homesick_fruit", FuncCase.EMPTY);
    }

    public int getBaseEvaluation(){return 32;}

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvance) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvance);
        if (pStack.getOrCreateTag().getBoolean(TAG_INITED)) {
            Vector2i p = getInitPos(pStack);
            pTooltipComponents.add(Component.translatable("item.mine_black_flow.homesick_fruit.pos")
                    .append("x: "+p.x+", "+"z: "+p.y).withStyle(ChatFormatting.BLUE));
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected){
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        BlockPos pos = pEntity.blockPosition();
        Vector2i curPos = new Vector2i(pos.getX(), pos.getZ());
        double dist = curPos.distance(getInitPos(pStack));
        if (dist > 128) dist = 128;
        setEvaluation(pStack, (int) (-dist * 0.25f));
    }

    public int getCost() {return 0;}

    public static final String TAG_X = "pos_x", TAG_Z = "pos_z", TAG_INITED = "inited";

    @Override
    public void onGain(ItemStack accessoryStack, Player pOwner) {
        super.onGain(accessoryStack, pOwner);
        accessoryStack.getOrCreateTag().putBoolean(TAG_INITED, true);
        accessoryStack.getOrCreateTag().putInt(TAG_X, pOwner.blockPosition().getX());
        accessoryStack.getOrCreateTag().putInt(TAG_Z, pOwner.blockPosition().getZ());
    }

    public static Vector2i getInitPos(ItemStack pStack){
        return new Vector2i(pStack.getOrCreateTag().getInt(TAG_X), pStack.getOrCreateTag().getInt(TAG_Z));
    }
}
