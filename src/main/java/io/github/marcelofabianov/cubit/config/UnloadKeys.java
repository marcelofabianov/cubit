package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.tier.UnloadSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The chests of one tier and how far they may be. The key names are a contract (1094). */
final class UnloadKeys {
  private static final String CHESTS = "unload_chests";
  private static final String REACH = "unload_reach_blocks";
  private static final int MIN_ZERO = 0;
  private static final int MIN_ONE = 1;
  private static final int MAX_CHESTS = 16;
  private static final int MAX_REACH = 128;

  private final ModConfigSpec.IntValue chests;
  private final ModConfigSpec.IntValue reach;

  UnloadKeys(ModConfigSpec.Builder builder, UnloadSpec defaults) {
    chests = builder.defineInRange(CHESTS, defaults.chests(), MIN_ZERO, MAX_CHESTS);
    reach =
        builder.defineInRange(REACH, Math.max(defaults.reachBlocks(), MIN_ONE), MIN_ONE, MAX_REACH);
  }

  UnloadSpec read() {
    return new UnloadSpec(chests.get(), reach.get());
  }
}
