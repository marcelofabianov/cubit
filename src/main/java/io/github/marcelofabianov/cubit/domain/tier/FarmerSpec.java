package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;

/**
 * What a tier does with the card of the Farmer: the energy and the pause of each action, harvesting
 * or tilling, the crops of special harvest, bone meal and Fortune (answers 1219, 1221, 1226 and
 * 1235).
 */
public record FarmerSpec(
    Energy actionCost, int actionPauseTicks, boolean specialCrops, boolean boneMeal, int fortune) {
  public static final FarmerSpec NONE = new FarmerSpec(Energy.ZERO, 1, false, false, 0);

  /** The highest level Fortune has in the game. */
  public static final int MAX_FORTUNE = 3;

  private static final String INVALID = "Invalid Farmer numbers: pause ";
  private static final String FORTUNE = ", fortune ";

  public FarmerSpec {
    if (actionPauseTicks < 1 || fortune < 0 || fortune > MAX_FORTUNE) {
      throw new IllegalArgumentException(INVALID + actionPauseTicks + FORTUNE + fortune);
    }
  }

  /** Each action costs its energy, down to an empty battery, and never more (rule 13). */
  public Battery payForAction(Battery battery) {
    return battery.consume(actionCost).battery();
  }
}
