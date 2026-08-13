package net.apocalypse.mineblackflow.item.base;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.init.CollectibleRarity;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
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
    public AccessoryBase(int intRarity, String id, FuncCase funcCase) {
        super(new Properties().stacksTo(16).rarity(switch (intRarity){
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
            return obj.getBaseEvaluation() + stack.getOrCreateTag().getInt(TAG_CHANGE);
        return 0;
    }

    public static void setEvaluation(ItemStack stack, int price){
        stack.getOrCreateTag().putInt(TAG_CHANGE,  price);
    }
    public static void boostEvaluation(ItemStack stack, int boost){
        setEvaluation(stack, stack.getOrCreateTag().getInt(TAG_CHANGE) + boost);}

    public static ItemStack soldResult(ItemStack good){
        int num = getEvaluation(good);
        if (num <= 0) return ItemStack.EMPTY;
        return new ItemStack(MBFItems.BLACKFLOWIUM_INGOT.get(), num);
    }
    public List<ItemStack> giveTo(Player player, int pCount){
        return giveAccessoryTo(this, player, pCount);
    }
    public List<ItemStack> giveTo(Player player){return giveTo(player, 1);}

    public static List<ItemStack> giveAccessoryTo(AccessoryBase accessory, Player pPlayer, int pCount){
        List<ItemStack> stacks = new ArrayList<>();
        if (pCount <= 0) return stacks;
        int countLeft = pCount;
        while (countLeft > 0){
            int givenCount = Math.min(countLeft, 16);
            stacks.add(giveAccessoryToRaw(accessory, pPlayer, givenCount));
            countLeft -= givenCount;
        }
        return stacks;
    }

    private static ItemStack giveAccessoryToRaw(AccessoryBase accessory, Player pPlayer, int pCount){
        ItemStack stack = new ItemStack(accessory, pCount);
        ItemHandlerHelper.giveItemToPlayer(pPlayer, stack);
        accessory.onGain(stack, pPlayer);
        MBFUtil.forEachItemInPlayerInventory(pPlayer, item ->{
            if (item != stack && item.getItem() instanceof AccessoryBase base){
                base.onOuterGain(stack, item, pPlayer);
            }
        });
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
