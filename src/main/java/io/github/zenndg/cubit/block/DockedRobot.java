package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.domain.behavior.RobotExpression;
import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.tier.RobotTier;

/**
 * What the dock screen reads from the robot in the base. It comes as a capability, {@link
 * DockCapabilities#DOCKED_ROBOT}, so the dock never depends on a robot class (AGENTS.md, rule 42).
 */
public interface DockedRobot {
  Battery battery();

  float health();

  float maxHealth();

  RobotExpression expression();

  RobotTier tier();
}
