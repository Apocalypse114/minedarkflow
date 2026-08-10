package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.init.MBFTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {

    public ModEntityTypeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, MineBlackFlow.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(MBFTags.Entities.MANIA_2K).add(
                EntityType.ELDER_GUARDIAN, EntityType.PIGLIN_BRUTE, EntityType.EVOKER, EntityType.ENDERMAN
        );
        this.tag(MBFTags.Entities.MANIA_4K).add(
                EntityType.WITHER, EntityType.WARDEN
        );
        this.tag(MBFTags.Entities.MANIA_8K).add(
                EntityType.IRON_GOLEM, EntityType.SNOW_GOLEM
        );
        this.tag(MBFTags.Entities.MANIA_10K);
        this.tag(MBFTags.Entities.MANIA_IMMUNE).add(
                EntityType.SLIME, EntityType.MAGMA_CUBE, EntityType.ALLAY, EntityType.ENDER_DRAGON
        );
    }
}