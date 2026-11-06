package io.github.marcelofabianov.cubit.domain.behavior;

/** How strong the halo of the lamp is at each moment. It breathes, and it never goes out. */
public final class LampPulse {
  /** Only visual, so it stays here and not in the config (question 398). */
  public static final double PERIOD_TICKS = 40;

  public static final double WEAKEST = 0.7;
  private static final double STRONGEST = 1;
  private static final double FULL_TURN = 2 * Math.PI;
  private static final double HALF = 0.5;

  private LampPulse() {}

  public static double at(double ticks) {
    double wave = HALF + HALF * Math.cos(FULL_TURN * ticks / PERIOD_TICKS);
    return WEAKEST + (STRONGEST - WEAKEST) * wave;
  }
}
