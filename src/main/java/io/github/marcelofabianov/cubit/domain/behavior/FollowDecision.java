package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/** Decides how the following robot moves. The distances come from the config. */
public record FollowDecision(int stopDistanceBlocks, int teleportDistanceBlocks) {
  private static final String INVALID_MESSAGE = "Need 0 < stop < teleport: stop=%d, teleport=%d";

  public FollowDecision {
    if (stopDistanceBlocks <= 0 || teleportDistanceBlocks <= stopDistanceBlocks) {
      throw new IllegalArgumentException(
          INVALID_MESSAGE.formatted(stopDistanceBlocks, teleportDistanceBlocks));
    }
  }

  public FollowAction decide(PowerState state, double distanceBlocks, boolean sameDimension) {
    if (state != PowerState.ACTIVE || !sameDimension) {
      return FollowAction.STOP;
    }
    if (distanceBlocks >= teleportDistanceBlocks) {
      return FollowAction.TELEPORT;
    }
    return distanceBlocks > stopDistanceBlocks ? FollowAction.FOLLOW : FollowAction.STOP;
  }
}
