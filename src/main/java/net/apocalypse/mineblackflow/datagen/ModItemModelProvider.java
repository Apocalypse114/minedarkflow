package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("removal")
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MineBlackFlow.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels(){
        withExistingParent(MBFItems.MECHANIST_SPAWN_EGG.getId().getPath(), "item/generated")
                .override()
                .predicate(MineBlackFlow.modLoc("mechanist_type"), 0)
                .model(withExistingParent("mechanist_npc", "item/generated")
                        .texture("layer0", "item/mechanist_npc_spawn_egg"))
                .end().override()
                .predicate(MineBlackFlow.modLoc("mechanist_type"), 1)
                .model(withExistingParent("mechanist_opr", "item/generated")
                        .texture("layer0", "item/mechanist_opr_spawn_egg"))
                .end().override()
                .predicate(MineBlackFlow.modLoc("mechanist_type"), 2)
                .model(withExistingParent("mechanist_enemy", "item/generated")
                        .texture("layer0", "item/mechanist_enemy_spawn_egg"));
    }

    private ItemModelBuilder simpleItem(RegistryObject<? extends Item> item) {
        return simpleItem(item, "");
    }

    private ItemModelBuilder simpleItem(RegistryObject<? extends Item> item, String location) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", MineBlackFlow.modLoc("item/"+location+item.getId().getPath()));
    }
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld"))
                .texture("layer0", MineBlackFlow.modLoc("item/" + item.getId().getPath()));
    }
    private ItemModelBuilder spawnEggItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/template_spawn_egg"));
    }
}