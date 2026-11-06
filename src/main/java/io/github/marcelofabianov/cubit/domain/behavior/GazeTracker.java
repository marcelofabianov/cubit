package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * Follows the height of the robot. The direction holds for a while after the movement, because a
 * step lasts only a few ticks and the eyes would shake on stairs.
 */
public record GazeTracker(GazeDirection direction, int ticksLeft) {
  public static final GazeTracker LEVEL = new GazeTracker(GazeDirection.LEVEL, 0);

  private static final int HOLD_TICKS = 10;
  private static final double MIN_MOVEMENT = 0.02;

  public GazeTracker next(double heightChange) {
    if (heightChange > MIN_MOVEMENT) {
      return new GazeTracker(GazeDirection.UP, HOLD_TICKS);
    }
    if (heightChange < -MIN_MOVEMENT) {
      return new GazeTracker(GazeDirection.DOWN, HOLD_TICKS);
    }
    return ticksLeft > 1 ? new GazeTracker(direction, ticksLeft - 1) : LEVEL;
  }
}
