package io.github.marcelofabianov.cubit.player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * {@code /cubit reset} frees the player's own following link, so it needs no permission. The robot
 * that followed finds out by itself, even in a chunk that is unloaded right now.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class ResetCommand {
  public static final String CLEARED_KEY = "command.cubit.reset.cleared";
  public static final String NOBODY_KEY = "command.cubit.reset.nobody";

  private static final String ROOT_LITERAL = "cubit";
  private static final String RESET_LITERAL = "reset";

  private ResetCommand() {}

  @SubscribeEvent
  public static void onRegisterCommands(RegisterCommandsEvent event) {
    event
        .getDispatcher()
        .register(
            Commands.literal(ROOT_LITERAL)
                .then(
                    Commands.literal(RESET_LITERAL)
                        .executes(context -> reset(context.getSource()))));
  }

  private static int reset(CommandSourceStack source) throws CommandSyntaxException {
    ServerPlayer player = source.getPlayerOrException();
    boolean hadLink = player.getData(CubitAttachments.FOLLOWING_ROBOT).robot().isPresent();
    player.setData(CubitAttachments.FOLLOWING_ROBOT, FollowingRobotLink.NOBODY);
    String message = hadLink ? CLEARED_KEY : NOBODY_KEY;
    source.sendSuccess(() -> Component.translatable(message), false);
    return Command.SINGLE_SUCCESS;
  }
}
