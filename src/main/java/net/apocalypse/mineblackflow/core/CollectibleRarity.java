package net.apocalypse.mineblackflow.core;

import net.minecraft.world.item.Rarity;

public class CollectibleRarity {
    public static final Rarity COMMON = Rarity.create("MBF_RELIC_COMMON", style -> style.withColor(0x99a8aa));
    public static final Rarity UNCOMMON = Rarity.create("NBF_RELIC_UNCOMMON", style -> style.withColor(0x1f999c));
    public static final Rarity RARE = Rarity.create("MBF_RELIC_RARE", style -> style.withColor(0xb20b38));
}
