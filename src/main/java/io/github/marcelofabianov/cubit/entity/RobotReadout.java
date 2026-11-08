package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.energy.EnergyReading;

/**
 * What the server can tell about a robot to show elsewhere, such as in Jade. Only reads, so nothing
 * outside this package changes the robot.
 */
public final class RobotReadout {
  private RobotReadout() {}

  public static EnergyReading battery(RobotEntity robot) {
    return new EnergyReading(robot.battery().charge(), robot.battery().capacity());
  }
}
