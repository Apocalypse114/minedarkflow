package net.apocalypse.mineblackflow.item.base;

import net.apocalypse.mineblackflow.init.CollectibleRarity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AccessoryBase extends Item implements IBlackflowiumPriced{
    private final String DESC, FUNC;
    public final FuncCase functionCase;
    public AccessoryBase(int intRarity, String id, FuncCase funcCase) {
        super(new Properties().rarity(switch (intRarity){
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
        pTooltipComponents.add(Component.translatable(DESC).withStyle(ChatFormatting.ITALIC));
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

    public abstract int getBaseEvaluation();
    public static int getEvaluation(ItemStack stack){
        if (stack.getItem() instanceof AccessoryBase obj)
            return obj.getBaseEvaluation() + stack.getOrCreateTag().getInt(TAG_CHANGE);
        return 0;
    }
    public static void setEvaluation(ItemStack stack, int price){
        stack.getOrCreateTag().putInt(TAG_CHANGE, Math.max(0, price));
    }
    public static void boostEvaluation(ItemStack stack, int boost){
        setEvaluation(stack, getEvaluation(stack)+boost);}

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
