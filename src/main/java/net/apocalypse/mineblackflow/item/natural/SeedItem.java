package net.apocalypse.mineblackflow.item.natural;

import net.apocalypse.mineblackflow.init.MBFItems;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class SeedItem extends AccessoryBase {
    public SeedItem() {
        super(0, "seeds", FuncCase.EMPTY, true);
    }

    public int getBaseEvaluation(){return 2;}

    public int getCost() {return 4;}

    public static void breed(ItemStack seedStack, Player player){
        if (player == null) return;
        for (int i = 0; i<seedStack.getCount(); i++) {
            List<RegistryObject<Item>> accs = new ArrayList<>(MBFItems.ACCESSORY.getEntries());
            Item randomAcc = accs.get(Mth.nextInt(player.getRandom(), 0, accs.size() - 1)).get();
            if (randomAcc instanceof AccessoryBase base) {
                base.giveTo(player);
            }
        }
        seedStack.setCount(0);
    }
}
