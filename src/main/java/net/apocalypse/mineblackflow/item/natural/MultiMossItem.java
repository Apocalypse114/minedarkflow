package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.init.MBFItems;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class MultiMossItem extends AccessoryBase {
    public MultiMossItem() {
        super(1, "multi_moss", FuncCase.INVENTORY);
    }

    public int getBaseEvaluation(){return 1;}

    @Override
    public void onGain(ItemStack accessoryStack, Player pOwner) {
        super.onGain(accessoryStack, pOwner);
        MBFItems.DEAD_MOSS_BALL.get().giveTo(pOwner, 3 * accessoryStack.getCount());
    }

    @Override
    public void onOuterGain(ItemStack outerStack, ItemStack currentStack, Player player){
        super.onOuterGain(outerStack, currentStack, player);
        boostEvaluation(currentStack, outerStack.getCount());
    }

    public int getCost() {return 12;}
}
