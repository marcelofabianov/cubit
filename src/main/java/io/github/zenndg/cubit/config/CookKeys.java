package io.github.zenndg.cubit.config;

import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.tier.CookSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/** What one tier does with the card of the Cook. The key names are a contract (answer 1326). */
final class CookKeys {
  private static final String OVENS = "cook_ovens";
  private static final String COST = "cook_action_cost";
  private static final String PAUSE = "cook_action_pause_ticks";
  private static final String SCAN = "cook_scan_interval_ticks";
  private static final int MIN_ZERO = 0;
  private static final int MIN_ONE = 1;
  private static final int MAX_OVENS = 64;
  private static final int MAX_TICKS = 72000;

  private final ModConfigSpec.IntValue ovens;
  private final ModConfigSpec.IntValue cost;
  private final ModConfigSpec.IntValue pause;
  private final ModConfigSpec.IntValue scan;

  CookKeys(ModConfigSpec.Builder builder, CookSpec defaults) {
    ovens = builder.defineInRange(OVENS, defaults.ovens(), MIN_ZERO, MAX_OVENS);
    cost =
        builder.defineInRange(
            COST, (int) defaults.actionCost().amount(), MIN_ZERO, Integer.MAX_VALUE);
    pause = builder.defineInRange(PAUSE, defaults.actionPauseTicks(), MIN_ONE, MAX_TICKS);
    scan = builder.defineInRange(SCAN, defaults.scanIntervalTicks(), MIN_ONE, MAX_TICKS);
  }

  CookSpec read() {
    return new CookSpec(ovens.get(), new Energy(cost.get()), pause.get(), scan.get());
  }
}
