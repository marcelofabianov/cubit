package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the defense shared by all the tiers. The names are a contract. */
final class DefenseSection {
  private static final String RETALIATE_MEMORY = "retaliate_memory_ticks";
  private static final int RETALIATE_MEMORY_DEFAULT = 600;
  private static final int RETALIATE_MEMORY_MIN = 1;
  private static final int RETALIATE_MEMORY_MAX = 72000;

  final ModConfigSpec.IntValue retaliateMemory;

  DefenseSection(ModConfigSpec.Builder builder) {
    retaliateMemory =
        builder.defineInRange(
            RETALIATE_MEMORY, RETALIATE_MEMORY_DEFAULT, RETALIATE_MEMORY_MIN, RETALIATE_MEMORY_MAX);
  }
}
