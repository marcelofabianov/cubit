package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the energy slot, in the general section. The names are a contract. */
final class EnergySlotSection {
  private static final String DUST_ENERGY = "redstone_dust_energy";
  private static final int DUST_ENERGY_DEFAULT = 5_000;
  private static final String BLOCK_ENERGY = "redstone_block_energy";
  private static final int BLOCK_ENERGY_DEFAULT = 45_000;
  private static final int ENERGY_MIN = 1;

  final ModConfigSpec.IntValue dustEnergy;
  final ModConfigSpec.IntValue blockEnergy;

  EnergySlotSection(ModConfigSpec.Builder builder) {
    dustEnergy =
        builder.defineInRange(DUST_ENERGY, DUST_ENERGY_DEFAULT, ENERGY_MIN, Integer.MAX_VALUE);
    blockEnergy =
        builder.defineInRange(BLOCK_ENERGY, BLOCK_ENERGY_DEFAULT, ENERGY_MIN, Integer.MAX_VALUE);
  }
}
