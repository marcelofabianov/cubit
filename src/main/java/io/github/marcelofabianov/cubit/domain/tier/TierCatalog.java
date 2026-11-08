package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.robot.RobotType;

/** Gives the numbers of a robot type at a tier. The domain defines it, and the config serves it. */
public interface TierCatalog {
  TierSpec spec(RobotType type, RobotTier tier);
}
