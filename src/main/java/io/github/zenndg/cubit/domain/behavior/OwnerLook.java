package io.github.zenndg.cubit.domain.behavior;

import io.github.zenndg.cubit.domain.energy.PowerState;

/** When a robot turns to its owner. The distance is a constant, because this is only visual. */
public final class OwnerLook {
  private static final double RADIUS_BLOCKS = 8;

  private OwnerLook() {}

  /** The comparison is false for an invalid distance, so the robot just does not turn. */
  public static boolean applies(PowerState state, boolean sameDimension, double distanceBlocks) {
    return state == PowerState.ACTIVE && sameDimension && distanceBlocks <= RADIUS_BLOCKS;
  }
}
