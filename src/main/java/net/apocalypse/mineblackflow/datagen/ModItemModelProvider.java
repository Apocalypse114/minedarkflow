package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("removal")
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MineBlackFlow.MODID, existingFileHelper);
    }

    private static final ResourceLocation EMPTY = new ResourceLocation("wooden_sword");
    private static final ResourceLocation ITEM = new ResourceLocation("item/generated");
    private static final ResourceLocation HANDHELD = new ResourceLocation("item/handheld");

    @Override
    protected void registerModels(){
        handheldItem(MBFItems.NULL_MASK_WAND);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return simpleItem(item, "");
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item, String location) {
        return withExistingParent(item.getId().getPath(), ITEM).texture("layer0", MineBlackFlow.modLoc("item/"+location+item.getId().getPath()));
    }
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), HANDHELD)
                .texture("layer0", MineBlackFlow.modLoc("item/" + item.getId().getPath()));
    }
}