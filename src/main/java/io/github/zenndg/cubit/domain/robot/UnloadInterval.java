package io.github.zenndg.cubit.domain.robot;

/**
 * How often a Cubit in its area unloads before it is full, in minutes, chosen in the Work tab: zero
 * is only when full, as before, and a Cubit nobody chose for follows the default of the config
 * (answers 1250 to 1254). The config is read by the caller, so the domain stays free of it.
 */
public record UnloadInterval(int minutes) {
  /** Declared first, because the constructor checks it while building the constants below. */
  private static final int UNSET = -1;

  public static final UnloadInterval ONLY_WHEN_FULL = new UnloadInterval(0);

  /** Nobody chose yet, so the default of the config holds, and it may change with the pack. */
  public static final UnloadInterval CONFIG_DEFAULT = new UnloadInterval(UNSET);

  private static final int TICKS_PER_MINUTE = 1200;
  private static final String INVALID = "Invalid unload interval: ";

  public UnloadInterval {
    if (minutes < UNSET) {
      throw new IllegalArgumentException(INVALID + minutes);
    }
  }

  public boolean chosen() {
    return minutes != UNSET;
  }

  public int minutesOr(int configDefault) {
    return chosen() ? minutes : configDefault;
  }

  /** One step of the buttons − and +, from what holds now, between only when full and the max. */
  public UnloadInterval step(int direction, int configDefault, int max) {
    int next = minutesOr(configDefault) + Integer.signum(direction);
    return new UnloadInterval(Math.clamp(next, ONLY_WHEN_FULL.minutes(), max));
  }

  /** Time to go to the chest: the interval passed since the last unload, with something to take. */
  public boolean due(long ticksSinceLast, boolean somethingToUnload, int configDefault) {
    int resolved = minutesOr(configDefault);
    return resolved > 0
        && somethingToUnload
        && ticksSinceLast >= (long) resolved * TICKS_PER_MINUTE;
  }
}
