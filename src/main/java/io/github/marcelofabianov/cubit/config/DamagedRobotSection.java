package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of a damaged robot, in the general section. The names are a contract. */
final class DamagedRobotSection {
  private static final String REPAIR_HEALTH = "repair_health_percent";
  private static final int REPAIR_HEALTH_DEFAULT = 25;
  private static final int REPAIR_HEALTH_MIN = 1;
  private static final int REPAIR_HEALTH_MAX = 100;
  private static final String RESCUE_RETRY = "void_rescue_retry_ticks";
  private static final int RESCUE_RETRY_DEFAULT = 20;
  private static final int RESCUE_RETRY_MIN = 1;
  private static final int RESCUE_RETRY_MAX = 1200;

  final ModConfigSpec.IntValue repairHealth;
  final ModConfigSpec.IntValue voidRescueRetry;

  DamagedRobotSection(ModConfigSpec.Builder builder) {
    repairHealth =
        builder.defineInRange(
            REPAIR_HEALTH, REPAIR_HEALTH_DEFAULT, REPAIR_HEALTH_MIN, REPAIR_HEALTH_MAX);
    voidRescueRetry =
        builder.defineInRange(
            RESCUE_RETRY, RESCUE_RETRY_DEFAULT, RESCUE_RETRY_MIN, RESCUE_RETRY_MAX);
  }
}
