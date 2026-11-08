package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config key of the weight of a full inventory. The name is a contract with the pack. */
final class LoadSection {
  private static final String FULL_INVENTORY_SPEED = "full_inventory_speed_multiplier";
  private static final double FULL_INVENTORY_SPEED_DEFAULT = 0.75;
  private static final double FULL_INVENTORY_SPEED_MIN = 0.1;
  private static final double FULL_INVENTORY_SPEED_MAX = 1;

  final ModConfigSpec.DoubleValue fullInventorySpeed;

  LoadSection(ModConfigSpec.Builder builder) {
    fullInventorySpeed =
        builder.defineInRange(
            FULL_INVENTORY_SPEED,
            FULL_INVENTORY_SPEED_DEFAULT,
            FULL_INVENTORY_SPEED_MIN,
            FULL_INVENTORY_SPEED_MAX);
  }
}
