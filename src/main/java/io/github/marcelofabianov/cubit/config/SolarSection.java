package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the solar panel for every robot. The names are a contract with the pack. */
final class SolarSection {
  private static final String CHECK_TICKS = "solar_check_ticks";
  private static final int CHECK_TICKS_DEFAULT = 20;
  private static final int CHECK_TICKS_MIN = 1;
  private static final int CHECK_TICKS_MAX = 1200;

  final ModConfigSpec.IntValue checkTicks;

  SolarSection(ModConfigSpec.Builder builder) {
    checkTicks =
        builder.defineInRange(CHECK_TICKS, CHECK_TICKS_DEFAULT, CHECK_TICKS_MIN, CHECK_TICKS_MAX);
  }
}
