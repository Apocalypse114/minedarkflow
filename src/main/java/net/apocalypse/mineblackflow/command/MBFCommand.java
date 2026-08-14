package net.apocalypse.mineblackflow.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MBFCommand {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(MineBlackFlow.MODID)
                .then(ManiaCommand.get())
                .then(AccessoryCommand.get());

        dispatcher.register(command);
    }
}
