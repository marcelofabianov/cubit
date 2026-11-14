package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.tier.AreaSpec;

/**
 * Whether the two corners make an area the chosen Cubit can take, or why not: the tier, the size,
 * the dimension and the reach (answers 1050, 1054 and 1058).
 */
public enum AreaCheck {
  OK,
  LOCKED,
  TOO_BIG,
  OTHER_DIMENSION,
  OUT_OF_REACH;

  /** Where the Cubit is now, and the reach of its tier. */
  public record Place(String dimension, double x, double z, int reach) {}

  public static AreaCheck of(WorkArea area, AreaSpec spec, Place robot) {
    if (!spec.unlocked()) {
      return LOCKED;
    }
    if (!area.fits(spec)) {
      return TOO_BIG;
    }
    if (!area.isIn(robot.dimension())) {
      return OTHER_DIMENSION;
    }
    return area.centerWithin(robot.x(), robot.z(), robot.reach()) ? OK : OUT_OF_REACH;
  }
}
