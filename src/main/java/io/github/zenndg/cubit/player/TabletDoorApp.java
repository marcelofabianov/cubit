package io.github.zenndg.cubit.player;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.DoorMarks;
import io.github.zenndg.cubit.domain.robot.MarkedDoor;
import io.github.zenndg.cubit.entity.DoorBlocks;
import io.github.zenndg.cubit.menu.DoorKeys;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * The Doors app, for every Cubit of the player: a click on a door, a trapdoor or a fence gate marks
 * it, a click on a marked one unmarks it, and Shift and a click take out the clicked door, or else
 * the last one marked, as the Unload and Mark apps (answers 1364, 1365 and 1369).
 */
final class TabletDoorApp {
  private TabletDoorApp() {}

  static void click(ServerPlayer player, TabletUse.Click click) {
    SavedDoorMarks saved = SavedDoorMarks.of(player.server);
    DoorMarks marks = saved.marksOf(player.getUUID());
    Optional<MarkedDoor> door = doorAt(player, click);
    if (player.isShiftKeyDown()) {
      DoorMarks next = door.filter(marks::contains).map(marks::without).orElse(marks.withoutLast());
      saved.set(player.getUUID(), next);
      say(player, Component.translatable(next.equals(marks) ? DoorKeys.NONE : DoorKeys.UNMARKED));
    } else if (door.isEmpty()) {
      say(player, Component.translatable(DoorKeys.ONLY_DOOR));
      return;
    } else {
      int limit = CubitMod.CONFIG.doors().marksLimit();
      DoorMarks.Clicked clicked = marks.clicked(door.get(), limit);
      saved.set(player.getUUID(), clicked.marks());
      say(player, Component.translatable(keyOf(clicked.mark()), limit));
    }
    TabletChestView.send(player);
  }

  private static Optional<MarkedDoor> doorAt(ServerPlayer player, TabletUse.Click click) {
    if (!(click instanceof TabletUse.Block block)) {
      return Optional.empty();
    }
    String dimension = player.serverLevel().dimension().location().toString();
    return DoorBlocks.markedBlock(player.serverLevel(), block.pos())
        .map(BlockPos::asLong)
        .map(pos -> new MarkedDoor(pos, dimension));
  }

  private static String keyOf(DoorMarks.Mark mark) {
    return switch (mark) {
      case ADDED -> DoorKeys.MARKED;
      case REMOVED -> DoorKeys.UNMARKED;
      case FULL -> DoorKeys.FULL;
    };
  }

  private static void say(ServerPlayer player, Component text) {
    player.displayClientMessage(text, true);
  }
}
