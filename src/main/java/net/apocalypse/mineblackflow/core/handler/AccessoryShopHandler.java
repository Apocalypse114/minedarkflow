package net.apocalypse.mineblackflow.core.handler;

import net.apocalypse.mineblackflow.init.MBFItems;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.apocalypse.mineblackflow.item.base.IBlackflowiumPriced;
import net.apocalypse.mineblackflow.item.natural.SeedItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccessoryShopHandler extends ItemStackHandler {
    public AccessoryShopHandler(){super(24);}

    private int unlockedGoodSlot = 10;
    private boolean firstlyOpen = true;
    private final boolean[] indexToPurchase = genIndex();
    private static final List<RegistryObject<Item>> itemsForSale = new ArrayList<>();
    private static boolean isItemsForSaleUnchecked = true;

    public int robberyCount = 0, refreshPrice = 4, refreshRepriceCooldown = 200;

    public int getUnlockedGoodSlot(){return unlockedGoodSlot;}
    public void setUnlockedGoodSlot(int t){unlockedGoodSlot = Mth.clamp(t, 10, 23);}
    public int calcSelectedPrice(){
        int p = 0;
        for (int i=1; i<24;i++){
            if (indexToPurchase[i]){
                ItemStack stack = getStackInSlot(i);
                if (stack.getItem() instanceof IBlackflowiumPriced priced) p += priced.getCost();
            }
        }
        return p;
    }

    public boolean isAnythingSelected(){
        for (int i=1; i<24;i++){
            if (indexToPurchase[i]) return true;
        }
        return false;
    }
    public boolean isIndexSelected(int index){
        return indexToPurchase[index];
    }
    public void putStackToBuy(int index){
        if (index <= 0 || index > getUnlockedGoodSlot()) return;
        if (getStackInSlot(index).isEmpty()) return;
        indexToPurchase[index] = !indexToPurchase[index];
    }
    public void confirmStackToBuy(Player player){
        boolean onceSold = false;
        for (int i = 1; i<24; i++){
            if (indexToPurchase[i] && purchase(i, player)) onceSold = true;
        }
        if(onceSold){clearToBuy();}
    }
    public void clearToBuy(){
        for (int i = 1; i<24; i++){
            indexToPurchase[i] = false;
        }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack){
        if (slot == 0) return stack.is(MBFItems.SEEDS.get());
        return stack.getItem() instanceof AccessoryBase;
    }

    @Override
    public CompoundTag serializeNBT(){
        CompoundTag tag = super.serializeNBT();
        tag.putInt("unlocked", unlockedGoodSlot);
        tag.putBoolean("firstlyOpen", firstlyOpen);
        return tag;
    }
    @Override
    public void deserializeNBT(CompoundTag tag){
        super.deserializeNBT(tag);
        this.firstlyOpen = tag.getBoolean("firstlyOpen");
        this.setUnlockedGoodSlot(tag.getInt("unlocked"));
    }

    public void breedSeeds(Player player){
        ItemStack stack = getStackInSlot(0);
        if (stack.isEmpty()) return;
        SeedItem.breed(stack, player);
    }

    public void onOpen(Player player){
        if (firstlyOpen){
            firstlyOpen = false;
            refreshGoods(player.getRandom(), 1, unlockedGoodSlot);
        }
    }

    public void playerRefresh(Player player){
        if (player == null) return;;
        if (refreshPrice >= 16) return;
        if (tryPay(player.getInventory(), refreshPrice)) {
            refreshGoods(player.getRandom(), 1, 23);
            refreshPrice += 2;
            refreshRepriceCooldown = 12000;
        }
    }

    public void refreshGoods(RandomSource source, int fromIndex, int toIndex){
        toIndex = Mth.clamp(toIndex, 1, unlockedGoodSlot);
        fromIndex = Mth.clamp(fromIndex, 1, toIndex);
        for (int i=fromIndex; i<=toIndex; i++){
            this.setStackInSlot(i, randomStack(source));
        }
    }

    public boolean purchase(int index, Player player){
        if (index <= 0 || index > unlockedGoodSlot) return false;
        ItemStack good = getStackInSlot(index);
        if (good.getItem() instanceof AccessoryBase accessory){
            int cost = accessory.getCost();
            Inventory playerInventory = player.getInventory();
            if (tryPay(playerInventory, cost)){
                accessory.giveTo(player);
                good.setCount(0);
                return true;
            }
        }
        return false;
    }

    public void tickShop(){
        if (refreshRepriceCooldown <= 0) refreshPrice = 4;
        else refreshRepriceCooldown --;
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
        if (isItemsForSaleUnchecked){
            itemsForSale.clear();
            isItemsForSaleUnchecked = false;
            for (RegistryObject<Item> item: MBFItems.ACCESSORY.getEntries()){
                if (item.get() instanceof IBlackflowiumPriced priced && priced.canExistInTrade())itemsForSale.add(item);
            }
        }
        return itemsForSale.get(Mth.nextInt(source, 0, itemsForSale.size() - 1));
    }

    @Contract(pure = true)
    private static boolean @NotNull [] genIndex() {
        boolean[] val = new boolean[24];
        for(int i = 0; i<24;i++) val[i] = false;
        return val;
    }
}
