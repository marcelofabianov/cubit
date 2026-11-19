package io.github.zenndg.cubit.domain.robot;

/**
 * The steps of the speed buttons, read from the config by the caller: the lowest percentage and how
 * much each press moves it. The top is always the whole speed of the tier (answers 1259 and 1267).
 */
public record SpeedSteps(int minPercent, int stepPercent) {
  private static final int MIN_ALLOWED = 1;
  private static final String INVALID_MIN = "Invalid minimum speed percentage: ";
  private static final String INVALID_STEP = "Invalid speed step percentage: ";

  public SpeedSteps {
    if (minPercent < MIN_ALLOWED || minPercent > SpeedChoice.MAX_PERCENT) {
      throw new IllegalArgumentException(INVALID_MIN + minPercent);
    }
    if (stepPercent < MIN_ALLOWED) {
      throw new IllegalArgumentException(INVALID_STEP + stepPercent);
    }
  }
}
