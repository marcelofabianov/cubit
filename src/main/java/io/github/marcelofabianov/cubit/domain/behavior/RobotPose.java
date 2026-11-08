package io.github.marcelofabianov.cubit.domain.behavior;

/** The body animation of a robot, decided from what the client already knows about it. */
public enum RobotPose {
  POWERED_DOWN,
  WALKING,
  IDLE;

  /** How long the arms stay up after a hit. It is only visual, so it stays out of the config. */
  public static final int ARMS_RAISED_TICKS = 40;

  /** A robot that is off or in the dock stays still even when something pushes it. */
  public static RobotPose of(RobotExpression expression, boolean moving) {
    if (expression == RobotExpression.OFF || expression == RobotExpression.CHARGING) {
      return POWERED_DOWN;
    }
    return moving ? WALKING : IDLE;
  }

  /** The arms go up on a hit, for less time than the expression of damage holds on the screen. */
  public static boolean raisesArms(RobotExpression expression, boolean justHit) {
    return justHit && (expression == RobotExpression.HURT || expression == RobotExpression.DYING);
  }
}
