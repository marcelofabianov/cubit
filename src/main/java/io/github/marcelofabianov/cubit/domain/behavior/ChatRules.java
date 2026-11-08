package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.Set;

/** When the robot may comment on something. */
public final class ChatRules {
  private static final Set<RobotExpression> CALM =
      Set.of(RobotExpression.NORMAL, RobotExpression.HAPPY);

  private ChatRules() {}

  public static boolean runs(boolean switchedOn, PowerState state, boolean followsItsOwner) {
    return switchedOn && state == PowerState.ACTIVE && followsItsOwner;
  }

  /**
   * Congratulating makes the face happy, and that change beeps only from the normal face. From any
   * other, the face stays or a stronger one wins, so the phrase needs its own beep.
   */
  public static boolean needsItsOwnBeep(RobotExpression expression) {
    return expression != RobotExpression.NORMAL;
  }

  /** A pet is never a joke, and a farm would otherwise make the robot speak at every animal. */
  public static boolean remarksOnAnimal(boolean pet, int roll, int chancePercent) {
    return !pet && roll < chancePercent;
  }

  /** Any other face means something more important is going on, such as a mob or a wound. */
  public static boolean isCalm(RobotExpression expression) {
    return CALM.contains(expression);
  }
}
