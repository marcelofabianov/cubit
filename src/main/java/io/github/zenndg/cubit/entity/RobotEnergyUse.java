package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.PowerState;
import io.github.zenndg.cubit.domain.energy.RobotActivity;
import io.github.zenndg.cubit.domain.tier.TierSpec;

/**
 * Spends the battery on what the robot did in the tick and charges it from the energy slot and from
 * a Recharge Splash, before the power rules, so a robot without charge can wake from either.
 */
final class RobotEnergyUse {
  private final RobotEntity robot;
  private final RobotStepMeter stepMeter = new RobotStepMeter();

  RobotEnergyUse(RobotEntity robot) {
    this.robot = robot;
  }

  /**
   * Being pushed, or a stuck route, is not walking, so it costs nothing. The charge before the tick
   * comes before the dock, so a rise up to here came from the dock.
   */
  void tick(Energy chargeBefore, boolean lampLit) {
    RobotInventory inventory = robot.robotInventory();
    RobotActivity activity = stepMeter.measure(robot, inventory.isFull(), lampLit);
    TierSpec spec = robot.spec();
    Battery before = robot.battery();
    Battery spent = spec.spendOneTick(before, robot.powerState(), activity);
    Battery fromSlot = inventory.energySlot().charge(spent, spec.rechargePerTick());
    Battery charged = RobotRechargeSplash.tick(robot, fromSlot);
    robot.setBattery(charged);
    RobotTickReading.of(robot)
        .recordUse(
            before.charge().minus(spent.charge()),
            activity,
            before.charge().amount() > chargeBefore.amount(),
            fromSlot.charge().amount() > spent.charge().amount());
    PowerState next = CubitMod.CONFIG.powerRules().afterEnergyChange(robot.powerState(), charged);
    if (next != robot.powerState()) {
      robot.setPowerState(next);
      robot.getNavigation().stop();
    }
    RobotAudio.update(robot, activity.walking(), chargeBefore);
  }
}
