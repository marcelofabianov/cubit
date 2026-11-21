package io.github.zenndg.cubit.domain.desk;

/**
 * What the desk is doing. A pause keeps the order and the progress, and the desk goes on by itself
 * when the cause is gone (answers 1443, 1448 and 1472).
 */
public enum DeskStatus {
  IDLE,
  CRAFTING,
  REDSTONE,
  MISSING_MATERIALS,
  REFUSED,
  NO_ENERGY,
  OUTPUT_FULL;

  /** The first cause found is the one shown, from the order to the room in the output. */
  public static DeskStatus decide(DeskCheck check) {
    if (!check.hasOrder()) {
      return IDLE;
    }
    if (check.redstone()) {
      return REDSTONE;
    }
    if (!check.materials()) {
      return MISSING_MATERIALS;
    }
    if (!check.accepted()) {
      return REFUSED;
    }
    if (!check.energy()) {
      return NO_ENERGY;
    }
    return check.outputFits() ? CRAFTING : OUTPUT_FULL;
  }

  public boolean isWorking() {
    return this == CRAFTING;
  }

  public boolean isPaused() {
    return this != IDLE && this != CRAFTING;
  }
}
