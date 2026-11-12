package io.github.marcelofabianov.cubit.domain.energy;

/** What the robot screen says about the solar panel, from the most to the least important. */
public enum SolarStatus {
  /** Inactive or in the base, the panel is off (energia.md). */
  OFF,
  /** The owner switched the panel off on the screen (answer 860). */
  SWITCHED_OFF,
  /** The Nether and the End have no sun. */
  NO_SUN,
  FULL,
  NO_SKY,
  /** Only the tiers with a night output charge at night. */
  NIGHT,
  CHARGING;

  public static SolarStatus of(SolarView view, Battery battery, SolarOutput output) {
    if (view.state() != PowerState.ACTIVE) {
      return OFF;
    }
    if (!view.switchedOn()) {
      return SWITCHED_OFF;
    }
    if (!view.sunExists()) {
      return NO_SUN;
    }
    if (battery.isFull()) {
      return FULL;
    }
    if (!view.skyOpen()) {
      return NO_SKY;
    }
    if (!view.day() && output.nightPerTick().isZero()) {
      return NIGHT;
    }
    return CHARGING;
  }

  /** What the caller read from the world. */
  public record SolarView(
      PowerState state, boolean sunExists, boolean skyOpen, boolean day, boolean switchedOn) {}
}
