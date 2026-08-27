package net.apocalypse.mineblackflow.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.capability.data.MBFDataHandler;
import net.apocalypse.mineblackflow.core.stalk.StalkInstance;
import net.apocalypse.mineblackflow.core.stalk.StalkResource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.command.EnumArgument;

public class StalkCommand {
    private static final LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("stalk")
            .then(Commands.literal("start")
                    .then(Commands.argument("stalk", EnumArgument.enumArgument(StalkResource.class))
                            .executes(StalkCommand::startStalk)
                            .then(Commands.argument("pos", Vec2Argument.vec2())
                                    .executes(StalkCommand::startStalkAt)))
                    .then(Commands.argument("resource", ComponentArgument.textComponent())
                            .executes(StalkCommand::startStalkWithResource)
                            .then(Commands.argument("pos", Vec2Argument.vec2())
                                    .executes(StalkCommand::startStalkAtWithResource)))
            );

    public static LiteralArgumentBuilder<CommandSourceStack> get(){return command;}

    private static int startStalkAt(CommandContext<CommandSourceStack> argument) {
        StalkResource resource = argument.getArgument("stalk", StalkResource.class);
        Vec2 vec2 = Vec2Argument.getVec2(argument, "pos");
        ServerLevel serverLevel = argument.getSource().getLevel();
        int y = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) vec2.x, (int) vec2.y);
        StalkInstance instance = MBFDataHandler.startNewStalk(serverLevel, resource.getLocation(), new BlockPos((int) vec2.x, y, (int) vec2.y));
        if (instance.validInstance()) return sendStartMessage(argument.getSource(), (int) vec2.x, (int) vec2.y, instance);
        return sendFailMessage(argument.getSource(), resource.getLocation().getPath());
    }
    private static int startStalk(CommandContext<CommandSourceStack> argument) {
        StalkResource resource = argument.getArgument("stalk", StalkResource.class);
        Vec3 pos = argument.getSource().getPosition();
        ServerLevel serverLevel = argument.getSource().getLevel();
        int y = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) pos.x, (int) pos.z);
        StalkInstance instance = MBFDataHandler.startNewStalk(serverLevel, resource.getLocation(), new BlockPos((int) pos.x, y, (int) pos.z));
        if(instance.validInstance()) return sendStartMessage(argument.getSource(), (int) pos.x, (int) pos.z, instance);
        return sendFailMessage(argument.getSource(), resource.getLocation().getPath());
    }
    private static int startStalkAtWithResource(CommandContext<CommandSourceStack> argument) {
        String location = ComponentArgument.getComponent(argument, "resource").getString();
        Vec2 vec2 = Vec2Argument.getVec2(argument, "pos");
        ServerLevel serverLevel = argument.getSource().getLevel();
        int y = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) vec2.x, (int) vec2.y);
        StalkInstance instance = MBFDataHandler.startNewStalk(serverLevel, MineBlackFlow.modLoc(location), new BlockPos((int) vec2.x, y, (int) vec2.y));
        if (instance.validInstance()) return sendStartMessage(argument.getSource(), (int) vec2.x, (int) vec2.y, instance);
        return sendFailMessage(argument.getSource(), location);
    }
    private static int startStalkWithResource(CommandContext<CommandSourceStack> argument) {
        String location = ComponentArgument.getComponent(argument, "resource").getString();
        Vec3 pos = argument.getSource().getPosition();
        ServerLevel serverLevel = argument.getSource().getLevel();
        int y = serverLevel.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) pos.x, (int) pos.z);
        StalkInstance instance = MBFDataHandler.startNewStalk(serverLevel, MineBlackFlow.modLoc(location), new BlockPos((int) pos.x, y, (int) pos.z));
        if (instance.validInstance()) sendStartMessage(argument.getSource(), (int) pos.x, (int) pos.z, instance);
        return sendFailMessage(argument.getSource(), location);
    }
    private static int sendStartMessage(CommandSourceStack stack, int x, int z, StalkInstance instance){
        stack.sendSuccess(()-> Component.translatable("command.mine_black_flow.stalk.start", x, z, instance.getCast().getDesc()), true);
        return 0;
    }
    private static int sendFailMessage(CommandSourceStack stack, String id){
        stack.sendFailure(Component.translatable("command.mine_black_flow.stalk.fail").append(id));
        return -1;
    }
}
