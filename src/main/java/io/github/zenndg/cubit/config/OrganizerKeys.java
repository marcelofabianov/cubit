package io.github.zenndg.cubit.config;

import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.tier.OrganizerSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * What one tier does with the card of the Organizer. The key names are a contract (answer 1300).
 */
final class OrganizerKeys {
  private static final String CHESTS = "organizer_chests";
  private static final String MARKS = "organizer_marks";
  private static final String COST = "organizer_action_cost";
  private static final String PAUSE = "organizer_action_pause_ticks";
  private static final String SCAN = "organizer_scan_interval_ticks";
  private static final int MIN_ZERO = 0;
  private static final int MIN_ONE = 1;
  private static final int MAX_BLOCKS = 64;
  private static final int MAX_TICKS = 72000;

  private final ModConfigSpec.IntValue chests;
  private final ModConfigSpec.IntValue marks;
  private final ModConfigSpec.IntValue cost;
  private final ModConfigSpec.IntValue pause;
  private final ModConfigSpec.IntValue scan;

  OrganizerKeys(ModConfigSpec.Builder builder, OrganizerSpec defaults) {
    chests = builder.defineInRange(CHESTS, defaults.chests(), MIN_ZERO, MAX_BLOCKS);
    marks = builder.defineInRange(MARKS, defaults.marks(), MIN_ZERO, MAX_BLOCKS);
    cost =
        builder.defineInRange(
            COST, (int) defaults.actionCost().amount(), MIN_ZERO, Integer.MAX_VALUE);
    pause = builder.defineInRange(PAUSE, defaults.actionPauseTicks(), MIN_ONE, MAX_TICKS);
    scan = builder.defineInRange(SCAN, defaults.scanIntervalTicks(), MIN_ONE, MAX_TICKS);
  }

  OrganizerSpec read() {
    return new OrganizerSpec(
        chests.get(), marks.get(), new Energy(cost.get()), pause.get(), scan.get());
  }
}
