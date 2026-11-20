package io.github.zenndg.cubit.domain.tier;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;

/**
 * What a tier does with the card of the Cook: how many ovens it marks, the energy of each stack
 * moved, the pause between actions and how often it scans (answer 1326).
 */
public record CookSpec(int ovens, Energy actionCost, int actionPauseTicks, int scanIntervalTicks) {
  public static final CookSpec NONE = new CookSpec(0, Energy.ZERO, 1, 1);

  private static final String INVALID = "Invalid Cook numbers: ovens ";
  private static final String PAUSE = ", pause ";
  private static final String SCAN = ", scan ";

  public CookSpec {
    if (ovens < 0 || actionPauseTicks < 1 || scanIntervalTicks < 1) {
      throw new IllegalArgumentException(
          INVALID + ovens + PAUSE + actionPauseTicks + SCAN + scanIntervalTicks);
    }
  }

  /** Each stack moved costs its energy, down to an empty battery, and never more (rule 13). */
  public Battery payForAction(Battery battery) {
    return battery.consume(actionCost).battery();
  }
}
