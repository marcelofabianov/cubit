package io.github.zenndg.cubit.config;

import io.github.zenndg.cubit.domain.desk.DeskSpec;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.tier.RobotTier;

/**
 * The default numbers of the Engineering Desk at each tier, from _docs/tiers.md (answers 1433 and
 * 1434): 5, 4, 2.5 and 1.5 seconds per craft.
 */
final class DeskDefaults {
  private static final long BASE_CAPACITY = 100_000;
  private static final long ADVANCED_CAPACITY = 400_000;
  private static final long ELITE_CAPACITY = 2_000_000;
  private static final long ULTIMATE_CAPACITY = 8_000_000;
  private static final int BASE_TICKS = 100;
  private static final int ADVANCED_TICKS = 80;
  private static final int ELITE_TICKS = 50;
  private static final int ULTIMATE_TICKS = 30;
  private static final int BASE_SLOTS = 12;
  private static final int ADVANCED_SLOTS = 18;
  private static final int ELITE_SLOTS = 27;
  private static final int ULTIMATE_SLOTS = 36;
  private static final int BASE_ORDER = 8;
  private static final int ADVANCED_ORDER = 32;
  private static final int ELITE_ORDER = 64;
  private static final int ULTIMATE_ORDER = 256;

  private static final DeskSpec BASE = spec(BASE_CAPACITY, BASE_TICKS, BASE_SLOTS, BASE_ORDER);
  private static final DeskSpec ADVANCED =
      spec(ADVANCED_CAPACITY, ADVANCED_TICKS, ADVANCED_SLOTS, ADVANCED_ORDER);
  private static final DeskSpec ELITE = spec(ELITE_CAPACITY, ELITE_TICKS, ELITE_SLOTS, ELITE_ORDER);
  private static final DeskSpec ULTIMATE =
      spec(ULTIMATE_CAPACITY, ULTIMATE_TICKS, ULTIMATE_SLOTS, ULTIMATE_ORDER);

  private DeskDefaults() {}

  static DeskSpec of(RobotTier tier) {
    return switch (tier) {
      case BASE -> BASE;
      case ADVANCED -> ADVANCED;
      case ELITE -> ELITE;
      case ULTIMATE -> ULTIMATE;
    };
  }

  private static DeskSpec spec(long capacity, int ticks, int slots, int order) {
    return new DeskSpec(new Energy(capacity), ticks, slots, order);
  }
}
