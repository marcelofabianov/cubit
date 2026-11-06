package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * Follows the height of the owner. The fall limit is above the speed of landing from a jump, so a
 * jump ends with the eyes level instead of down. Constants, because this is only visual.
 */
public record OwnerGaze(GazeDirection direction, int ticksLeft) {
  public static final OwnerGaze LEVEL = new OwnerGaze(GazeDirection.LEVEL, 0);

  private static final int HOLD_TICKS = 10;
  private static final double QUICK_RISE = 0.2;
  private static final double QUICK_FALL = -0.45;
  private static final double OTHER_LEVEL_BLOCKS = 1;

  /** An invalid height fails every comparison, so the eyes just stay level. */
  public OwnerGaze next(double ownerHeightChange, double ownerHeightAboveRobot) {
    if (ownerHeightChange >= QUICK_RISE) {
      return new OwnerGaze(GazeDirection.UP, HOLD_TICKS);
    }
    if (ownerHeightChange <= QUICK_FALL) {
      return new OwnerGaze(GazeDirection.DOWN, HOLD_TICKS);
    }
    if (ticksLeft > 1) {
      return new OwnerGaze(direction, ticksLeft - 1);
    }
    return new OwnerGaze(levelOf(ownerHeightAboveRobot), 0);
  }

  /** Where the owner stands, without the jumps: this is what the body leans to. */
  public static GazeDirection levelOf(double ownerHeightAboveRobot) {
    if (ownerHeightAboveRobot >= OTHER_LEVEL_BLOCKS) {
      return GazeDirection.UP;
    }
    return ownerHeightAboveRobot <= -OTHER_LEVEL_BLOCKS ? GazeDirection.DOWN : GazeDirection.LEVEL;
  }
}
