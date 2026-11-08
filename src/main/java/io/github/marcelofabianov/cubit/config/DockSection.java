package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the Induction Dock, valid for every robot. The names are a contract. */
final class DockSection {
  private static final String CAPACITY = "dock_energy_capacity";
  private static final int CAPACITY_DEFAULT = 100_000;
  private static final String INPUT = "dock_input_per_tick";
  private static final int INPUT_DEFAULT = 5_000;
  private static final String FULL_HEAL = "dock_full_heal_ticks";
  private static final int FULL_HEAL_DEFAULT = 1_200;
  private static final int FULL_HEAL_MAX = 72_000;
  private static final String STUCK = "dock_stuck_ticks";
  private static final int STUCK_DEFAULT = 100;
  private static final int STUCK_MAX = 1_200;
  private static final String COMPLETE_HOLD = "dock_complete_hold_ticks";
  private static final int COMPLETE_HOLD_DEFAULT = 60;
  private static final int COMPLETE_HOLD_MAX = 1_200;
  private static final int MIN = 1;

  final ModConfigSpec.IntValue capacity;
  final ModConfigSpec.IntValue input;
  final ModConfigSpec.IntValue fullHealTicks;
  final ModConfigSpec.IntValue stuckTicks;
  final ModConfigSpec.IntValue completeHoldTicks;

  DockSection(ModConfigSpec.Builder builder) {
    capacity = builder.defineInRange(CAPACITY, CAPACITY_DEFAULT, MIN, Integer.MAX_VALUE);
    input = builder.defineInRange(INPUT, INPUT_DEFAULT, MIN, Integer.MAX_VALUE);
    fullHealTicks = builder.defineInRange(FULL_HEAL, FULL_HEAL_DEFAULT, MIN, FULL_HEAL_MAX);
    stuckTicks = builder.defineInRange(STUCK, STUCK_DEFAULT, MIN, STUCK_MAX);
    completeHoldTicks =
        builder.defineInRange(COMPLETE_HOLD, COMPLETE_HOLD_DEFAULT, MIN, COMPLETE_HOLD_MAX);
  }
}
