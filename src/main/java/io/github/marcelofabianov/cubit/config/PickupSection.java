package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** How often the robot looks for items, shared by all the tiers (answers 887, 889 and 943). */
final class PickupSection {
  private static final String PICKUP_CHECK = "pickup_check_ticks";
  private static final int PICKUP_CHECK_DEFAULT = 5;
  private static final String MAGNET_CHECK = "magnet_check_ticks";
  private static final int MAGNET_CHECK_DEFAULT = 10;
  private static final String FETCH_CHECK = "fetch_check_ticks";
  private static final int FETCH_CHECK_DEFAULT = 20;
  private static final String FETCH_GIVE_UP = "fetch_give_up_ticks";
  private static final int FETCH_GIVE_UP_DEFAULT = 100;
  private static final String FETCH_IGNORE = "fetch_ignore_ticks";
  private static final int FETCH_IGNORE_DEFAULT = 200;
  private static final int CHECK_MIN = 1;
  private static final int CHECK_MAX = 1200;

  final ModConfigSpec.IntValue pickupCheck;
  final ModConfigSpec.IntValue magnetCheck;
  final ModConfigSpec.IntValue fetchCheck;
  final ModConfigSpec.IntValue fetchGiveUp;
  final ModConfigSpec.IntValue fetchIgnore;

  PickupSection(ModConfigSpec.Builder builder) {
    pickupCheck = builder.defineInRange(PICKUP_CHECK, PICKUP_CHECK_DEFAULT, CHECK_MIN, CHECK_MAX);
    magnetCheck = builder.defineInRange(MAGNET_CHECK, MAGNET_CHECK_DEFAULT, CHECK_MIN, CHECK_MAX);
    fetchCheck = builder.defineInRange(FETCH_CHECK, FETCH_CHECK_DEFAULT, CHECK_MIN, CHECK_MAX);
    fetchGiveUp = builder.defineInRange(FETCH_GIVE_UP, FETCH_GIVE_UP_DEFAULT, CHECK_MIN, CHECK_MAX);
    fetchIgnore = builder.defineInRange(FETCH_IGNORE, FETCH_IGNORE_DEFAULT, CHECK_MIN, CHECK_MAX);
  }
}
