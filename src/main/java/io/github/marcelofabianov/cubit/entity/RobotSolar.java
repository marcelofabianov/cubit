package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.energy.SolarPanel;
import net.minecraft.world.level.Level;

/**
 * Fills the battery from the solar panel and tells the client when it does, for the pulse on the
 * cell. It runs after the sounds are decided, so the panel does not play the recharge sound, which
 * is for the base and the slot. Nothing here is saved, because it is recalculated.
 */
public final class RobotSolar {
  private final RobotEntity robot;
  private boolean sunReachesIt;
  private boolean wasActive;

  RobotSolar(RobotEntity robot) {
    this.robot = robot;
  }

  public static boolean isCharging(RobotEntity robot) {
    return robot.getEntityData().get(RobotSyncedData.SOLAR_CHARGING);
  }

  /** Only reading the sky waits for the interval (AGENTS.md, rule 21). */
  void tick() {
    boolean active = robot.powerState() == PowerState.ACTIVE;
    if (active && (!wasActive || robot.tickCount % CubitMod.CONFIG.solarCheckTicks() == 0)) {
      sunReachesIt = readSun();
    }
    wasActive = active;
    Level level = robot.level();
    Energy offered =
        SolarPanel.output(
            robot.spec().solar(),
            robot.powerState(),
            sunReachesIt,
            SolarPanel.isDay(level.getDayTime()));
    Battery.Charged charged = robot.battery().recharge(offered);
    robot.setBattery(charged.battery());
    robot.getEntityData().set(RobotSyncedData.SOLAR_CHARGING, !charged.accepted().isZero());
  }

  /** Light that passes through glass still counts, and the Nether and the End have no sun. */
  private boolean readSun() {
    Level level = robot.level();
    return level.dimensionType().hasSkyLight()
        && !level.dimensionType().hasCeiling()
        && level.canSeeSky(robot.blockPosition().above());
  }
}
