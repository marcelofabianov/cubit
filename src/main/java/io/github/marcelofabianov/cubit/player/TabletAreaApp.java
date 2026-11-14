package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.AreaCheck;
import io.github.marcelofabianov.cubit.domain.robot.AreaCorner;
import io.github.marcelofabianov.cubit.domain.robot.AreaMarking;
import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotTablet;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

/**
 * The Area app: a click on an own Cubit chooses it, and two blocks after it are the corners of its
 * area, which it goes to at once (answers 1051, 1054 and 1060). Shift and the click cancel a
 * marking halfway, which lives only in the memory of the server.
 */
public final class TabletAreaApp {
  private static final Map<UUID, AreaMarking> MARKING = new ConcurrentHashMap<>();

  private TabletAreaApp() {}

  static void click(ServerPlayer player, TabletUse.Click click) {
    if (player.isShiftKeyDown()) {
      cancel(player);
    } else if (click instanceof TabletUse.Living living) {
      pick(player, living.entity());
    } else if (click instanceof TabletUse.Block block) {
      corner(player, block.pos());
    } else {
      say(player, Component.translatable(hintOf(MARKING.get(player.getUUID()))));
    }
    TabletAreaView.sync(player);
  }

  /** The marking halfway, for the box the client draws (answer 1056). */
  static Optional<AreaMarking> marking(ServerPlayer player) {
    return Optional.ofNullable(MARKING.get(player.getUUID()));
  }

  /** Switching the app or leaving the game drops the marking halfway (answer 1060). */
  static void forget(ServerPlayer player) {
    if (MARKING.remove(player.getUUID()) != null) {
      TabletAreaView.sync(player);
    }
  }

  private static void cancel(ServerPlayer player) {
    if (MARKING.remove(player.getUUID()) != null) {
      say(player, Component.translatable(TabletKeys.AREA_CANCELLED));
    }
  }

  private static void pick(ServerPlayer player, Entity entity) {
    if (!(entity instanceof RobotEntity robot)
        || !Ownership.owns(robot.ownerId(), player.getUUID())) {
      say(player, Component.translatable(TabletKeys.AREA_ONLY_YOURS));
      return;
    }
    if (!RobotTablet.areaOf(robot).unlocked()) {
      say(player, Component.translatable(TabletKeys.AREA_LOCKED, robot.getDisplayName()));
      return;
    }
    TabletSelection.choose(player, robot.getUUID());
    MARKING.put(player.getUUID(), AreaMarking.of(robot.getUUID()));
    say(player, Component.translatable(TabletKeys.AREA_FIRST_CORNER, robot.getDisplayName()));
  }

  private static void corner(ServerPlayer player, BlockPos pos) {
    AreaMarking marking = MARKING.get(player.getUUID());
    if (marking == null) {
      say(player, Component.translatable(TabletKeys.AREA_PICK_CUBIT));
      return;
    }
    Optional<RobotEntity> robot = RobotTablet.find(player, marking.robot());
    if (robot.isEmpty()) {
      MARKING.remove(player.getUUID());
      say(player, Component.translatable(TabletKeys.AREA_CUBIT_GONE));
      return;
    }
    AreaCorner corner = new AreaCorner(pos.getX(), pos.getY(), pos.getZ());
    if (marking.first().isEmpty()) {
      MARKING.put(player.getUUID(), marking.withFirst(corner));
      say(
          player,
          Component.translatable(
              TabletKeys.AREA_SECOND_CORNER, pos.getX(), pos.getY(), pos.getZ()));
      return;
    }
    close(
        player, robot.get(), WorkArea.between(marking.first().get(), corner, dimensionOf(player)));
  }

  /** A refused area keeps the first corner, so only the second one is clicked again. */
  private static void close(ServerPlayer player, RobotEntity robot, WorkArea area) {
    AreaCheck check = RobotTablet.checkArea(robot, area);
    if (check != AreaCheck.OK) {
      say(player, refusal(check, robot, area));
      return;
    }
    robot.workArea().assign(area, player);
    MARKING.remove(player.getUUID());
    say(
        player,
        Component.translatable(
            TabletKeys.AREA_SET,
            robot.getDisplayName(),
            area.width(),
            area.height(),
            area.depth()));
  }

  private static Component refusal(AreaCheck check, RobotEntity robot, WorkArea area) {
    return switch (check) {
      case TOO_BIG ->
          Component.translatable(
              TabletKeys.AREA_TOO_BIG,
              area.width(),
              area.height(),
              area.depth(),
              RobotTablet.areaOf(robot).sizeBlocks(),
              RobotTablet.areaOf(robot).heightBlocks());
      case OTHER_DIMENSION ->
          Component.translatable(TabletKeys.AREA_OTHER_DIMENSION, robot.getDisplayName());
      case OUT_OF_REACH ->
          Component.translatable(
              TabletKeys.AREA_OUT_OF_REACH, robot.getDisplayName(), robot.dockCallRadius());
      case LOCKED, OK -> Component.translatable(TabletKeys.AREA_LOCKED, robot.getDisplayName());
    };
  }

  private static String hintOf(AreaMarking marking) {
    if (marking == null) {
      return TabletKeys.AREA_PICK_CUBIT;
    }
    return marking.first().isEmpty() ? TabletKeys.AREA_FIRST_HINT : TabletKeys.AREA_SECOND_HINT;
  }

  static String dimensionOf(ServerPlayer player) {
    return player.level().dimension().location().toString();
  }

  private static void say(ServerPlayer player, Component text) {
    player.displayClientMessage(text, true);
  }
}
