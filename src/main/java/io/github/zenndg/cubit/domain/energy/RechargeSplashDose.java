package io.github.zenndg.cubit.domain.energy;

/**
 * What a Recharge Splash still has to give one robot. It gives an equal part each tick, and the
 * last tick takes what is left, so the parts add up to the whole dose (answer 1415).
 */
public record RechargeSplashDose(Energy energyLeft, double healthLeft, int ticksLeft) {
  public static final RechargeSplashDose NONE = new RechargeSplashDose(Energy.ZERO, 0, 0);
  private static final int TICKS_PER_SECOND = 20;
  private static final String SEPARATOR = ", ";
  private static final String INVALID_MESSAGE = "Invalid Recharge Splash dose: ";

  public RechargeSplashDose {
    if (ticksLeft < 0 || !(healthLeft >= 0) || Double.isInfinite(healthLeft)) {
      throw new IllegalArgumentException(INVALID_MESSAGE + healthLeft + SEPARATOR + ticksLeft);
    }
  }

  public boolean isActive() {
    return ticksLeft > 0;
  }

  /** Rounded up, so the notice never says 0 s while the robot is still receiving. */
  public int secondsLeft() {
    return seconds(ticksLeft);
  }

  static int seconds(int ticks) {
    return (ticks + TICKS_PER_SECOND - 1) / TICKS_PER_SECOND;
  }

  public Step next() {
    if (!isActive()) {
      return new Step(Energy.ZERO, 0, NONE);
    }
    Energy energy = new Energy(energyLeft.amount() / ticksLeft);
    double health = healthLeft / ticksLeft;
    int rest = ticksLeft - 1;
    if (rest == 0) {
      return new Step(energyLeft, healthLeft, NONE);
    }
    return new Step(
        energy,
        health,
        new RechargeSplashDose(energyLeft.minus(energy), healthLeft - health, rest));
  }

  /** What one tick gives and what is left after it. */
  public record Step(Energy energy, double health, RechargeSplashDose rest) {}
}
