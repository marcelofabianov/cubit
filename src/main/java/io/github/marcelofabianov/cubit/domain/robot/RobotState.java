package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;

/**
 * What a robot keeps when it becomes an item. The config may change while a robot is stored, so the
 * numbers are only valid after being fitted to a {@link TierSpec}.
 */
public record RobotState(
    RobotTier tier,
    Energy energy,
    float health,
    boolean lampOn,
    RobotColor color,
    boolean scannersOn,
    OreFilter oreFilter) {
  private static final String INVALID_HEALTH_MESSAGE = "Invalid robot health: ";
  private static final float MIN_PLACED_HEALTH = 1f;

  public RobotState {
    if (Float.isNaN(health) || health < 0) {
      throw new IllegalArgumentException(INVALID_HEALTH_MESSAGE + health);
    }
  }

  /** The lamp starts switched on, also for what was saved before the lamp existed. */
  public RobotState(RobotTier tier, Energy energy, float health) {
    this(tier, energy, health, true);
  }

  /** The shell starts white, also for what was saved before the colors existed. */
  public RobotState(RobotTier tier, Energy energy, float health, boolean lampOn) {
    this(tier, energy, health, lampOn, RobotColor.WHITE);
  }

  /** The scanners start on and looking for nothing in particular, also for older saves. */
  public RobotState(RobotTier tier, Energy energy, float health, boolean lampOn, RobotColor color) {
    this(tier, energy, health, lampOn, color, true, OreFilter.EMPTY);
  }

  public static RobotState initial(RobotTier tier, TierSpec spec, Percentage initialCharge) {
    return new RobotState(tier, spec.newBattery(initialCharge).charge(), spec.maxHealth());
  }

  /** What a robot keeps when it dies: the tier and the looks, without energy or health. */
  public RobotState damaged() {
    return new RobotState(tier, Energy.ZERO, 0f, lampOn, color, scannersOn, oreFilter);
  }

  public RobotState repaired(TierSpec spec, Percentage health) {
    return new RobotState(
        tier,
        Energy.ZERO,
        (float) spec.maxHealth() * health.value() / Percentage.MAX,
        lampOn,
        color,
        scannersOn,
        oreFilter);
  }

  public Battery batteryFor(TierSpec spec) {
    return new Battery(spec.batteryCapacity(), energy.min(spec.batteryCapacity()));
  }

  /** Never below the minimum: a robot placed without health would die immediately. */
  public float healthFor(TierSpec spec) {
    return Math.clamp(health, MIN_PLACED_HEALTH, (float) spec.maxHealth());
  }
}
