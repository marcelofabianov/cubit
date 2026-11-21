package io.github.zenndg.cubit.config;

import io.github.zenndg.cubit.domain.energy.Energy;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The config keys of the Engineering Desk valid for every tier (answers 1435 and 1475). The names
 * are a contract.
 */
public final class EngineeringDeskSection {
  private static final String CRAFT_ENERGY = "engineering_desk_craft_energy";
  private static final int CRAFT_ENERGY_DEFAULT = 2_500;
  private static final String INPUT = "engineering_desk_input_per_tick";
  private static final int INPUT_DEFAULT = 5_000;
  private static final int MIN_ZERO = 0;
  private static final int MIN_POSITIVE = 1;

  private final ModConfigSpec.IntValue craftEnergy;
  private final ModConfigSpec.IntValue input;

  EngineeringDeskSection(ModConfigSpec.Builder builder) {
    craftEnergy =
        builder.defineInRange(CRAFT_ENERGY, CRAFT_ENERGY_DEFAULT, MIN_ZERO, Integer.MAX_VALUE);
    input = builder.defineInRange(INPUT, INPUT_DEFAULT, MIN_POSITIVE, Integer.MAX_VALUE);
  }

  /** What each finished craft costs, the same in every recipe and tier (answer 1460). */
  public Energy craftEnergy() {
    return new Energy(craftEnergy.get());
  }

  public Energy inputPerTick() {
    return new Energy(input.get());
  }
}
