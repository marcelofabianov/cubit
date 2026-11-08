package io.github.marcelofabianov.cubit.domain.robot;

/**
 * How long the base takes to bring the health back to full, in whole minutes. The base heals the
 * whole bar in a fixed time from the config, so a smaller loss takes less time.
 */
public record HealTime(Kind kind, int minutes) {
  public static final int TICKS_PER_MINUTE = 1_200;
  private static final HealTime FULL = new HealTime(Kind.FULL, 0);
  private static final HealTime UNDER_A_MINUTE = new HealTime(Kind.UNDER_A_MINUTE, 0);
  private static final String NEGATIVE_MESSAGE = "Minutes must not be negative: ";

  public HealTime {
    if (minutes < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + minutes);
    }
  }

  /** Rounded to the nearest minute, as the screen says "about". */
  public static HealTime of(float health, float maxHealth, int fullHealTicks) {
    if (maxHealth <= 0 || health >= maxHealth) {
      return FULL;
    }
    float missing = (maxHealth - Math.max(health, 0)) / maxHealth;
    long ticksLeft = (long) Math.ceil(missing * Math.max(fullHealTicks, 0));
    if (ticksLeft < TICKS_PER_MINUTE) {
      return UNDER_A_MINUTE;
    }
    return new HealTime(Kind.MINUTES, Math.round((float) ticksLeft / TICKS_PER_MINUTE));
  }

  /** The three ways the screen writes it. */
  public enum Kind {
    FULL,
    UNDER_A_MINUTE,
    MINUTES
  }
}
