package io.github.zenndg.cubit.domain.robot;

/**
 * How fast the player wants the Cubit to walk, as a percentage of the speed of its tier, so the
 * same value holds in any tier and follows the upgrade (answers 1259 and 1266). The valid values go
 * down from 100% one step at a time, and the minimum is always the last one, even off the steps.
 */
public record SpeedChoice(int percent) {
  public static final int MAX_PERCENT = 100;
  public static final SpeedChoice FULL = new SpeedChoice(MAX_PERCENT);

  private static final int MIN_PERCENT = 1;
  private static final double WHOLE = 1.0;
  private static final double PERCENT_SCALE = 100.0;
  private static final String INVALID = "Invalid speed percentage: ";

  public SpeedChoice {
    if (percent < MIN_PERCENT || percent > MAX_PERCENT) {
      throw new IllegalArgumentException(INVALID + percent);
    }
  }

  /** The nearest valid step, for a value saved before the pack changed the config (1266). */
  public SpeedChoice fitted(SpeedSteps steps) {
    int clamped = Math.clamp(percent, steps.minPercent(), MAX_PERCENT);
    int stepsDown = Math.round((float) (MAX_PERCENT - clamped) / steps.stepPercent());
    int onStep = Math.max(MAX_PERCENT - stepsDown * steps.stepPercent(), steps.minPercent());
    boolean minimumIsNearer = Math.abs(clamped - steps.minPercent()) < Math.abs(clamped - onStep);
    return new SpeedChoice(minimumIsNearer ? steps.minPercent() : onStep);
  }

  /** One press of − or +, from the fitted value, stopping at the minimum and at 100%. */
  public SpeedChoice step(int direction, SpeedSteps steps) {
    int from = fitted(steps).percent();
    if (direction < 0) {
      return new SpeedChoice(Math.max(from - steps.stepPercent(), steps.minPercent()));
    }
    if (direction == 0) {
      return new SpeedChoice(from);
    }
    int stepsDown = Math.ceilDiv(MAX_PERCENT - from, steps.stepPercent());
    return new SpeedChoice(
        Math.min(MAX_PERCENT - (stepsDown - 1) * steps.stepPercent(), MAX_PERCENT));
  }

  /** What multiplies the speed of the tier: the whole of it in danger, so it flees fast (1268). */
  public double factor(boolean inDanger) {
    return inDanger ? WHOLE : percent / PERCENT_SCALE;
  }
}
