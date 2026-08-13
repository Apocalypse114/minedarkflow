package net.apocalypse.mineblackflow.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.apocalypse.mineblackflow.core.Accessories;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.server.command.EnumArgument;

import java.util.ArrayList;
import java.util.List;

public class AccessoryCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> get(CommandBuildContext pContext) {
        return Commands.literal("accessory").then(
                Commands.argument("player", EntityArgument.players()).then(
                        Commands.argument("accessory", EnumArgument.enumArgument(Accessories.class))
                                .then(Commands.argument("num", IntegerArgumentType.integer(1, 16))
                                        .executes(AccessoryCommand::giveAccessory))
                                .executes(AccessoryCommand::giveOneAccessory)
        ));
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
}
