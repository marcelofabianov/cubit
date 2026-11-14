package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.domain.robot.UnloadCall;
import io.github.marcelofabianov.cubit.domain.robot.UnloadChests;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotTablet;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * The Unload app, for the chosen Cubit: a click on a block with an inventory marks it, a click on a
 * marked chest sends the Cubit to unload there, and Shift and a click anywhere take out the clicked
 * chest, or else the last one marked (answers 1086, 1098 and 1099).
 */
final class TabletUnloadApp {
  private TabletUnloadApp() {}

  static void click(ServerPlayer player, Optional<RobotEntity> chosen, TabletUse.Click click) {
    if (chosen.isEmpty()) {
      say(player, Component.translatable(TabletKeys.DOCK_NO_CUBIT));
      return;
    }
    RobotEntity robot = chosen.get();
    Optional<BlockPos> pos =
        click instanceof TabletUse.Block block ? Optional.of(block.pos()) : Optional.empty();
    if (player.isShiftKeyDown()) {
      unmark(player, robot, pos);
    } else if (pos.isEmpty()) {
      say(player, Component.translatable(TabletKeys.UNLOAD_ONLY_INVENTORY));
      return;
    } else if (robot.docking().unload().marks(pos.get())) {
      unloadThere(player, robot, pos.get());
    } else {
      mark(player, robot, pos.get());
    }
    TabletChestView.send(player);
  }

  private static void unmark(ServerPlayer player, RobotEntity robot, Optional<BlockPos> pos) {
    String key =
        robot.docking().unload().unmark(player, pos)
            ? TabletKeys.UNLOAD_UNMARKED
            : TabletKeys.UNLOAD_NONE;
    say(player, Component.translatable(key, robot.getDisplayName()));
  }

  private static void unloadThere(ServerPlayer player, RobotEntity robot, BlockPos pos) {
    UnloadCall call = robot.docking().unload().unloadInto(player, pos);
    String key =
        switch (call) {
          case READY -> TabletKeys.UNLOAD_GOING;
          case NO_CHEST -> TabletKeys.UNLOAD_NO_CHEST;
          case EMPTY -> TabletKeys.UNLOAD_EMPTY;
          case SWITCHED_OFF -> TabletKeys.DOCK_SWITCHED_OFF;
          case HIBERNATING -> TabletKeys.DOCK_HIBERNATING;
        };
    say(player, Component.translatable(key, robot.getDisplayName()));
  }

  private static void mark(ServerPlayer player, RobotEntity robot, BlockPos pos) {
    Optional<UnloadChests.Mark> mark = robot.docking().unload().click(player, pos);
    if (mark.isEmpty()) {
      say(player, Component.translatable(TabletKeys.UNLOAD_ONLY_INVENTORY));
      return;
    }
    String key =
        switch (mark.get()) {
          case ADDED -> TabletKeys.UNLOAD_MARKED;
          case REMOVED -> TabletKeys.UNLOAD_UNMARKED;
          case FULL -> TabletKeys.UNLOAD_FULL;
        };
    say(
        player,
        Component.translatable(key, robot.getDisplayName(), RobotTablet.unloadOf(robot).chests()));
  }

  private static void say(ServerPlayer player, Component text) {
    player.displayClientMessage(text, true);
  }
}
