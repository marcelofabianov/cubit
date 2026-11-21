package io.github.zenndg.cubit.domain.desk;

import io.github.zenndg.cubit.domain.tier.RobotTier;

/**
 * Gives the numbers of the Engineering Desk at a tier. The domain defines it, the config serves it.
 */
public interface DeskCatalog {
  DeskSpec desk(RobotTier tier);
}
