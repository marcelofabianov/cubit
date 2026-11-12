package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** How often the robot looks for items, shared by all the tiers (answers 887 and 889). */
final class PickupSection {
  private static final String PICKUP_CHECK = "pickup_check_ticks";
  private static final int PICKUP_CHECK_DEFAULT = 5;
  private static final String MAGNET_CHECK = "magnet_check_ticks";
  private static final int MAGNET_CHECK_DEFAULT = 10;
  private static final int CHECK_MIN = 1;
  private static final int CHECK_MAX = 1200;

  final ModConfigSpec.IntValue pickupCheck;
  final ModConfigSpec.IntValue magnetCheck;

  PickupSection(ModConfigSpec.Builder builder) {
    pickupCheck = builder.defineInRange(PICKUP_CHECK, PICKUP_CHECK_DEFAULT, CHECK_MIN, CHECK_MAX);
    magnetCheck = builder.defineInRange(MAGNET_CHECK, MAGNET_CHECK_DEFAULT, CHECK_MIN, CHECK_MAX);
  }
}
