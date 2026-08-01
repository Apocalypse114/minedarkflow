package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.item.NullMaskWandItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MBFItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, MineBlackFlow.MODID);

    public static final RegistryObject<Item> NULL_MASK_WAND = REGISTRY.register("null_mask_wand", NullMaskWandItem::new);

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData){

    }

    public static CreativeModeTab mbfItemsTab(){
        return CreativeModeTab.builder()
                .icon(()->new ItemStack(Items.OAK_SAPLING))
                .displayItems((para, tab)->{
                    tab.accept(NULL_MASK_WAND.get());
                })
                .build();
    }
}
