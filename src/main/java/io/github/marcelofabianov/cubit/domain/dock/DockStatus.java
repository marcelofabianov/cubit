package io.github.marcelofabianov.cubit.domain.dock;

/** What the screen of a dock shows. */
public enum DockStatus {
  OFF,
  IDLE,
  CHARGING,
  COMPLETE,
  NO_ENERGY;

  /** With a robot in the base, a full battery comes first: it needs no energy from the stock. */
  public static DockStatus decide(boolean stockEmpty, boolean robotDocked, boolean robotFull) {
    if (!robotDocked) {
      return stockEmpty ? OFF : IDLE;
    }
    if (robotFull) {
      return COMPLETE;
    }
    return stockEmpty ? NO_ENERGY : CHARGING;
  }
}
