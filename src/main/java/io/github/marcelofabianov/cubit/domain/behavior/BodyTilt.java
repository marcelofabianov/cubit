package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * How much the body leans towards an owner at another level, in degrees, with up as positive. It
 * moves a little each tick to stay subtle. Constants, because this is only visual.
 */
public record BodyTilt(float previous, float current) {
  public static final BodyTilt STRAIGHT = new BodyTilt(0f, 0f);

  private static final float FULL_DEGREES = 8f;
  private static final float DEGREES_PER_TICK = 1f;
  private static final String INVALID_MESSAGE = "Invalid body tilt: ";
  private static final String SEPARATOR = ", ";

  public BodyTilt {
    if (!isWithinTheLean(previous) || !isWithinTheLean(current)) {
      throw new IllegalArgumentException(INVALID_MESSAGE + previous + SEPARATOR + current);
    }
  }

  /** Also false for a value that is not a number. */
  private static boolean isWithinTheLean(float degrees) {
    return Math.abs(degrees) <= FULL_DEGREES;
  }

  public BodyTilt next(GazeDirection ownerLevel) {
    float target =
        switch (ownerLevel) {
          case UP -> FULL_DEGREES;
          case DOWN -> -FULL_DEGREES;
          case LEVEL -> 0f;
        };
    float step = Math.clamp(target - current, -DEGREES_PER_TICK, DEGREES_PER_TICK);
    return new BodyTilt(current, current + step);
  }

  /** The drawing runs between ticks, so it blends the last two to stay smooth. */
  public float at(float partialTick) {
    return previous + (current - previous) * partialTick;
  }
}
