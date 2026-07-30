package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.init.MBFDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class DamageTypeTagProvider extends DamageTypeTagsProvider {

    public DamageTypeTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, MineBlackFlow.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(DamageTypeTags.BYPASSES_RESISTANCE).add(MBFDamageTypes.MANIA_SWALLOW);
        this.tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(MBFDamageTypes.MANIA_SWALLOW);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(MBFDamageTypes.MANIA_SWALLOW);
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(MBFDamageTypes.MANIA_SWALLOW);
        this.tag(DamageTypeTags.BYPASSES_COOLDOWN).add(MBFDamageTypes.MANIA_SWALLOW);
    }
}