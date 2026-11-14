package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;

/**
 * Whether `Unload` of the Tablet can send the chosen Cubit to its chests now, or why not (answer
 * 1089). A chest with room in reach counts, and an empty inventory has nothing to take.
 */
public enum UnloadCall {
  READY,
  NO_CHEST,
  EMPTY,
  SWITCHED_OFF,
  HIBERNATING;

  public static UnloadCall of(PowerState state, boolean empty, boolean chestWithRoom) {
    if (state == PowerState.HIBERNATING) {
      return HIBERNATING;
    }
    if (state != PowerState.ACTIVE) {
      return SWITCHED_OFF;
    }
    if (empty) {
      return EMPTY;
    }
    return chestWithRoom ? READY : NO_CHEST;
  }

  public boolean ready() {
    return this == READY;
  }

  /**
   * By itself, only a Cubit working in an area unloads, with the switch on and a full inventory,
   * unless it is already on its way (answers 1087 and 1093).
   */
  public static boolean unloadsByItself(
      boolean switchedOn, boolean workingInArea, boolean full, boolean onTrip) {
    return switchedOn && workingInArea && full && !onTrip;
  }
}
