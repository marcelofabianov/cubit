package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;

/**
 * What a tier does with the card of the Fisher: the levels of Lure and Luck of the Sea, as the
 * enchantments of the game, and the energy of each fish caught (answers 1185 and 1190).
 */
public record FisherSpec(int lureLevel, int luckLevel, Energy catchCost) {
  public static final FisherSpec NONE = new FisherSpec(0, 0, Energy.ZERO);

  /** The highest level the enchantments have in the game. */
  public static final int MAX_LEVEL = 3;

  private static final String INVALID_LEVEL = "Fisher levels go from 0 to 3: ";
  private static final String SEPARATOR = ", ";

  public FisherSpec {
    if (lureLevel < 0 || lureLevel > MAX_LEVEL || luckLevel < 0 || luckLevel > MAX_LEVEL) {
      throw new IllegalArgumentException(INVALID_LEVEL + lureLevel + SEPARATOR + luckLevel);
    }
  }

  /** Each fish caught costs its energy, down to an empty battery, and never more (rule 13). */
  public Battery payForCatch(Battery battery) {
    return battery.consume(catchCost).battery();
  }
}
