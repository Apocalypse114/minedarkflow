package net.apocalypse.mineblackflow.core;

import net.apocalypse.mineblackflow.init.MBFItems;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.registries.RegistryObject;

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
    public static Accessories create(String name, RegistryObject<AccessoryBase> registryObject)
    {
        throw new IllegalStateException("Enum not extended");
    }
}
