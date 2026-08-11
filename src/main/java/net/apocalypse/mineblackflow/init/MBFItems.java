package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.item.BlackflowiumIngotItem;
import net.apocalypse.mineblackflow.item.NullMaskWandItem;
import net.apocalypse.mineblackflow.item.natural.*;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class MBFItems {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(Registries.ITEM, MineBlackFlow.MODID);
    public static final DeferredRegister<Item> NATURAL_OBJECTS = DeferredRegister.create(Registries.ITEM, MineBlackFlow.MODID);

    public static final RegistryObject<Item> NULL_MASK_WAND = REGISTRY.register("null_mask_wand", NullMaskWandItem::new);
    public static final RegistryObject<Item> BLACKFLOWIUM_INGOT = REGISTRY.register("blackflowium_ingot", BlackflowiumIngotItem::new);

    public static final RegistryObject<Item> SEEDS = NATURAL_OBJECTS.register("seeds", SeedItem::new);
    public static final RegistryObject<Item> MISTY_TUMBLEWEED = NATURAL_OBJECTS.register("misty_tumbleweed", MistyTumbleweedItem::new);
    public static final RegistryObject<Item> BLOOD_MUSHROOM = NATURAL_OBJECTS.register("blood_mushroom", BloodMushroomItem::new);
    public static final RegistryObject<Item> SPINDRIFT = NATURAL_OBJECTS.register("spindrift", SpindriftItem::new);
    public static final RegistryObject<Item> FROST_TREE = NATURAL_OBJECTS.register("frost_tree", FrostTree::new);
    public static final RegistryObject<Item> ECHO_CORN = NATURAL_OBJECTS.register("echo_corn", EchoCorn::new);

    public static final RegistryObject<Item> BUCKET_APOCATA = REGISTRY.register("bucket_apocata", AABucketApocataItem::new);

    public static final RegistryObject<Item> NULL_DOG_SPAWN_EGG = spawnEgg("the_null_value", MBFEntities.THE_NULL_VALUE, 0x000000, 0xffffff);
    public static final RegistryObject<Item> WATER_PRAISER_SPAWN_EGG = spawnEgg("water_praiser", MBFEntities.WATER_PRAISER, 0xcfbdb1, 0x3b6b65);
    public static final RegistryObject<Item> ELEPHANT_SPAWN_EGG = spawnEgg("forsaken_earthshaker", MBFEntities.FORSAKEN_EARTHSHAKER, 0x61797e, 0x454b4d);

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData){

    }
    public static RegistryObject<Item> spawnEgg(String name, Supplier<? extends EntityType<? extends Mob>> type, int backColor, int spotColor){
        return REGISTRY.register(name+"_spawn_egg", ()->new ForgeSpawnEggItem(type, backColor, spotColor, new Item.Properties()));
    }

    public static CreativeModeTab mbfItemsTab(){
        return CreativeModeTab.builder().icon(()->new ItemStack(Items.OAK_SAPLING))
                .title(Component.translatable("creative_tab.mine_black_flow.mbf_items").withStyle(ChatFormatting.WHITE))
                .withBackgroundLocation(MineBlackFlow.modLoc("textures/gui/tab/tab_items_1.png"))
                .withTabsImage(MineBlackFlow.modLoc("textures/gui/tab/tabs.png"))
                .displayItems((para, tab)->{
                    tab.accept(BLACKFLOWIUM_INGOT.get());

                    tab.accept(NULL_DOG_SPAWN_EGG.get());
                    tab.accept(WATER_PRAISER_SPAWN_EGG.get());
                    tab.accept(ELEPHANT_SPAWN_EGG.get());

                    tab.accept(NULL_MASK_WAND.get());
                }).withTabsAfter(MineBlackFlow.modLoc("natural_objects")).build();
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

                    tab.accept(BUCKET_APOCATA.get());
                })).withTabsBefore(MineBlackFlow.modLoc("mbf_items")).build();
    }
}
