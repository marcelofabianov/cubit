package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.BatteryIndicator;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;

/**
 * Sends the client what the collar and the battery indicator depend on. It stays out of the saves
 * because it is recalculated, and the game only sends a value when it changes.
 */
public final class RobotLooks {
  private RobotLooks() {}

  public static RobotTier tier(RobotEntity robot) {
    return RobotTier.values()[robot.getEntityData().get(RobotSyncedData.TIER)];
  }

  public static BatteryIndicator batteryIndicator(RobotEntity robot) {
    return BatteryIndicator.values()[robot.getEntityData().get(RobotSyncedData.BATTERY_INDICATOR)];
  }

  static void update(RobotEntity robot) {
    BatteryIndicator indicator =
        BatteryIndicator.of(robot.battery(), CubitMod.CONFIG.criticalCharge());
    robot.getEntityData().set(RobotSyncedData.TIER, (byte) robot.state().tier().ordinal());
    robot.getEntityData().set(RobotSyncedData.BATTERY_INDICATOR, (byte) indicator.ordinal());
  }
}
