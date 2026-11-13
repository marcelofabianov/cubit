package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.TorchSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/** The torches of one tier. The key names are a contract (answer 981). */
final class TorchKeys {
  private static final String COST = "torch_cost";
  private static final String LIGHT_LEVEL = "torch_light_level";
  private static final int MIN_ZERO = 0;
  private static final int MAX_ENERGY = Integer.MAX_VALUE;

  private final ModConfigSpec.IntValue cost;
  private final ModConfigSpec.IntValue lightLevel;

  TorchKeys(ModConfigSpec.Builder builder, TorchSpec defaults) {
    cost =
        builder.defineInRange(
            COST, Math.toIntExact(defaults.cost().amount()), MIN_ZERO, MAX_ENERGY);
    lightLevel =
        builder.defineInRange(LIGHT_LEVEL, defaults.lightLevel().value(), MIN_ZERO, LightLevel.MAX);
  }

  TorchSpec read() {
    return new TorchSpec(new Energy(cost.get()), new LightLevel(lightLevel.get()));
  }
}
