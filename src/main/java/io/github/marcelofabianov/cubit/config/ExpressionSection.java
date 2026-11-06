package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** The config keys of the expressions. The names are a contract with the pack. */
final class ExpressionSection {
  private static final String CRITICAL_CHARGE = "critical_charge_percent";
  private static final int CRITICAL_CHARGE_DEFAULT = 10;
  private static final int PERCENT_MIN = 0;
  private static final int PERCENT_MAX = 100;
  private static final String HAPPY_TICKS = "happy_expression_ticks";
  private static final int HAPPY_TICKS_DEFAULT = 60;
  private static final int HAPPY_TICKS_MIN = 1;
  private static final int HAPPY_TICKS_MAX = 1200;

  private static final String HURT_TICKS = "hurt_expression_ticks";
  private static final int HURT_TICKS_DEFAULT = 100;
  private static final String DYING_HEALTH = "dying_health_percent";
  private static final int DYING_HEALTH_DEFAULT = 10;

  private static final String WORRIED_TICKS = "worried_expression_ticks";
  private static final int WORRIED_TICKS_DEFAULT = 60;
  private static final String WORRIED_RADIUS = "worried_radius_blocks";
  private static final int WORRIED_RADIUS_DEFAULT = 16;
  private static final int WORRIED_RADIUS_MIN = 1;
  private static final int WORRIED_RADIUS_MAX = 128;

  final ModConfigSpec.IntValue worriedTicks;
  final ModConfigSpec.IntValue worriedRadius;
  private static final String DYING_TICKS = "dying_expression_ticks";
  private static final int DYING_TICKS_DEFAULT = 60;

  final ModConfigSpec.IntValue dyingTicks;
  final ModConfigSpec.IntValue criticalCharge;
  final ModConfigSpec.IntValue hurtTicks;
  final ModConfigSpec.IntValue dyingHealth;
  final ModConfigSpec.IntValue happyTicks;

  ExpressionSection(ModConfigSpec.Builder builder) {
    dyingTicks =
        builder.defineInRange(DYING_TICKS, DYING_TICKS_DEFAULT, HAPPY_TICKS_MIN, HAPPY_TICKS_MAX);
    worriedTicks =
        builder.defineInRange(
            WORRIED_TICKS, WORRIED_TICKS_DEFAULT, HAPPY_TICKS_MIN, HAPPY_TICKS_MAX);
    worriedRadius =
        builder.defineInRange(
            WORRIED_RADIUS, WORRIED_RADIUS_DEFAULT, WORRIED_RADIUS_MIN, WORRIED_RADIUS_MAX);
    hurtTicks =
        builder.defineInRange(HURT_TICKS, HURT_TICKS_DEFAULT, HAPPY_TICKS_MIN, HAPPY_TICKS_MAX);
    dyingHealth =
        builder.defineInRange(DYING_HEALTH, DYING_HEALTH_DEFAULT, PERCENT_MIN, PERCENT_MAX);
    criticalCharge =
        builder.defineInRange(CRITICAL_CHARGE, CRITICAL_CHARGE_DEFAULT, PERCENT_MIN, PERCENT_MAX);
    happyTicks =
        builder.defineInRange(HAPPY_TICKS, HAPPY_TICKS_DEFAULT, HAPPY_TICKS_MIN, HAPPY_TICKS_MAX);
  }
}
