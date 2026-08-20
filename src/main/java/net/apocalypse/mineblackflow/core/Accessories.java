package net.apocalypse.mineblackflow.core;

import net.apocalypse.mineblackflow.init.MBFItems;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public enum Accessories implements IExtensibleEnum {
    SEEDS(MBFItems.SEEDS),
    MISTY_TUMBLEWEED(MBFItems.MISTY_TUMBLEWEED),
    BLOOD_MUSHROOM(MBFItems.BLOOD_MUSHROOM),
    SPINDRIFT(MBFItems.SPINDRIFT),
    FROST_TREE(MBFItems.FROST_TREE),
    ECHO_CORN(MBFItems.ECHO_CORN),
    MULTI_MOSS(MBFItems.MULTI_MOSS),
    DEAD_MOSS_BALL(MBFItems.DEAD_MOSS_BALL),
    HOMESICK_FRUIT(MBFItems.HOMESICK_FRUIT),
    PLATE_VINE(MBFItems.PLATE_VINE),
    SHINY_TRUFFLE(MBFItems.SHINY_TRUFFLE),
    CAGE_CONTROLLER(MBFItems.CAGE_CONTROLLER)
    ;

    private final RegistryObject<AccessoryBase> accessory;
    Accessories(RegistryObject<AccessoryBase> accessoryBase){
        this.accessory = accessoryBase;
    }
    public AccessoryBase getAccessory(){
        return accessory.get();
    }

    public List<ItemStack> giveTo(Player pPlayer, boolean displayMessage){
        return displayMessage ? getAccessory().giveWithMessage(pPlayer): getAccessory().giveTo(pPlayer);
    }
    public List<ItemStack> giveTo(Player pPlayer){
        return getAccessory().giveTo(pPlayer);
    }
    public static Accessories create(String name, RegistryObject<AccessoryBase> registryObject)
    {
        throw new IllegalStateException("Enum not extended");
    }
}
