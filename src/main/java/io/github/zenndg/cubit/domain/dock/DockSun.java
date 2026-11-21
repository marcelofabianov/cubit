package io.github.zenndg.cubit.domain.dock;

/**
 * What the panel of a Solar Dock sees: whether it is day, whether the sky above it is open, whether
 * it rains there and whether the world has a sun at all, which the Nether and the End do not
 * (answer 1384).
 */
public record DockSun(boolean day, boolean skyOpen, boolean raining, boolean naturalSky) {
  /** Under the sun the panel generates, and otherwise the dock is a normal one. */
  public static boolean generates(DockSun sun) {
    return sun.state() == DockSunState.GENERATING;
  }

  /**
   * Why the panel stops, the most lasting reason first: a world without a sun, then a block over
   * the panel, then the night and then the rain, which only the player can change or wait for
   * (answer 1396).
   */
  public DockSunState state() {
    if (!naturalSky) {
      return DockSunState.NO_SUN;
    }
    if (!skyOpen) {
      return DockSunState.COVERED;
    }
    if (!day) {
      return DockSunState.NIGHT;
    }
    return raining ? DockSunState.RAIN : DockSunState.GENERATING;
  }
}
