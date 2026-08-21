package net.apocalypse.mineblackflow.item.base;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.CollectibleRarity;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnusedReturnValue")
public abstract class AccessoryBase extends Item implements IBlackflowiumPriced{
    private final String DESC, FUNC;
    public final FuncCase functionCase;
    public AccessoryBase(int intRarity, String id, FuncCase funcCase, boolean canStack) {
        super(new Properties().stacksTo(canStack?16:1).rarity(switch (intRarity){
            case 0-> CollectibleRarity.COMMON;
            case 1-> CollectibleRarity.UNCOMMON;
            case 2-> CollectibleRarity.RARE;
            default -> Rarity.COMMON;
        }));
        DESC = "item.mine_black_flow."+id+".desc";
        FUNC = "item.mine_black_flow."+id+".func";
        functionCase = funcCase;
    }
    public AccessoryBase(int intRarity, String id){
        this(intRarity, id, FuncCase.EMPTY);
    }
    public AccessoryBase(int intRarity, String id, FuncCase funcCase){this(intRarity, id, funcCase, false);}

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvance){
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvance);
        pTooltipComponents.add(functionCase.description().append(Component.translatable(FUNC).withStyle(ChatFormatting.WHITE)));
        pTooltipComponents.add(Component.translatable(DESC).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        int eva = getEvaluation(pStack);
        ChatFormatting color = eva > 0 ? ChatFormatting.GREEN: ChatFormatting.RED;
        pTooltipComponents.add(Component.translatable(KEY_EVA)
                .append(Component.literal(""+eva))
                .withStyle(color));
        if (pIsAdvance.isAdvanced() && pStack.getItem() instanceof AccessoryBase obj)
            pTooltipComponents.add(Component.translatable(KEY_BASE_EVA)
                    .append(Component.literal(""+obj.getBaseEvaluation()))
                    .withStyle(ChatFormatting.GREEN));
    }

    public void onGain(ItemStack accessoryStack, Player pOwner){}
    public void onOuterGain(ItemStack outerAccessoryStack, ItemStack currentAccessoryStack, Player pOwner){}

    public abstract int getBaseEvaluation();
    public static int getEvaluation(ItemStack stack){
        if (stack.getItem() instanceof AccessoryBase obj)
            return Math.min(999, obj.getBaseEvaluation() + stack.getOrCreateTag().getInt(TAG_CHANGE));
        return 0;
    }

    public static void setEvaluation(ItemStack stack, int price){
        stack.getOrCreateTag().putInt(TAG_CHANGE,  price);
    }
    public static void boostEvaluation(ItemStack stack, int boost){
        setEvaluation(stack, stack.getOrCreateTag().getInt(TAG_CHANGE) + boost);}

    public static List<ItemStack> soldResult(ItemStack good){
        int num = getEvaluation(good) * good.getCount();
        List<ItemStack> list = new ArrayList<>();
        if (num <= 0) return list;
        if (num > 999) num = 999;
        int num_64_raw = num / 64, num_8_raw = num / 8;
        if (num <= 64) list.add(new ItemStack(MBFItems.BLACKFLOWIUM_INGOT.get(), num));
        else if (num_8_raw <= 64){
            list.add(new ItemStack(MBFItems.BLACKFLOWIUM_CLUSTER.get(), num_8_raw));
            num -= num_8_raw * 8;
            if (num > 0) list.add(new ItemStack(MBFItems.BLACKFLOWIUM_INGOT.get(), num));
        }else {
            list.add(new ItemStack(MBFItems.BLACKFLOWIUM_BLOCK.get(), num_64_raw));
            num -= num_64_raw * 64; num_8_raw = num / 8;
            if (num_8_raw > 0) list.add(new ItemStack(MBFItems.BLACKFLOWIUM_CLUSTER.get(), num_8_raw));
            num -= num_8_raw * 8;
            if (num > 0) list.add(new ItemStack(MBFItems.BLACKFLOWIUM_INGOT.get(), num));
        }
        return list;
    }

    public static boolean sellFromPlayer(ItemStack pStack, Player pPlayer){
        if (pStack.getItem() == MBFItems.BUCKET_APOCATA.get()){
            pPlayer.displayClientMessage(Component.translatable("gameplay.mine_black_flow.sell_apocata"), false);
            pPlayer.hurt(pPlayer.level().damageSources().genericKill(), 32);
            return false;
        }
        List<ItemStack> income = soldResult(pStack);
        if (!income.isEmpty()){
            pStack.shrink(pStack.getCount());
            income.forEach(stack -> ItemHandlerHelper.giveItemToPlayer(pPlayer, stack));
            return true;
        }
        return false;
    }
    public List<ItemStack> giveTo(Player player, int pCount){
        return giveAccessoryTo(this, player, pCount);
    }
    public List<ItemStack> giveTo(Player player){return giveTo(player, 1);}

    public List<ItemStack> giveWithMessage(Player player){
        List<ItemStack> list = this.giveTo(player);
        MutableComponent message = Component.translatable("gameplay.mine_black_flow.gain_acc");
        for (ItemStack stack : list){
            message.append(stack.getDisplayName());
        }
        player.displayClientMessage(message, true);
        return list;
    }

    public static List<ItemStack> giveAccessoryTo(AccessoryBase accessory, Player pPlayer, int pCount){
        List<ItemStack> stacks = new ArrayList<>();
        if (pCount <= 0) return stacks;
        int countLeft = pCount;
        while (countLeft > 0){
            stacks.add(giveAccessoryToRaw(accessory, pPlayer));
            countLeft -= 1;
        }
        return stacks;
    }

    private static ItemStack giveAccessoryToRaw(AccessoryBase accessory, Player pPlayer){
        ItemStack stack = new ItemStack(accessory);
        accessory.onGain(stack, pPlayer);
        MBFUtil.forEachItemInPlayerInventory(pPlayer, item ->{
            if (item.getItem() instanceof AccessoryBase base){
                base.onOuterGain(stack, item, pPlayer);
            }
        });
        ItemHandlerHelper.giveItemToPlayer(pPlayer, stack);
        return stack;
    }

    public static final String TAG_CHANGE = "evaluation_change", TAG_MODIFIED = "modified",
            KEY_EVA = "item.mine_black_flow.natural_object.evaluation",
            KEY_BASE_EVA = "item.mine_black_flow.natural_object.base_evaluation";

    public enum FuncCase{
        INVENTORY("item.mine_black_flow.natural_object.carrying"),
        HOTBAR("item.mine_black_flow.natural_object.hotbar"),
        CURIOS("item.mine_black_flow.natural_object.curios"),
        HAND("item.mine_black_flow.natural_object.hold"),
        EMPTY("")
        ;

        private final String descr;
        FuncCase(String desc){
            this.descr = desc;
        }
        public MutableComponent description(){
            if (this == EMPTY) return Component.empty();
            return Component.translatable(descr);
        }
    }
}
