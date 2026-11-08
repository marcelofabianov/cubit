package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * What the antennas and the halo around the body show of the scanners: red for a mob, blue for an
 * ore, or nothing (answers 558 and 638 to 640).
 */
public enum ScanSignal {
  NONE,
  MOB,
  ORE;

  /** Danger first, as the alert face wins over the surprise (answer 639). */
  public static ScanSignal of(boolean mobInRange, boolean oreFound) {
    if (mobInRange) {
      return MOB;
    }
    return oreFound ? ORE : NONE;
  }

  /** An unknown number falls back to nothing, so bad data never lights the robot. */
  public static ScanSignal fromOrdinal(int ordinal) {
    ScanSignal[] all = values();
    return ordinal >= 0 && ordinal < all.length ? all[ordinal] : NONE;
  }
}
