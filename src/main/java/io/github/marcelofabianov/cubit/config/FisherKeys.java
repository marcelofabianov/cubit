package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.FisherSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * What one tier does with the card of the Fisher: Lure, Luck of the Sea and the cost of each fish.
 * The key names are a contract (answers 1185 and 1190).
 */
final class FisherKeys {
  private static final String LURE = "fisher_lure_level";
  private static final String LUCK = "fisher_luck_level";
  private static final String COST = "fisher_catch_cost";
  private static final int MIN_ZERO = 0;

  private final ModConfigSpec.IntValue lure;
  private final ModConfigSpec.IntValue luck;
  private final ModConfigSpec.IntValue cost;

  FisherKeys(ModConfigSpec.Builder builder, FisherSpec defaults) {
    lure = builder.defineInRange(LURE, defaults.lureLevel(), MIN_ZERO, FisherSpec.MAX_LEVEL);
    luck = builder.defineInRange(LUCK, defaults.luckLevel(), MIN_ZERO, FisherSpec.MAX_LEVEL);
    cost =
        builder.defineInRange(
            COST, (int) defaults.catchCost().amount(), MIN_ZERO, Integer.MAX_VALUE);
  }

  FisherSpec read() {
    return new FisherSpec(lure.get(), luck.get(), new Energy(cost.get()));
  }
}
