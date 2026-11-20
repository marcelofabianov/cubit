package io.github.zenndg.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The speed control of every Cubit, the same on every tier: the lowest percentage of the speed of
 * the tier and how much each press of − and + moves it (answer 1267). The top is always 100%. The
 * names are a contract with the pack.
 */
final class SpeedControlSection {
  private static final String MIN = "speed_control_min_percent";
  private static final int MIN_DEFAULT = 50;
  private static final int MIN_LOWEST = 10;
  private static final int MIN_HIGHEST = 100;
  private static final String STEP = "speed_control_step_percent";
  private static final int STEP_DEFAULT = 10;
  private static final int STEP_LOWEST = 1;
  private static final int STEP_HIGHEST = 50;

  final ModConfigSpec.IntValue minPercent;
  final ModConfigSpec.IntValue stepPercent;

  SpeedControlSection(ModConfigSpec.Builder builder) {
    minPercent = builder.defineInRange(MIN, MIN_DEFAULT, MIN_LOWEST, MIN_HIGHEST);
    stepPercent = builder.defineInRange(STEP, STEP_DEFAULT, STEP_LOWEST, STEP_HIGHEST);
  }
}
