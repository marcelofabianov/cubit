package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.RobotActivity;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;

/**
 * What happened to the energy of a robot in its last tick, for its screen (answer 698). It lives in
 * an attachment that is neither saved nor sent, and it is overwritten on every tick, without
 * allocating.
 */
public final class RobotTickReading {
  private static final RobotActivity STILL =
      new RobotActivity(false, false, false, false, 0, false);

  private Energy spent = Energy.ZERO;
  private RobotActivity activity = STILL;
  private boolean dockCharged;
  private boolean slotCharged;
  private boolean sunExists;
  private boolean skyOpen;
  private boolean day;
  private Energy solarCharged = Energy.ZERO;

  public static RobotTickReading of(RobotEntity robot) {
    return robot.getData(CubitAttachments.ROBOT_TICK_READING);
  }

  void recordUse(Energy spent, RobotActivity activity, boolean dockCharged, boolean slotCharged) {
    this.spent = spent;
    this.activity = activity;
    this.dockCharged = dockCharged;
    this.slotCharged = slotCharged;
  }

  void recordSolar(boolean sunExists, boolean skyOpen, boolean day, Energy charged) {
    this.sunExists = sunExists;
    this.skyOpen = skyOpen;
    this.day = day;
    this.solarCharged = charged;
  }

  public Energy spent() {
    return spent;
  }

  public RobotActivity activity() {
    return activity;
  }

  public boolean dockCharged() {
    return dockCharged;
  }

  public boolean slotCharged() {
    return slotCharged;
  }

  public boolean sunExists() {
    return sunExists;
  }

  public boolean skyOpen() {
    return skyOpen;
  }

  public boolean day() {
    return day;
  }

  public Energy solarCharged() {
    return solarCharged;
  }
}
