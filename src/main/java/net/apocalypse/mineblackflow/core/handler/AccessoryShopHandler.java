package net.apocalypse.mineblackflow.core.handler;

import net.apocalypse.mineblackflow.init.MBFItems;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AccessoryShopHandler extends ItemStackHandler {
    public AccessoryShopHandler(){super(18);}

    public void refreshGoods(RandomSource source, int fromIndex, int toIndex){
        toIndex = Mth.clamp(toIndex, 0, 17);
        fromIndex = Mth.clamp(fromIndex, 0, toIndex);
        for (int i=fromIndex; i<=toIndex; i++){
            this.setStackInSlot(i, randomStack(source));
        }
    }

    public boolean purchase(int index, Player player){
        ItemStack good = getStackInSlot(index);
        if (good.getItem() instanceof AccessoryBase accessory){
            int cost = accessory.getCost();
            Inventory playerInventory = player.getInventory();
            if (tryPay(playerInventory, cost)){
                ItemHandlerHelper.giveItemToPlayer(player, good.copyAndClear());
                return true;
            }
        }
        return false;
    }

    private static boolean tryPay(Inventory inventory, int cost){
        if (cost <= 0) return false;
        Set<ItemStack> stacks = new HashSet<>();
        int leftIngots = 0;
        for (ItemStack stack: inventory.items){
            if (stack.is(MBFItems.BLACKFLOWIUM_INGOT.get())){
                stacks.add(stack);
                leftIngots += stack.getCount();
            }
        }
        if (leftIngots < cost) return false;
        for (ItemStack itemStack: stacks){
            if (itemStack.getCount() >= cost){
                itemStack.shrink(cost);
                return true;
            }
            cost -= itemStack.getCount();
            itemStack.setCount(0);
        }
        return true;
    }

    public ItemStack randomStack(RandomSource source){
        RegistryObject<Item> obj = randomAccessory(source);
        return obj==null ? ItemStack.EMPTY: new ItemStack(obj.get());
    }

    public RegistryObject<Item> randomAccessory(RandomSource source){
        var allItems = new ArrayList<>(MBFItems.ACCESSORY.getEntries());
        return allItems.get(Mth.nextInt(source, 0, allItems.size() - 1));
    }
}
