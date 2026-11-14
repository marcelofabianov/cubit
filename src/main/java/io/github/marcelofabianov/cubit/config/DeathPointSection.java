package io.github.marcelofabianov.cubit.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The death log and the guide, for every robot. The key names and limits are a contract (answers
 * 1115, 1126 and 1127).
 */
final class DeathPointSection {
  private static final String LOG_SIZE = "death_log_size";
  private static final int LOG_SIZE_DEFAULT = 10;
  private static final int LOG_SIZE_MIN = 1;
  private static final int LOG_SIZE_MAX = 50;
  private static final String ARRIVAL = "death_guide_arrival_blocks";
  private static final int ARRIVAL_DEFAULT = 3;
  private static final int ARRIVAL_MIN = 1;
  private static final int ARRIVAL_MAX = 16;
  private static final String LEAD = "death_guide_lead_blocks";
  private static final int LEAD_DEFAULT = 8;
  private static final int LEAD_MIN = 2;
  private static final int LEAD_MAX = 16;
  private static final String STUCK = "death_guide_stuck_seconds";
  private static final int STUCK_DEFAULT = 10;
  private static final int STUCK_MIN = 1;
  private static final int STUCK_MAX = 60;

  final ModConfigSpec.IntValue logSize;
  final ModConfigSpec.IntValue arrival;
  final ModConfigSpec.IntValue lead;
  final ModConfigSpec.IntValue stuckSeconds;

  DeathPointSection(ModConfigSpec.Builder builder) {
    logSize = builder.defineInRange(LOG_SIZE, LOG_SIZE_DEFAULT, LOG_SIZE_MIN, LOG_SIZE_MAX);
    arrival = builder.defineInRange(ARRIVAL, ARRIVAL_DEFAULT, ARRIVAL_MIN, ARRIVAL_MAX);
    lead = builder.defineInRange(LEAD, LEAD_DEFAULT, LEAD_MIN, LEAD_MAX);
    stuckSeconds = builder.defineInRange(STUCK, STUCK_DEFAULT, STUCK_MIN, STUCK_MAX);
  }
}
