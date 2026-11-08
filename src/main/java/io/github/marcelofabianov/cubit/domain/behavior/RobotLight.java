package io.github.marcelofabianov.cubit.domain.behavior;

/** The light a robot gives to the world. */
public final class RobotLight {
  private static final LightLevel NO_LIGHT = new LightLevel(0);

  private RobotLight() {}

  /** Nothing with the screen dark, because the screen and the lamp only work on a robot awake. */
  public static LightLevel level(
      boolean screenGlows, LightLevel screen, boolean lampLit, LightLevel lamp) {
    if (!screenGlows) {
      return NO_LIGHT;
    }
    return lampLit ? screen.max(lamp) : screen;
  }
}
