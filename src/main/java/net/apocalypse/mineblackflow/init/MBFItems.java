package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.item.AccessoryBoxItem;
import net.apocalypse.mineblackflow.item.BlackflowiumIngotItem;
import net.apocalypse.mineblackflow.item.NullMaskWandItem;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.apocalypse.mineblackflow.item.natural.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MBFItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, MineBlackFlow.MODID);
    public static final DeferredRegister<Item> ACCESSORY = DeferredRegister.create(Registries.ITEM, MineBlackFlow.MODID);

    public static final RegistryObject<Item> NULL_MASK_WAND = REGISTRY.register("null_mask_wand", NullMaskWandItem::new);
    public static final RegistryObject<Item> BLACKFLOWIUM_INGOT = REGISTRY.register("blackflowium_ingot", BlackflowiumIngotItem::new);
    public static final RegistryObject<Item> ACCESSORY_BOX = REGISTRY.register("accessory_box", AccessoryBoxItem::new);

    public static final RegistryObject<AccessoryBase> SEEDS = ACCESSORY.register("seeds", SeedItem::new);
    public static final RegistryObject<AccessoryBase> MISTY_TUMBLEWEED = ACCESSORY.register("misty_tumbleweed", MistyTumbleweedItem::new);
    public static final RegistryObject<AccessoryBase> BLOOD_MUSHROOM = ACCESSORY.register("blood_mushroom", BloodMushroomItem::new);
    public static final RegistryObject<AccessoryBase> SPINDRIFT = ACCESSORY.register("spindrift", SpindriftItem::new);
    public static final RegistryObject<AccessoryBase> FROST_TREE = ACCESSORY.register("frost_tree", FrostTree::new);
    public static final RegistryObject<AccessoryBase> ECHO_CORN = ACCESSORY.register("echo_corn", EchoCorn::new);
    public static final RegistryObject<AccessoryBase> MULTI_MOSS = ACCESSORY.register("multi_moss", MultiMossItem::new);
    public static final RegistryObject<AccessoryBase> DEAD_MOSS_BALL = ACCESSORY.register("dead_moss_ball", DeadMossBallItem::new);
    public static final RegistryObject<AccessoryBase> HOMESICK_FRUIT = ACCESSORY.register("homesick_fruit", HomesickFruitItem::new);
    public static final RegistryObject<AccessoryBase> PLATE_VINE = ACCESSORY.register("plate_vine", PlateVineItem::new);
    public static final RegistryObject<AccessoryBase> SHINY_TRUFFLE = ACCESSORY.register("shiny_truffle", ShinyTruffleItem::new);
    public static final RegistryObject<AccessoryBase> CAGE_CONTROLLER = ACCESSORY.register("cage_controller", CageControllerItem::new);

    public static final RegistryObject<AccessoryBase> BUCKET_APOCATA = REGISTRY.register("bucket_apocata", AABucketApocataItem::new);

    public static final RegistryObject<Item> NULL_DOG_SPAWN_EGG = spawnEgg("the_null_value", MBFEntities.THE_NULL_VALUE, 0x000000, 0xffffff);
    public static final RegistryObject<Item> WATER_PRAISER_SPAWN_EGG = spawnEgg("water_praiser", MBFEntities.WATER_PRAISER, 0xcfbdb1, 0x3b6b65);
    public static final RegistryObject<Item> ELEPHANT_SPAWN_EGG = spawnEgg("forsaken_earthshaker", MBFEntities.FORSAKEN_EARTHSHAKER, 0x61797e, 0x454b4d);
    public static final RegistryObject<Item> WIND_HUNTER_SPAWN_EGG = spawnEgg("wind_hunter", MBFEntities.WIND_HUNTER, 0x90705c, 0x335f4b);
    public static final RegistryObject<Item> HUNTING_DOG_PROTO_SPAWN_EGG = spawnEgg("huntingdog_proto", MBFEntities.HUNTING_DOG_PROTO, 0x3f4244, 0x354b41);

    public static final RegistryObject<Item> BLACKFLOWIUM_CLUSTER = blockItem(MBFBlocks.BLACKFLOWIUM_CLUSTER);
    public static final RegistryObject<Item> BLACKFLOWIUM_BLOCK = blockItem(MBFBlocks.BLACKFLOWIUM_BLOCK);
    public static final RegistryObject<Item> BLOOD_MUSHROOM_PLANT = blockItem(MBFBlocks.BLOOD_MUSHROOM_BLOCK);

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData){

    }
    public static RegistryObject<Item> spawnEgg(String name, Supplier<? extends EntityType<? extends Mob>> type, int backColor, int spotColor){
        return REGISTRY.register(name+"_spawn_egg", ()->new ForgeSpawnEggItem(type, backColor, spotColor, new Item.Properties()));
    }
    public static RegistryObject<Item> blockItem(RegistryObject<Block> block){
        return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static CreativeModeTab mbfItemsTab(){
        return CreativeModeTab.builder().icon(()->new ItemStack(MBFItems.BLACKFLOWIUM_INGOT.get()))
                .title(Component.translatable("creative_tab.mine_black_flow.mbf_items").withStyle(ChatFormatting.WHITE))
                .withBackgroundLocation(MineBlackFlow.modLoc("textures/gui/tab/tab_items_1.png"))
                .withTabsImage(MineBlackFlow.modLoc("textures/gui/tab/tabs.png"))
                .displayItems((para, tab)->{
                    tab.accept(ACCESSORY_BOX.get());
                    tab.accept(BLOOD_MUSHROOM_PLANT.get());

                    tab.accept(BLACKFLOWIUM_INGOT.get());
                    tab.accept(BLACKFLOWIUM_CLUSTER.get());
                    tab.accept(BLACKFLOWIUM_BLOCK.get());

                    tab.accept(NULL_DOG_SPAWN_EGG.get());
                    tab.accept(WATER_PRAISER_SPAWN_EGG.get());
                    tab.accept(WIND_HUNTER_SPAWN_EGG.get());
                    tab.accept(ELEPHANT_SPAWN_EGG.get());
                    tab.accept(HUNTING_DOG_PROTO_SPAWN_EGG.get());

                    tab.accept(NULL_MASK_WAND.get());
                }).build();
    }
    public static CreativeModeTab naturalObjectTab(){
        return CreativeModeTab.builder().icon(()->new ItemStack(SEEDS.get()))
                .title(Component.translatable("creative_tab.mine_black_flow.natural_objects").withStyle(ChatFormatting.WHITE))
                .withBackgroundLocation(MineBlackFlow.modLoc("textures/gui/tab/tab_items.png"))
                .withTabsImage(MineBlackFlow.modLoc("textures/gui/tab/tabs.png"))
                .displayItems(((pParameters, tab) -> {
                    tab.accept(SEEDS.get());
                    tab.accept(BLOOD_MUSHROOM.get());
                    tab.accept(MISTY_TUMBLEWEED.get());
                    tab.accept(ECHO_CORN.get());
                    tab.accept(SPINDRIFT.get());
                    tab.accept(FROST_TREE.get());
                    tab.accept(MULTI_MOSS.get());
                    tab.accept(DEAD_MOSS_BALL.get());
                    tab.accept(PLATE_VINE.get());
                    tab.accept(HOMESICK_FRUIT.get());
                    tab.accept(SHINY_TRUFFLE.get());
                    tab.accept(CAGE_CONTROLLER.get());

                    tab.accept(BUCKET_APOCATA.get());
                })).build();
    }
}
