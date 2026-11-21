package io.github.zenndg.cubit.config;

import io.github.zenndg.cubit.domain.dock.DockSpec;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.tier.RobotTier;

/**
 * The default numbers of the Induction Dock at each tier, from _docs/tiers.md (answers 1374 and
 * 1385).
 */
final class DockDefaults {
  private static final long BASE_CAPACITY = 300_000;
  private static final long ADVANCED_CAPACITY = 1_200_000;
  private static final long ELITE_CAPACITY = 6_000_000;
  private static final long ULTIMATE_CAPACITY = 16_000_000;
  private static final long BASE_RECHARGE = 500;
  private static final long ADVANCED_RECHARGE = 1_000;
  private static final long ELITE_RECHARGE = 2_500;
  private static final long ULTIMATE_RECHARGE = 5_000;
  private static final int BASE_SLOTS = 27;
  private static final int ADVANCED_SLOTS = 36;
  private static final int ELITE_SLOTS = 45;
  private static final int ULTIMATE_SLOTS = 54;
  private static final long BASE_SOLAR = 25;
  private static final long ADVANCED_SOLAR = 100;
  private static final long ELITE_SOLAR = 500;
  private static final long ULTIMATE_SOLAR = 1_400;
  private static final DockSpec BASE = spec(BASE_CAPACITY, BASE_RECHARGE, BASE_SLOTS, BASE_SOLAR);
  private static final DockSpec ADVANCED =
      spec(ADVANCED_CAPACITY, ADVANCED_RECHARGE, ADVANCED_SLOTS, ADVANCED_SOLAR);
  private static final DockSpec ELITE =
      spec(ELITE_CAPACITY, ELITE_RECHARGE, ELITE_SLOTS, ELITE_SOLAR);
  private static final DockSpec ULTIMATE =
      spec(ULTIMATE_CAPACITY, ULTIMATE_RECHARGE, ULTIMATE_SLOTS, ULTIMATE_SOLAR);

  private DockDefaults() {}

  static DockSpec of(RobotTier tier) {
    return switch (tier) {
      case BASE -> BASE;
      case ADVANCED -> ADVANCED;
      case ELITE -> ELITE;
      case ULTIMATE -> ULTIMATE;
    };
  }

  private static DockSpec spec(long capacity, long recharge, int slots, long solar) {
    return new DockSpec(new Energy(capacity), new Energy(recharge), slots, new Energy(solar));
  }
}
