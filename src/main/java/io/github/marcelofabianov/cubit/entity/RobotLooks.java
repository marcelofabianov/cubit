package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.BatteryIndicator;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;

/**
 * Sends the client what the collar, the battery indicator and the shell depend on. The game only
 * sends a value when it changes. The tier and the indicator stay out of the saves because they are
 * recalculated, and {@link SavedRobotValues} saves the color.
 */
public final class RobotLooks {
  private RobotLooks() {}

  public static RobotTier tier(RobotEntity robot) {
    return RobotTier.values()[robot.getEntityData().get(RobotSyncedData.TIER)];
  }

  public static BatteryIndicator batteryIndicator(RobotEntity robot) {
    return BatteryIndicator.values()[robot.getEntityData().get(RobotSyncedData.BATTERY_INDICATOR)];
  }

  public static RobotColor color(RobotEntity robot) {
    return RobotColor.values()[robot.getEntityData().get(RobotSyncedData.COLOR)];
  }

  static void setColor(RobotEntity robot, RobotColor color) {
    robot.getEntityData().set(RobotSyncedData.COLOR, (byte) color.ordinal());
  }

  static void update(RobotEntity robot) {
    BatteryIndicator indicator =
        BatteryIndicator.of(robot.battery(), CubitMod.CONFIG.criticalCharge());
    robot.getEntityData().set(RobotSyncedData.TIER, (byte) robot.state().tier().ordinal());
    robot.getEntityData().set(RobotSyncedData.BATTERY_INDICATOR, (byte) indicator.ordinal());
  }
}
