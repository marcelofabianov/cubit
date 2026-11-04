package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys valid for every robot. The key names are a contract with the pack. */
final class GeneralSection {
  private static final String INITIAL_CHARGE = "initial_charge_percent";
  private static final int INITIAL_CHARGE_DEFAULT = 25;
  private static final String WAKE_CHARGE = "wake_charge_percent";
  private static final int WAKE_CHARGE_DEFAULT = 5;
  private static final int PERCENT_MIN = 0;
  private static final int PERCENT_MAX = 100;
  private static final String BASE_SPEED = "base_movement_speed";
  private static final double BASE_SPEED_DEFAULT = 0.3;
  private static final double BASE_SPEED_MIN = 0.01;
  private static final double BASE_SPEED_MAX = 1;
  private static final String REPATH_TICKS = "follow_repath_ticks";
  private static final int REPATH_TICKS_DEFAULT = 10;
  private static final int REPATH_TICKS_MIN = 1;
  private static final int REPATH_TICKS_MAX = 200;
  private static final String STOP_DISTANCE = "stop_distance_blocks";
  private static final int STOP_DISTANCE_DEFAULT = 3;
  private static final int STOP_DISTANCE_MIN = 1;
  private static final int STOP_DISTANCE_MAX = 16;
  private static final String TELEPORT_DISTANCE = "teleport_distance_blocks";
  private static final int TELEPORT_DISTANCE_DEFAULT = 20;
  private static final int TELEPORT_DISTANCE_MIN = STOP_DISTANCE_MAX + 1;
  private static final int TELEPORT_DISTANCE_MAX = 128;

  final ModConfigSpec.IntValue initialCharge;
  final ModConfigSpec.IntValue wakeCharge;
  final ModConfigSpec.DoubleValue baseSpeed;
  final ModConfigSpec.IntValue repathTicks;
  final ModConfigSpec.IntValue stopDistance;
  final ModConfigSpec.IntValue teleportDistance;

  GeneralSection(ModConfigSpec.Builder builder) {
    initialCharge =
        builder.defineInRange(INITIAL_CHARGE, INITIAL_CHARGE_DEFAULT, PERCENT_MIN, PERCENT_MAX);
    wakeCharge = builder.defineInRange(WAKE_CHARGE, WAKE_CHARGE_DEFAULT, PERCENT_MIN, PERCENT_MAX);
    baseSpeed =
        builder.defineInRange(BASE_SPEED, BASE_SPEED_DEFAULT, BASE_SPEED_MIN, BASE_SPEED_MAX);
    repathTicks =
        builder.defineInRange(
            REPATH_TICKS, REPATH_TICKS_DEFAULT, REPATH_TICKS_MIN, REPATH_TICKS_MAX);
    stopDistance =
        builder.defineInRange(
            STOP_DISTANCE, STOP_DISTANCE_DEFAULT, STOP_DISTANCE_MIN, STOP_DISTANCE_MAX);
    teleportDistance =
        builder.defineInRange(
            TELEPORT_DISTANCE,
            TELEPORT_DISTANCE_DEFAULT,
            TELEPORT_DISTANCE_MIN,
            TELEPORT_DISTANCE_MAX);
  }
}
