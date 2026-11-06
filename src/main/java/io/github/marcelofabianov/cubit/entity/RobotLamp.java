package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.LampPolicy;
import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.behavior.RobotLight;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.menu.LampSwitch;

/**
 * Decides on the server whether the lamp is lit and how much light the robot gives, and sends it to
 * the client, because the config does not reach it. The lit state stays out of the saves because it
 * is recalculated.
 */
public final class RobotLamp implements LampSwitch {
  private final RobotEntity robot;
  private boolean switchedOn = true;
  private boolean lit;
  private boolean wasAllowed;

  RobotLamp(RobotEntity robot) {
    this.robot = robot;
  }

  public static boolean isLit(RobotEntity robot) {
    return robot.getEntityData().get(RobotSyncedData.LAMP_LIT);
  }

  public static int lightLevel(RobotEntity robot) {
    return robot.getEntityData().get(RobotSyncedData.LIGHT_LEVEL);
  }

  boolean lit() {
    return lit;
  }

  boolean switchedOn() {
    return switchedOn;
  }

  void setSwitchedOn(boolean switchedOn) {
    this.switchedOn = switchedOn;
  }

  @Override
  public boolean isOn() {
    return switchedOn;
  }

  @Override
  public boolean isAvailable() {
    return robot.powerState() != PowerState.HIBERNATING;
  }

  @Override
  public void toggle() {
    switchedOn = !switchedOn;
  }

  /**
   * The state and the switch count at once, so the lamp never stays lit on a robot that is off.
   * Only reading the light of the world waits for the interval (AGENTS.md, rule 21).
   */
  void tick() {
    boolean allowed = LampPolicy.allows(switchedOn, robot.powerState());
    if (!allowed) {
      lit = false;
    } else if (!wasAllowed || robot.tickCount % CubitMod.CONFIG.lampCheckTicks() == 0) {
      lit = readLit();
    }
    wasAllowed = allowed;
    LightLevel level =
        RobotLight.level(
            RobotFace.expression(robot).glows(),
            CubitMod.CONFIG.screenLight(),
            lit,
            robot.spec().lampLight());
    robot.getEntityData().set(RobotSyncedData.LAMP_LIT, lit);
    robot.getEntityData().set(RobotSyncedData.LIGHT_LEVEL, (byte) level.value());
  }

  /** The dynamic light only exists on the client, so this never sees the lamp itself. */
  private boolean readLit() {
    LightLevel ambient =
        new LightLevel(robot.level().getMaxLocalRawBrightness(robot.blockPosition()));
    return CubitMod.CONFIG.lampPolicy().staysLit(switchedOn, robot.powerState(), ambient, lit);
  }
}
