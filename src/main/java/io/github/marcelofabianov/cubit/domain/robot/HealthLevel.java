package io.github.marcelofabianov.cubit.domain.robot;

/** The health shown in a bar. Any health shows something, so the bar only empties at zero. */
public final class HealthLevel {
  private static final int FULL_PERCENT = 100;

  private HealthLevel() {}

  public static int percent(float health, float maxHealth) {
    if (health <= 0 || maxHealth <= 0) {
      return 0;
    }
    int rounded = (int) Math.ceil(health * FULL_PERCENT / maxHealth);
    return Math.min(rounded, FULL_PERCENT);
  }
}
