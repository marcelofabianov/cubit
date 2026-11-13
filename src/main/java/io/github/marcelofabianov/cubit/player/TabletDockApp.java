package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * The Dock app: a click on a Dock of the player marks it as the preferred Dock of the chosen Cubit,
 * and a click on the marked one removes it. Anywhere else it only says what it does, and nothing
 * opens (answers 1045 and 1046).
 */
final class TabletDockApp {
  private TabletDockApp() {}

  static void click(ServerPlayer player, Optional<RobotEntity> chosen, TabletUse.Click click) {
    Optional<InductionDockBlockEntity> dock =
        click instanceof TabletUse.Block block
            ? block.dock().filter(found -> found.isOwner(player))
            : Optional.empty();
    if (dock.isEmpty()) {
      say(player, Component.translatable(TabletKeys.DOCK_ONLY_YOURS));
      return;
    }
    if (chosen.isEmpty()) {
      say(player, Component.translatable(TabletKeys.DOCK_NO_CUBIT));
      return;
    }
    RobotEntity robot = chosen.get();
    if (robot.docking().preferred().click(player, dock.get())) {
      String key =
          robot.docking().preferred().marked().isPresent()
              ? TabletKeys.DOCK_MARKED
              : TabletKeys.DOCK_UNMARKED;
      say(player, Component.translatable(key, robot.getDisplayName()));
    }
  }

  private static void say(ServerPlayer player, Component text) {
    player.displayClientMessage(text, true);
  }
}
