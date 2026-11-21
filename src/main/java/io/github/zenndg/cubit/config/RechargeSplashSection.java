package io.github.zenndg.cubit.config;

import io.github.zenndg.cubit.domain.energy.Percentage;
import io.github.zenndg.cubit.domain.energy.RechargeSplashSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The Recharge Splash, the same on every tier (answers 1405, 1412 and 1414). The names are a
 * contract with the pack.
 */
public final class RechargeSplashSection {
  private static final String HEALTH = "recharge_splash_health_percent";
  private static final int HEALTH_DEFAULT = 20;
  private static final String ENERGY = "recharge_splash_energy_percent";
  private static final int ENERGY_DEFAULT = 15;
  private static final int PERCENT_MIN = 0;
  private static final String RADIUS = "recharge_splash_radius_blocks";
  private static final int RADIUS_DEFAULT = 4;
  private static final int RADIUS_MIN = 1;
  private static final int RADIUS_MAX = 16;
  private static final String DURATION = "recharge_splash_duration_ticks";
  private static final int DURATION_DEFAULT = 60;
  private static final int DURATION_MIN = 1;
  private static final int DURATION_MAX = 1200;

  private final ModConfigSpec.IntValue health;
  private final ModConfigSpec.IntValue energy;
  private final ModConfigSpec.IntValue radius;
  private final ModConfigSpec.IntValue duration;

  RechargeSplashSection(ModConfigSpec.Builder builder) {
    health = builder.defineInRange(HEALTH, HEALTH_DEFAULT, PERCENT_MIN, Percentage.MAX);
    energy = builder.defineInRange(ENERGY, ENERGY_DEFAULT, PERCENT_MIN, Percentage.MAX);
    radius = builder.defineInRange(RADIUS, RADIUS_DEFAULT, RADIUS_MIN, RADIUS_MAX);
    duration = builder.defineInRange(DURATION, DURATION_DEFAULT, DURATION_MIN, DURATION_MAX);
  }

  /** The ranges of the keys keep every value valid for the domain. */
  public RechargeSplashSpec spec() {
    return new RechargeSplashSpec(
        new Percentage(health.get()), new Percentage(energy.get()), radius.get(), duration.get());
  }
}
