package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/** How often and how far the robot looks for dark spots, shared by all the tiers (answer 981). */
final class TorchSection {
  private static final String CHECK = "torch_check_ticks";
  private static final int CHECK_DEFAULT = 40;
  private static final int CHECK_MIN = 1;
  private static final int CHECK_MAX = 1200;
  private static final String SCAN_RADIUS = "torch_scan_radius_blocks";
  private static final int SCAN_RADIUS_DEFAULT = 8;
  private static final int SCAN_RADIUS_MAX = 16;
  private static final String REACH = "torch_reach_blocks";
  private static final int REACH_DEFAULT = 4;
  private static final int REACH_MAX = 8;
  private static final int MIN_ONE = 1;

  final ModConfigSpec.IntValue check;
  final ModConfigSpec.IntValue scanRadius;
  final ModConfigSpec.IntValue reach;

  TorchSection(ModConfigSpec.Builder builder) {
    check = builder.defineInRange(CHECK, CHECK_DEFAULT, CHECK_MIN, CHECK_MAX);
    scanRadius = builder.defineInRange(SCAN_RADIUS, SCAN_RADIUS_DEFAULT, MIN_ONE, SCAN_RADIUS_MAX);
    reach = builder.defineInRange(REACH, REACH_DEFAULT, MIN_ONE, REACH_MAX);
  }
}
