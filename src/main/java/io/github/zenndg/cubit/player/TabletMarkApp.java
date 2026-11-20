package io.github.zenndg.cubit.player;

import io.github.zenndg.cubit.domain.robot.UnloadChests;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.entity.RobotMarks;
import io.github.zenndg.cubit.menu.TabletKeys;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * The Mark app, for the chosen Organizer: a click on a block with an inventory marks it with the
 * face clicked, a click on a marked one unmarks it, and Shift and a click anywhere take out the
 * clicked block, or else the last one marked, as the Unload app (answers 1296 and 1297).
 */
final class TabletMarkApp {
  private TabletMarkApp() {}

  static void click(ServerPlayer player, Optional<RobotEntity> chosen, TabletUse.Click click) {
    if (chosen.isEmpty()) {
      say(player, Component.translatable(TabletKeys.DOCK_NO_CUBIT));
      return;
    }
    RobotMarks marks = chosen.get().workArea().work().marks();
    Optional<TabletUse.Block> block =
        click instanceof TabletUse.Block found ? Optional.of(found) : Optional.empty();
    if (player.isShiftKeyDown()) {
      boolean removed = marks.unmark(player, block.map(TabletUse.Block::pos));
      String key = removed ? TabletKeys.MARK_UNMARKED : TabletKeys.MARK_NONE;
      say(player, Component.translatable(key, chosen.get().getDisplayName()));
    } else if (block.isEmpty()) {
      say(player, Component.translatable(TabletKeys.MARK_ONLY_INVENTORY));
      return;
    } else {
      mark(player, chosen.get(), marks, block.get());
    }
    TabletChestView.send(player);
  }

  private static void mark(
      ServerPlayer player, RobotEntity robot, RobotMarks marks, TabletUse.Block block) {
    BlockPos pos = block.pos();
    Optional<UnloadChests.Mark> mark = marks.click(player, pos, block.face());
    if (mark.isEmpty()) {
      say(player, Component.translatable(TabletKeys.MARK_ONLY_INVENTORY));
      return;
    }
    String key =
        switch (mark.get()) {
          case ADDED -> TabletKeys.MARK_MARKED;
          case REMOVED -> TabletKeys.MARK_UNMARKED;
          case FULL -> TabletKeys.MARK_FULL;
        };
    say(player, Component.translatable(key, robot.getDisplayName(), marks.limit()));
  }

  private static void say(ServerPlayer player, Component text) {
    player.displayClientMessage(text, true);
  }
}
