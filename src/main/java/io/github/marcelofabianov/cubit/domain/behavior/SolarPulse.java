package io.github.marcelofabianov.cubit.domain.behavior;

/** How strong the light on the solar cell is at each moment. Slow, to stay subtle. */
public final class SolarPulse {
  /** Only visual, so it stays here and not in the config (question 398). */
  public static final double PERIOD_TICKS = 60;

  public static final double WEAKEST = 0.1;
  public static final double STRONGEST = 0.6;
  private static final double FULL_TURN = 2 * Math.PI;
  private static final double HALF = 0.5;

  private SolarPulse() {}

  public static double at(double ticks) {
    double wave = HALF - HALF * Math.cos(FULL_TURN * ticks / PERIOD_TICKS);
    return WEAKEST + (STRONGEST - WEAKEST) * wave;
  }
}
