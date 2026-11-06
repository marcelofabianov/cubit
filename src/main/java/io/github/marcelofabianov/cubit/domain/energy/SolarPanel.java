package io.github.marcelofabianov.cubit.domain.energy;

/** When the solar panel gives energy. The caller reads the world, and this decides. */
public final class SolarPanel {
  private static final long TICKS_PER_DAY = 24_000;
  private static final long DAY_ENDS_AT = 12_000;

  private SolarPanel() {}

  /** By the clock of the world and not by the sky light, because the weather has no effect. */
  public static boolean isDay(long dayTime) {
    return Math.floorMod(dayTime, TICKS_PER_DAY) < DAY_ENDS_AT;
  }

  public static Energy output(
      SolarOutput solar, PowerState state, boolean sunReachesIt, boolean day) {
    if (state != PowerState.ACTIVE || !sunReachesIt) {
      return Energy.ZERO;
    }
    return day ? solar.dayPerTick() : solar.nightPerTick();
  }
}
