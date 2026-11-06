package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the light of a robot. The names are a contract with the pack. */
final class LampSection {
  private static final String SCREEN_LIGHT = "screen_light_level";
  private static final int SCREEN_LIGHT_DEFAULT = 4;
  private static final String ON_BELOW = "lamp_on_below_light";
  private static final int ON_BELOW_DEFAULT = 8;
  private static final String OFF_FROM = "lamp_off_from_light";
  private static final int OFF_FROM_DEFAULT = 10;
  private static final int LIGHT_MIN = 0;
  private static final String CHECK_TICKS = "lamp_check_ticks";
  private static final int CHECK_TICKS_DEFAULT = 20;
  private static final int CHECK_TICKS_MIN = 1;
  private static final int CHECK_TICKS_MAX = 1200;

  final ModConfigSpec.IntValue screenLight;
  final ModConfigSpec.IntValue onBelow;
  final ModConfigSpec.IntValue offFrom;
  final ModConfigSpec.IntValue checkTicks;

  LampSection(ModConfigSpec.Builder builder) {
    screenLight =
        builder.defineInRange(SCREEN_LIGHT, SCREEN_LIGHT_DEFAULT, LIGHT_MIN, LightLevel.MAX);
    onBelow = builder.defineInRange(ON_BELOW, ON_BELOW_DEFAULT, LIGHT_MIN, LightLevel.MAX);
    offFrom = builder.defineInRange(OFF_FROM, OFF_FROM_DEFAULT, LIGHT_MIN, LightLevel.MAX);
    checkTicks =
        builder.defineInRange(CHECK_TICKS, CHECK_TICKS_DEFAULT, CHECK_TICKS_MIN, CHECK_TICKS_MAX);
  }
}
