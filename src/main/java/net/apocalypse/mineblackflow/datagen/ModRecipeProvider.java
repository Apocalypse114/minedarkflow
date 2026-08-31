package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        simpleBuildingBlockRecipe("blackflow_stone", MBFBlocks.BLACKFLOW_STONE.get(), MBFBlocks.BLACKFLOW_STONE_SET, writer);
    }

    private void shapelessEightToOneTransform(String pre,ItemLike eight, ItemLike one, Consumer<FinishedRecipe> writer, String keyEight, String keyOne){
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, one)
                .requires(eight, 8).unlockedBy(getHasName(eight), has(eight))
                .save(writer, pre+"_"+keyEight+"_to_"+keyOne);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, eight, 8)
                .requires(one).unlockedBy(getHasName(one), has(one))
                .save(writer, pre+"_"+keyOne+"_to_"+keyEight);
    }

    private void simpleBuildingBlockRecipe(String name, Block base, MBFBlocks.NonCubeBuildingBlockSet set, Consumer<FinishedRecipe> writer){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.stair().get(), 4)
                .pattern("b  ")
                .pattern("bb ")
                .pattern("bbb")
                .define('b', base).unlockedBy(getHasName(base), has(base)).save(writer, name+"_stairs");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.slab().get(), 2)
                .pattern("bb")
                .define('b', base).unlockedBy(getHasName(base), has(base)).save(writer, name+"_slab");
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.wall().get(), 6)
                .pattern("bbb")
                .pattern("bbb")
                .define('b', base).unlockedBy(getHasName(base), has(base)).save(writer, name+"_wall");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, set.stair().get())
                .unlockedBy(getHasName(base), has(base)).save(writer, "stonecut_"+name+"_stairs");
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, set.slab().get(), 2)
                .unlockedBy(getHasName(base), has(base)).save(writer, "stonecut_"+name+"_slab");;
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, set.wall().get())
                .unlockedBy(getHasName(base), has(base)).save(writer, "stonecut_"+name+"_wall");;
    }
}