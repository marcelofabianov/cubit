package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.FarmerSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * What one tier does with the card of the Farmer. The key names are a contract (answers 1226 and
 * 1237).
 */
final class FarmerKeys {
  private static final String COST = "farmer_action_cost";
  private static final String PAUSE = "farmer_action_pause_ticks";
  private static final String SPECIAL = "farmer_special_crops";
  private static final String BONE_MEAL = "farmer_bone_meal";
  private static final String FORTUNE = "farmer_fortune_level";
  private static final int MIN_ZERO = 0;
  private static final int MIN_PAUSE = 1;
  private static final int MAX_PAUSE = 72000;

  private final ModConfigSpec.IntValue cost;
  private final ModConfigSpec.IntValue pause;
  private final ModConfigSpec.BooleanValue special;
  private final ModConfigSpec.BooleanValue boneMeal;
  private final ModConfigSpec.IntValue fortune;

  FarmerKeys(ModConfigSpec.Builder builder, FarmerSpec defaults) {
    cost =
        builder.defineInRange(
            COST, (int) defaults.actionCost().amount(), MIN_ZERO, Integer.MAX_VALUE);
    pause = builder.defineInRange(PAUSE, defaults.actionPauseTicks(), MIN_PAUSE, MAX_PAUSE);
    special = builder.define(SPECIAL, defaults.specialCrops());
    boneMeal = builder.define(BONE_MEAL, defaults.boneMeal());
    fortune = builder.defineInRange(FORTUNE, defaults.fortune(), MIN_ZERO, FarmerSpec.MAX_FORTUNE);
  }

  FarmerSpec read() {
    return new FarmerSpec(
        new Energy(cost.get()), pause.get(), special.get(), boneMeal.get(), fortune.get());
  }
}
