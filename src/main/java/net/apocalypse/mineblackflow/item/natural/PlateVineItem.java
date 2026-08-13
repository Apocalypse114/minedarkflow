package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PlateVineItem extends AccessoryBase {
    public PlateVineItem() {
        super(2, "plate_vine", FuncCase.INVENTORY);
    }

    public int getBaseEvaluation(){return 8;}

    @Override
    public void onOuterGain(ItemStack outerStack, ItemStack currentStack, Player player){
        super.onOuterGain(outerStack, currentStack, player);
        boostEvaluation(currentStack, 4 * outerStack.getCount());
    }

    public int getCost() {return 0;}
}
