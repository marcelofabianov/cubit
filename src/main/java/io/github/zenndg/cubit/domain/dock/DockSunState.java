package io.github.zenndg.cubit.domain.dock;

/** What the panel of a Solar Dock is doing, and why it stops, for the Solar tab (answer 1396). */
public enum DockSunState {
  GENERATING,
  NIGHT,
  RAIN,
  COVERED,
  NO_SUN
}
