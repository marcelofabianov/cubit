package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the defense shared by all the tiers. The names are a contract. */
final class DefenseSection {
  private static final String RETALIATE_MEMORY = "retaliate_memory_ticks";
  private static final int RETALIATE_MEMORY_DEFAULT = 600;
  private static final int RETALIATE_MEMORY_MIN = 1;
  private static final int RETALIATE_MEMORY_MAX = 72000;

  private static final String FOCUS_FORGET = "focus_forget_distance_blocks";
  private static final int FOCUS_FORGET_DEFAULT = 32;
  private static final int FOCUS_FORGET_MIN = 1;
  private static final int FOCUS_FORGET_MAX = 128;

  private static final String SHOCK_LOOK = "shock_look_after_ticks";
  private static final int SHOCK_LOOK_DEFAULT = 60;
  private static final int SHOCK_LOOK_MIN = 1;
  private static final int SHOCK_LOOK_MAX = 1200;

  /** The aim of the Target app of the Tablet, as far as Focus keeps a target (answer 1043). */
  private static final String TABLET_TARGET_REACH = "tablet_target_reach_blocks";

  private static final int TABLET_TARGET_REACH_DEFAULT = 32;
  private static final int TABLET_TARGET_REACH_MIN = 1;
  private static final int TABLET_TARGET_REACH_MAX = 128;

  final ModConfigSpec.IntValue retaliateMemory;
  final ModConfigSpec.IntValue tabletTargetReach;
  final ModConfigSpec.IntValue shockLook;
  final ModConfigSpec.IntValue focusForget;

  DefenseSection(ModConfigSpec.Builder builder) {
    retaliateMemory =
        builder.defineInRange(
            RETALIATE_MEMORY, RETALIATE_MEMORY_DEFAULT, RETALIATE_MEMORY_MIN, RETALIATE_MEMORY_MAX);
    focusForget =
        builder.defineInRange(
            FOCUS_FORGET, FOCUS_FORGET_DEFAULT, FOCUS_FORGET_MIN, FOCUS_FORGET_MAX);
    shockLook =
        builder.defineInRange(SHOCK_LOOK, SHOCK_LOOK_DEFAULT, SHOCK_LOOK_MIN, SHOCK_LOOK_MAX);
    tabletTargetReach =
        builder.defineInRange(
            TABLET_TARGET_REACH,
            TABLET_TARGET_REACH_DEFAULT,
            TABLET_TARGET_REACH_MIN,
            TABLET_TARGET_REACH_MAX);
  }
}
