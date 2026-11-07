package io.github.marcelofabianov.cubit.entity;

import java.util.Optional;
import net.minecraft.core.BlockPos;

/** The last place on the ground, out of fire, lava and water, for the rescue from the void. */
final class RobotSafePosition {
  private final RobotEntity robot;
  private BlockPos position;

  RobotSafePosition(RobotEntity robot) {
    this.robot = robot;
  }

  void tick() {
    if (robot.onGround() && !robot.isOnFire() && !robot.isInLava() && !robot.isInWater()) {
      markHere();
    }
  }

  void markHere() {
    position = robot.blockPosition();
  }

  void restore(BlockPos saved) {
    position = saved;
  }

  Optional<BlockPos> position() {
    return Optional.ofNullable(position);
  }
}
