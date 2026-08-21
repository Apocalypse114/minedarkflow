package net.apocalypse.mineblackflow.command;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.apocalypse.mineblackflow.core.ManiaInjury;
import net.apocalypse.mineblackflow.core.ManiaInjurySource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class ManiaCommand {
    private static final LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("mania").then(
            Commands.argument("entities", EntityArgument.entities())
                    .then(Commands.literal("hurt").requires(s->s.hasPermission(2))
                            .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                                    .executes(ManiaCommand::maniaHurt)))
                    .then(Commands.literal("heal").requires(s->s.hasPermission(2))
                            .then(Commands.argument("amount", FloatArgumentType.floatArg(0))
                                    .executes(ManiaCommand::maniaHeal))));

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return command;
    }

    private static int maniaHurt(CommandContext<CommandSourceStack> argument) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(argument, "entities");
        float amount = FloatArgumentType.getFloat(argument, "amount");
        int executedCount = 0;
        LivingEntity singleCandidate = null;

        for (Entity entity: entities){
            if (entity instanceof LivingEntity living
                    && ManiaInjury.dealManiaInjury(living, amount, ManiaInjurySource.fromCommand(argument.getSource())).success()){
                if (executedCount == 0) singleCandidate = living;
                executedCount ++;
            }
        }
        Component finalName = singleCandidate == null? Component.empty(): singleCandidate.getDisplayName();
        int finalCount = executedCount;
        if (executedCount <= 0) argument.getSource().sendFailure(Component.translatable("command.mine_black_flow.mania.fail"));
        else argument.getSource().sendSuccess(()-> successMessageHurt(finalCount, amount, finalName), true);
        return executedCount;
    }

    private static int maniaHeal(CommandContext<CommandSourceStack> argument) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(argument, "entities");
        float amount = FloatArgumentType.getFloat(argument, "amount");
        int executedCount = 0;
        LivingEntity singleCandidate = null;

        for (Entity entity: entities){
            if (entity instanceof LivingEntity living){
                ManiaInjury.healManiaInjury(living, amount);
                if (executedCount == 0) singleCandidate = living;
                executedCount ++;
            }
        }
        Component finalName = singleCandidate == null? Component.empty(): singleCandidate.getDisplayName();
        int finalCount = executedCount;
        if (executedCount <= 0) argument.getSource().sendFailure(Component.translatable("command.mine_black_flow.mania.fail"));
        else argument.getSource().sendSuccess(()-> successMessageHeal(finalCount, amount, finalName), true);
        return executedCount;
    }

    private static Component successMessageHurt(int number, float amount, Component name){
        return number == 1 ? Component.translatable("command.mine_black_flow.mania.hurt.success_single", amount, name) :
                Component.translatable("command.mine_black_flow.mania.hurt.success_multiple", amount, number);
    }
    private static Component successMessageHeal(int number, float amount, Component name){
        return number == 1 ? Component.translatable("command.mine_black_flow.mania.heal.success_single", amount, name) :
                Component.translatable("command.mine_black_flow.mania.heal.success_multiple", amount, number);
    }
}
