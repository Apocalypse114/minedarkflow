package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.gui.menu.AccessoryBoxMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MBFMenuType {
    public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MineBlackFlow.MODID);

    public static final RegistryObject<MenuType<AccessoryBoxMenu>> ACC_BOX = REGISTRY.register("accessory_box", ()-> IForgeMenuType.create(AccessoryBoxMenu::new));
}
