package io.github.zenndg.cubit.domain.energy;

/**
 * The numbers of the Recharge Splash, the same on every tier: the share of the health and of the
 * battery it gives, its radius and the ticks it takes to give them (answers 1405, 1412 and 1414).
 */
public record RechargeSplashSpec(
    Percentage health, Percentage energy, int radiusBlocks, int durationTicks) {
  private static final String SEPARATOR = ", ";
  private static final String NOT_POSITIVE_MESSAGE = "Recharge Splash numbers must be positive: ";

  public RechargeSplashSpec {
    if (radiusBlocks <= 0 || durationTicks <= 0) {
      throw new IllegalArgumentException(
          NOT_POSITIVE_MESSAGE + radiusBlocks + SEPARATOR + durationTicks);
    }
  }

  /** The energy is rounded down, so the splash never gives a fraction of FE (rule 13). */
  public RechargeSplashDose doseFor(Energy batteryCapacity, int maxHealth) {
    Energy dose = new Energy(batteryCapacity.amount() * energy.value() / Percentage.MAX);
    double healthDose = (double) maxHealth * health.value() / Percentage.MAX;
    return new RechargeSplashDose(dose, healthDose, durationTicks);
  }

  /** Rounded up, as the seconds of the notice, for the tooltip of the item. */
  public int durationSeconds() {
    return RechargeSplashDose.seconds(durationTicks);
  }

  /** A sphere around the impact, edge included, as the splash potion of the game. */
  public boolean reaches(double distanceSquared) {
    return distanceSquared <= (double) radiusBlocks * radiusBlocks;
  }
}
