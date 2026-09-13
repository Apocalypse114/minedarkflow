package net.apocalypse.mineblackflow.item.base;

import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MultiStateSpawnEgg extends ForgeSpawnEggItem {
    private final List<Supplier<? extends EntityType<? extends Mob>>> variants;
    private final List<String> desc;
    public MultiStateSpawnEgg(Supplier<? extends EntityType<? extends Mob>> type,
                              int backgroundColor, int highlightColor, Rarity rarity) {
        super(type, backgroundColor, highlightColor, new Properties().rarity(rarity));
        variants = new ArrayList<>();
        desc = new ArrayList<>();
    }

    @Override
    public @NotNull EntityType<?> getType(@Nullable CompoundTag tag) {
        if (tag != null && tag.contains(TAG_TYPE) && !variants.isEmpty()) {
            int t = tag.getInt(TAG_TYPE);
            t = validate(t);
            return variants.get(t).get();
        }
        return super.getType(tag);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvance) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvance);
        pTooltipComponents.add(Component.translatable("item.mine_black_flow.mechanist_spawn_egg.note").withStyle(ChatFormatting.GRAY));
        pTooltipComponents.add(Component.translatable("item.mine_black_flow.mechanist_spawn_egg.desc").append(typeDesc(pStack)).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel,
                                                           @NotNull Player pPlayer,
                                                           @NotNull InteractionHand pHand){
        if (pPlayer.isShiftKeyDown()) {
            ItemStack me = pPlayer.getItemInHand(pHand);
            int type = getType(me);
            setType(me, type+1);
            pPlayer.displayClientMessage(Component.translatable("item.mine_black_flow.mechanist_spawn_egg.desc").append(typeDesc(me)), true);
            return InteractionResultHolder.sidedSuccess(me, pPlayer.level().isClientSide);
        }
        return super.use(pLevel, pPlayer, pHand);
    }

    private Component typeDesc(ItemStack stack){
        int t = getType(stack);
        t = validate(t);
        return Component.translatable(desc.get(t));
    }

    private int validate(int t){
        return Mth.clamp(t, 0, variants.size());
    }

    public void addType(Supplier<? extends EntityType<? extends Mob>> type, String desc){
        this.variants.add(type);
        this.desc.add("item.mine_black_flow."+desc);
    }

    private static final String TAG_TYPE = "type";

    public int getType(ItemStack stack){
        return stack.getOrCreateTag().getInt(TAG_TYPE);
    }
    public void setType(ItemStack stack, int t){
        t %= variants.size();
        stack.getOrCreateTag().putInt(TAG_TYPE, t);
    }

    public static int getItemType(ItemStack stack, ClientLevel ignored0, LivingEntity ignored1, int ignored2){
        return stack.getOrCreateTag().getInt(TAG_TYPE);
    }
}
