package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.player.RobotSnapshot;
import java.util.Optional;
import java.util.UUID;

/** Reads a loaded Cubit for the Tablet, the same way its screen reads it (answer 1023). */
public final class RobotSnapshotReader {
  private static final int PERCENT = 100;
  private static final String NO_NAME = "";

  private RobotSnapshotReader() {}

  /** Empty for a Cubit without an owner, which no Tablet lists. */
  public static Optional<RobotSnapshot> of(RobotEntity robot) {
    Optional<UUID> owner = robot.ownerId();
    if (owner.isEmpty()) {
      return Optional.empty();
    }
    Battery battery = robot.battery();
    boolean following = RobotInteractions.followedPlayer(robot) != null;
    RobotSnapshot.RobotLook look =
        new RobotSnapshot.RobotLook(
            robot.state().tier(), robot.state().color(), RobotFace.expression(robot));
    return Optional.of(
        new RobotSnapshot(
            robot.getUUID(),
            owner.get(),
            robot.hasCustomName() ? robot.getCustomName().getString() : NO_NAME,
            look,
            RobotScreenStatus.of(
                robot.powerState(),
                following,
                robot.workArea().work(),
                battery,
                CubitMod.CONFIG.criticalCharge()),
            percentOf(battery),
            robot.blockPosition(),
            robot.level().dimension(),
            robot.level().getGameTime()));
  }

  /** Whether the robot follows its owner now, which only a loaded robot can tell. */
  public static boolean following(RobotEntity robot) {
    return RobotInteractions.followedPlayer(robot) != null;
  }

  private static int percentOf(Battery battery) {
    long capacity = battery.capacity().amount();
    return capacity == 0 ? 0 : (int) (battery.charge().amount() * PERCENT / capacity);
  }
}
