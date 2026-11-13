package io.github.marcelofabianov.cubit.domain.energy;

import java.util.Optional;

/**
 * Recharging a stored robot with a Power Bank in the crafting grid (answers 180 to 183): only below
 * the wake charge, only what is missing, and only when the bank has all of it.
 */
public final class WakeRecharge {
  private WakeRecharge() {}

  public static Optional<Result> of(Battery robot, Percentage wake, Battery bank) {
    Energy target = wakeCharge(robot.capacity(), wake);
    if (robot.charge().amount() >= target.amount()) {
      return Optional.empty();
    }
    Energy missing = target.minus(robot.charge());
    if (bank.charge().amount() < missing.amount()) {
      return Optional.empty();
    }
    EnergyTransfer moved = EnergyTransfer.move(bank, robot, missing);
    return Optional.of(new Result(moved.to(), moved.from()));
  }

  /** Rounded up, so the robot is never one FE short of waking. */
  private static Energy wakeCharge(Energy capacity, Percentage wake) {
    long scaled = capacity.amount() * wake.value();
    return new Energy((scaled + Percentage.MAX - 1) / Percentage.MAX);
  }

  /** The robot and the bank after the recharge. */
  public record Result(Battery robot, Battery bank) {}
}
