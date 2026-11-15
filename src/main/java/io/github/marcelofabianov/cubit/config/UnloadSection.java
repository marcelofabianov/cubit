package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The unload interval of a Cubit in its area, the same on every tier: the default of a Cubit nobody
 * chose for, with zero for only when full, and the most the Work tab offers (answers 1252 and
 * 1254). The names are a contract with the pack.
 */
final class UnloadSection {
  private static final String DEFAULT = "unload_interval_default_minutes";
  private static final int DEFAULT_MINUTES = 5;
  private static final String MAX = "unload_interval_max_minutes";
  private static final int MAX_MINUTES = 30;
  private static final int MIN_ZERO = 0;
  private static final int MIN_ONE = 1;
  private static final int A_DAY = 1440;

  final ModConfigSpec.IntValue defaultMinutes;
  final ModConfigSpec.IntValue maxMinutes;

  UnloadSection(ModConfigSpec.Builder builder) {
    defaultMinutes = builder.defineInRange(DEFAULT, DEFAULT_MINUTES, MIN_ZERO, A_DAY);
    maxMinutes = builder.defineInRange(MAX, MAX_MINUTES, MIN_ONE, A_DAY);
  }
}
