package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        shapelessEightToOneTransform("blackflowium",
                MBFItems.BLACKFLOWIUM_INGOT.get(),
                MBFBlocks.BLACKFLOWIUM_CLUSTER.get(), writer,
                "ingot", "cluster");
        shapelessEightToOneTransform("blackflowium",
                MBFBlocks.BLACKFLOWIUM_CLUSTER.get(),
                MBFBlocks.BLACKFLOWIUM_BLOCK.get(), writer,
                "cluster", "block");
    }

    private void shapelessEightToOneTransform(String pre,ItemLike eight, ItemLike one, Consumer<FinishedRecipe> writer, String keyEight, String keyOne){
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, one)
                .requires(eight, 8).unlockedBy(getHasName(eight), has(eight))
                .save(writer, pre+"_"+keyEight+"_to_"+keyOne);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, eight, 8)
                .requires(one).unlockedBy(getHasName(one), has(one))
                .save(writer, pre+"_"+keyOne+"_to_"+keyEight);
    }
}