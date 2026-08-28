package net.apocalypse.mineblackflow.compat.curios;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class MBFCuriosUtil {
    public static boolean isAccessoryEquipped(Player player, Item item){
        LazyOptional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player);
        if (handler.isPresent() && handler.resolve().isPresent()){
            ICuriosItemHandler itemHandler = handler.resolve().get();
            Optional<SlotResult> curioFound = itemHandler.findFirstCurio(item);
            return curioFound.isPresent();
        }
        return false;
    }
}
