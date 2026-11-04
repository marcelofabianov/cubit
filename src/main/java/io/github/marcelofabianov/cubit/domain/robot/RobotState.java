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
public record RobotState(RobotTier tier, Energy energy, float health) {
  private static final String INVALID_HEALTH_MESSAGE = "Invalid robot health: ";
  private static final float MIN_PLACED_HEALTH = 1f;

  public RobotState {
    if (Float.isNaN(health) || health < 0) {
      throw new IllegalArgumentException(INVALID_HEALTH_MESSAGE + health);
    }
  }

  public static RobotState initial(RobotTier tier, TierSpec spec, Percentage initialCharge) {
    return new RobotState(tier, spec.newBattery(initialCharge).charge(), spec.maxHealth());
  }

  public Battery batteryFor(TierSpec spec) {
    return new Battery(spec.batteryCapacity(), energy.min(spec.batteryCapacity()));
  }

  /** Never below the minimum: a robot placed without health would die immediately. */
  public float healthFor(TierSpec spec) {
    return Math.clamp(health, MIN_PLACED_HEALTH, (float) spec.maxHealth());
  }
}
