package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MBFTabs {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MineBlackFlow.MODID);

    public static final RegistryObject<CreativeModeTab> MBF_ITEMS = REGISTRY.register("mbf_items", MBFItems::mbfItemsTab);


}
