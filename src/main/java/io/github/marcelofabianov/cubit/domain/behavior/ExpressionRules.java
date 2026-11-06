package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.Set;

/** Which expression wins, and when changing to it beeps. The caller informs the situation. */
public final class ExpressionRules {
  private static final Set<RobotExpression> BEEPING =
      Set.of(RobotExpression.NORMAL, RobotExpression.HAPPY, RobotExpression.SLEEPY);
  private static final Set<RobotExpression> NO_BEEP_ON_LEAVING =
      Set.of(
          RobotExpression.OFF,
          RobotExpression.CHARGING,
          RobotExpression.HURT,
          RobotExpression.DYING,
          RobotExpression.WORRIED);

  private ExpressionRules() {}

  public static RobotExpression decide(PowerState state, RobotSituation situation) {
    if (state == PowerState.HIBERNATING) {
      return RobotExpression.CHARGING;
    }
    if (state != PowerState.ACTIVE) {
      return RobotExpression.OFF;
    }
    return activeExpression(situation);
  }

  public static boolean isDying(float health, float maxHealth, Percentage limit) {
    return health * Percentage.MAX <= maxHealth * limit.value();
  }

  /**
   * Turning on, being placed and loading start from a dark screen, and have their own sound.
   * Leaving the damage expressions is silent too, or every hit would end in a beep.
   */
  public static boolean beeps(RobotExpression from, RobotExpression to) {
    return from != to && BEEPING.contains(to) && !NO_BEEP_ON_LEAVING.contains(from);
  }

  private static RobotExpression activeExpression(RobotSituation now) {
    if (now.dying()) {
      return RobotExpression.DYING;
    }
    if (now.hurt()) {
      return RobotExpression.HURT;
    }
    if (now.worried()) {
      return RobotExpression.WORRIED;
    }
    if (now.alert()) {
      return RobotExpression.ALERT;
    }
    if (now.surprised()) {
      return RobotExpression.SURPRISED;
    }
    if (now.criticalBattery()) {
      return RobotExpression.SLEEPY;
    }
    return now.happy() ? RobotExpression.HAPPY : RobotExpression.NORMAL;
  }
}
