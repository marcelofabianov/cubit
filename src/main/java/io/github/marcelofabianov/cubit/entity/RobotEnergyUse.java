package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.energy.RobotActivity;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;

/** Spends the battery on what the robot did in the tick and charges it from the energy slot. */
final class RobotEnergyUse {
  private final RobotEntity robot;
  private final RobotStepMeter stepMeter = new RobotStepMeter();

  RobotEnergyUse(RobotEntity robot) {
    this.robot = robot;
  }

  /** Being pushed, or a stuck route, is not walking, so it costs nothing. */
  void tick(Energy chargeBefore, boolean lampLit) {
    RobotInventory inventory = robot.robotInventory();
    RobotActivity activity = stepMeter.measure(robot, inventory.isFull(), lampLit);
    TierSpec spec = robot.spec();
    Battery spent = spec.spendOneTick(robot.battery(), robot.powerState(), activity);
    Battery charged = inventory.energySlot().charge(spent, spec.rechargePerTick());
    robot.setBattery(charged);
    PowerState next = CubitMod.CONFIG.powerRules().afterEnergyChange(robot.powerState(), charged);
    if (next != robot.powerState()) {
      robot.setPowerState(next);
      robot.getNavigation().stop();
    }
    RobotAudio.update(robot, activity.walking(), chargeBefore);
  }
}
