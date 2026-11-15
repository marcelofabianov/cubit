package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The config keys of the jobs that are the same on every tier: the time of a catch, as in the game,
 * the reach of the cast and the scan of the Farmer (answers 1184, 1208 and 1237). The names are a
 * contract with the pack.
 */
final class FisherSection {
  private static final String WAIT_MIN = "fisher_wait_min_ticks";
  private static final int WAIT_MIN_DEFAULT = 100;
  private static final String WAIT_MAX = "fisher_wait_max_ticks";
  private static final int WAIT_MAX_DEFAULT = 600;
  private static final String BITE_MIN = "fisher_bite_min_ticks";
  private static final int BITE_MIN_DEFAULT = 20;
  private static final String BITE_MAX = "fisher_bite_max_ticks";
  private static final int BITE_MAX_DEFAULT = 80;
  private static final String LURE_TICKS = "fisher_lure_ticks_per_level";
  private static final int LURE_TICKS_DEFAULT = 100;
  private static final String CAST_REACH = "fisher_cast_reach_blocks";
  private static final String FARMER_SCAN = "farmer_scan_interval_ticks";
  private static final int FARMER_SCAN_DEFAULT = 40;
  private static final int CAST_REACH_DEFAULT = 6;
  private static final int CAST_REACH_MAX = 16;
  private static final int TICKS_MIN = 1;
  private static final int LURE_TICKS_MIN = 0;
  private static final int TICKS_MAX = 72000;

  final ModConfigSpec.IntValue waitMin;
  final ModConfigSpec.IntValue waitMax;
  final ModConfigSpec.IntValue biteMin;
  final ModConfigSpec.IntValue biteMax;
  final ModConfigSpec.IntValue lureTicks;
  final ModConfigSpec.IntValue castReach;
  final ModConfigSpec.IntValue farmerScan;

  FisherSection(ModConfigSpec.Builder builder) {
    waitMin = ticks(builder, WAIT_MIN, WAIT_MIN_DEFAULT);
    waitMax = ticks(builder, WAIT_MAX, WAIT_MAX_DEFAULT);
    biteMin = ticks(builder, BITE_MIN, BITE_MIN_DEFAULT);
    biteMax = ticks(builder, BITE_MAX, BITE_MAX_DEFAULT);
    lureTicks = builder.defineInRange(LURE_TICKS, LURE_TICKS_DEFAULT, LURE_TICKS_MIN, TICKS_MAX);
    castReach = builder.defineInRange(CAST_REACH, CAST_REACH_DEFAULT, TICKS_MIN, CAST_REACH_MAX);
    farmerScan = ticks(builder, FARMER_SCAN, FARMER_SCAN_DEFAULT);
  }

  private static ModConfigSpec.IntValue ticks(
      ModConfigSpec.Builder builder, String key, int defaultTicks) {
    return builder.defineInRange(key, defaultTicks, TICKS_MIN, TICKS_MAX);
  }
}
