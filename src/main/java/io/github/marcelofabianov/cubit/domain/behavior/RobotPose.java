package io.github.marcelofabianov.cubit.domain.behavior;

/** The body animation of a robot, decided from what the client already knows about it. */
public enum RobotPose {
  POWERED_DOWN,
  WALKING,
  IDLE;

  /** A robot that is off or in the dock stays still even when something pushes it. */
  public static RobotPose of(RobotExpression expression, boolean moving) {
    if (expression == RobotExpression.OFF || expression == RobotExpression.CHARGING) {
      return POWERED_DOWN;
    }
    return moving ? WALKING : IDLE;
  }

  /** The arms follow the expressions of damage, whatever the rest of the body is doing. */
  public static boolean raisesArms(RobotExpression expression) {
    return expression == RobotExpression.HURT || expression == RobotExpression.DYING;
  }
}
