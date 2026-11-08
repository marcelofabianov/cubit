package io.github.marcelofabianov.cubit.block;

import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;

/**
 * What the dock screen reads from the robot in the base. It comes as a capability, {@link
 * DockCapabilities#DOCKED_ROBOT}, so the dock never depends on a robot class (AGENTS.md, rule 42).
 */
public interface DockedRobot {
  Battery battery();

  float health();

  float maxHealth();

  Energy rechargePerTick();

  RobotExpression expression();

  RobotTier tier();
}
