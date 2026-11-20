package io.github.zenndg.cubit.domain.tier;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;

/**
 * What a tier does with the card of the Organizer: how many chests and marks it keeps, the energy
 * of each stack moved, the pause between actions and how often it scans (answer 1300).
 */
public record OrganizerSpec(
    int chests, int marks, Energy actionCost, int actionPauseTicks, int scanIntervalTicks) {
  public static final OrganizerSpec NONE = new OrganizerSpec(0, 0, Energy.ZERO, 1, 1);

  private static final String INVALID = "Invalid Organizer numbers: chests ";
  private static final String MARKS = ", marks ";
  private static final String PAUSE = ", pause ";
  private static final String SCAN = ", scan ";

  public OrganizerSpec {
    if (chests < 0 || marks < 0 || actionPauseTicks < 1 || scanIntervalTicks < 1) {
      throw new IllegalArgumentException(
          INVALID + chests + MARKS + marks + PAUSE + actionPauseTicks + SCAN + scanIntervalTicks);
    }
  }

  /** Each stack moved costs its energy, down to an empty battery, and never more (rule 13). */
  public Battery payForAction(Battery battery) {
    return battery.consume(actionCost).battery();
  }
}
