package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.block.DockedRobot;
import io.github.zenndg.cubit.domain.behavior.RobotExpression;
import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.tier.RobotTier;

/** The robot as the dock screen reads it. Only reads, so the dock never changes the robot. */
public record RobotDockedView(RobotEntity robot) implements DockedRobot {
  @Override
  public Battery battery() {
    return robot.battery();
  }

  @Override
  public float health() {
    return robot.getHealth();
  }

  @Override
  public float maxHealth() {
    return robot.getMaxHealth();
  }

  @Override
  public Energy rechargePerTick() {
    return robot.spec().rechargePerTick();
  }

  @Override
  public RobotExpression expression() {
    return RobotFace.expression(robot);
  }

  @Override
  public RobotTier tier() {
    return robot.state().tier();
  }
}
