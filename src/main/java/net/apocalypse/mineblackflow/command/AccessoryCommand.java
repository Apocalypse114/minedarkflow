package net.apocalypse.mineblackflow.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.apocalypse.mineblackflow.core.Accessories;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.command.EnumArgument;

import java.util.ArrayList;
import java.util.List;

public class AccessoryCommand {
    private static final LiteralArgumentBuilder<CommandSourceStack> give = Commands.literal("give").then(
            Commands.argument("player", EntityArgument.players()).then(
                    Commands.argument("accessory", EnumArgument.enumArgument(Accessories.class))
                            .then(Commands.argument("num", IntegerArgumentType.integer(1, 16))
                                    .executes(AccessoryCommand::giveAccessory))
                            .executes(AccessoryCommand::giveOneAccessory)
            ));
    private static final LiteralArgumentBuilder<CommandSourceStack> sell = Commands.literal("sell")
            .then(Commands.literal("main_hand").executes(AccessoryCommand::sellInMainHand))
            .then(Commands.literal("inventory").executes(AccessoryCommand::sellAll));

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("accessory").requires((s)->s.hasPermission(2))
                .then(give).then(sell);
    }

    private static int giveAccessory(CommandContext<CommandSourceStack> argument) throws CommandSyntaxException {
        Accessories input = argument.getArgument("accessory", Accessories.class);
        AccessoryBase accessory = input.getAccessory();
        int count = IntegerArgumentType.getInteger(argument, "num");
        List<ServerPlayer> players = new ArrayList<>(EntityArgument.getPlayers(argument, "player"));
        if (players.isEmpty()) return 0;
        for (ServerPlayer player: players){
            accessory.giveTo(player, count);
        }
        if (players.size() == 1) sendSuccessInfoSingle(count, accessory, argument.getSource(), players.get(0));
        else sendSuccessInfo(count, accessory, argument.getSource(), players.size());
        return players.size();
    }
    private static int giveOneAccessory(CommandContext<CommandSourceStack> argument) throws CommandSyntaxException {
        Accessories input = argument.getArgument("accessory", Accessories.class);
        AccessoryBase accessory = input.getAccessory();
        List<ServerPlayer> players = new ArrayList<>(EntityArgument.getPlayers(argument, "player"));
        if (players.isEmpty()) return 0;
        for (ServerPlayer player: players){
            accessory.giveTo(player);
        }
        if (players.size() == 1) sendSuccessInfoSingle(1, accessory, argument.getSource(), players.get(0));
        else sendSuccessInfo(1, accessory, argument.getSource(), players.size());
        return players.size();
    }
    private static int sellInMainHand(CommandContext<CommandSourceStack> argument) {
        Player player = argument.getSource().getPlayer();
        if (player == null) return 0;
        ItemStack stack = player.getMainHandItem();
        ItemStack copy = stack.copy();
        if (AccessoryBase.sellFromPlayer(stack, player)){
            sendSoldInfo(copy, argument.getSource());
            return 1;
        }
        sendSoldFail(argument.getSource());
        return 0;
    }
    private static int sellAll(CommandContext<CommandSourceStack> argument) {
        Player player = argument.getSource().getPlayer();
        if (player == null) return 0;
        MBFUtil.forEachItemInPlayerInventory(player, stack -> AccessoryBase.sellFromPlayer(stack, player));
        argument.getSource().sendSuccess(()-> Component.translatable("command.mine_black_flow.accessory.sell_many"), true);
        return 0;
    }
    private static void sendSuccessInfo(int count, AccessoryBase accessory, CommandSourceStack source, int num){
        source.sendSuccess(()-> Component.translatable("command.mine_black_flow.accessory.give.1")
                .append(accessory.getDescription()).append(count > 1 ? " x "+count: "")
                .append(Component.translatable("command.mine_black_flow.accessory.give.2"))
                .append(""+num)
                .append(Component.translatable("command.mine_black_flow.accessory.give.3")), true);;
    }
    private static void sendSuccessInfoSingle(int count, AccessoryBase accessory, CommandSourceStack source, Player player){
        source.sendSuccess(()-> Component.translatable("command.mine_black_flow.accessory.give.1")
                .append(accessory.getDescription()).append(count > 1 ? " x "+count: "")
                .append(Component.translatable("command.mine_black_flow.accessory.give.2"))
                .append(player.getDisplayName()), true);
    }
    private static void sendSoldInfo(ItemStack accessory, CommandSourceStack source){
        source.sendSuccess(()-> Component.translatable("command.mine_black_flow.accessory.sell")
                .append(accessory.getDisplayName()), true);
    }
    private static void sendSoldFail(CommandSourceStack source){
        source.sendFailure(Component.translatable("command.mine_black_flow.accessory.sell_fail"));
    }
}
