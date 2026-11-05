package io.github.marcelofabianov.cubit.entity;

import net.minecraft.world.phys.Vec3;

/** Tells whether the robot really changed position since the last tick. */
final class RobotStepMeter {
  private static final double MIN_STEP_SQUARED = 1.0E-4;

  private Vec3 last;

  boolean moved(Vec3 position) {
    boolean moved = last != null && last.distanceToSqr(position) > MIN_STEP_SQUARED;
    last = position;
    return moved;
  }
}
