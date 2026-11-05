package io.github.marcelofabianov.cubit.domain.energy;

/** What a robot did in one tick. The caller reads it from the world, and the domain prices it. */
public record RobotActivity(
    boolean walking,
    boolean fullInventory,
    boolean inWater,
    boolean negativeCondition,
    double climbedBlocks) {
  private static final String INVALID_HEIGHT_MESSAGE = "Climbed height must not be negative: ";

  public RobotActivity {
    if (!(climbedBlocks >= 0)) {
      throw new IllegalArgumentException(INVALID_HEIGHT_MESSAGE + climbedBlocks);
    }
  }
}
