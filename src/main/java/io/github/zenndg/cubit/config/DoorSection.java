package io.github.zenndg.cubit.config;

import io.github.zenndg.cubit.domain.energy.Energy;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The marked doors, the same on every tier: what opening one costs and how many each owner marks
 * (answers 1368 and 1369). The names are a contract with the pack.
 */
public final class DoorSection {
  private static final String OPEN_COST = "door_open_cost";
  private static final int OPEN_COST_DEFAULT = 50;
  private static final int OPEN_COST_HIGHEST = 10_000;
  private static final String MARKS_LIMIT = "door_marks_limit";
  private static final int MARKS_LIMIT_DEFAULT = 64;
  private static final int MARKS_LIMIT_LOWEST = 1;
  private static final int MARKS_LIMIT_HIGHEST = 256;

  private final ModConfigSpec.IntValue openCost;
  private final ModConfigSpec.IntValue marksLimit;

  DoorSection(ModConfigSpec.Builder builder) {
    openCost = builder.defineInRange(OPEN_COST, OPEN_COST_DEFAULT, 0, OPEN_COST_HIGHEST);
    marksLimit =
        builder.defineInRange(
            MARKS_LIMIT, MARKS_LIMIT_DEFAULT, MARKS_LIMIT_LOWEST, MARKS_LIMIT_HIGHEST);
  }

  public Energy openCost() {
    return new Energy(openCost.get());
  }

  public int marksLimit() {
    return marksLimit.get();
  }
}
