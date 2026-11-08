package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** Whether the robot that follows comes back with an owner who respawned. */
public final class RespawnReturn {
  private RespawnReturn() {}

  /**
   * Close by, in the same dimension, it walks or teleports by the usual rules. A robot on its way
   * to a dock keeps going, because the owner called it there.
   */
  public static boolean bringsBack(
      PowerState state,
      boolean goingToDock,
      boolean sameDimension,
      double distanceBlocks,
      int teleportDistanceBlocks) {
    if (state != PowerState.ACTIVE || goingToDock) {
      return false;
    }
    return !sameDimension || distanceBlocks >= teleportDistanceBlocks;
  }
}
