package io.github.marcelofabianov.cubit.domain.behavior;

/** How strong a box of the scanners is at each moment: a soft pulse, never gone (answer 614). */
public final class MarkPulse {
  /** Only visual, so it stays here and not in the config (question 398). */
  public static final double PERIOD_TICKS = 60;

  public static final double WEAKEST = 0.35;
  public static final double STRONGEST = 1;
  private static final double FULL_TURN = 2 * Math.PI;
  private static final double HALF = 0.5;

  private MarkPulse() {}

  public static double at(double ticks) {
    double wave = HALF + HALF * Math.cos(FULL_TURN * ticks / PERIOD_TICKS);
    return WEAKEST + (STRONGEST - WEAKEST) * wave;
  }
}
