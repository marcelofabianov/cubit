package io.github.zenndg.cubit.domain.dock;

import io.github.zenndg.cubit.domain.tier.RobotTier;

/**
 * Gives the numbers of the Induction Dock at a tier. The domain defines it, the config serves it.
 */
public interface DockCatalog {
  DockSpec dock(RobotTier tier);
}
