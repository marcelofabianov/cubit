package io.github.zenndg.cubit.domain.tier;

import io.github.zenndg.cubit.domain.robot.RobotType;

/** Gives the numbers of a robot type at a tier. The domain defines it, and the config serves it. */
public interface TierCatalog {
  TierSpec spec(RobotType type, RobotTier tier);
}
