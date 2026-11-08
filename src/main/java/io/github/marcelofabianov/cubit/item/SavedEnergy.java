package io.github.marcelofabianov.cubit.item;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import org.slf4j.Logger;

/** Reads an amount of energy from a save, where it could be anything (AGENTS.md, rule 17). */
public final class SavedEnergy {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String NEGATIVE_MESSAGE = "Negative energy in a save: {}. Using zero.";

  private SavedEnergy() {}

  public static Energy of(long amount) {
    if (amount < 0) {
      LOGGER.warn(NEGATIVE_MESSAGE, amount);
      return Energy.ZERO;
    }
    return new Energy(amount);
  }
}
