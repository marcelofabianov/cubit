package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;

/**
 * Who each defense mode shocks. The owner, players, robots and peaceful mobs never are, and a
 * neutral mob only when it turned against the owner, or when it is the target of Focus (answers 13,
 * 68, 832, 845 and 848).
 */
public final class DefenseRules {
  private DefenseRules() {}

  public static boolean shocks(DefenseMode mode, ShockCandidate candidate, ShockSignals signals) {
    boolean mob =
        candidate == ShockCandidate.HOSTILE_MOB || candidate == ShockCandidate.NEUTRAL_MOB;
    boolean enemy =
        candidate == ShockCandidate.HOSTILE_MOB
            || candidate == ShockCandidate.NEUTRAL_MOB && signals.angryAtOwner();
    return switch (mode) {
      case PEACEFUL -> false;
      case RETALIATE -> enemy && signals.remembered();
      case AGGRESSIVE -> enemy;
      case FOCUS -> focus(mob, enemy, signals);
    };
  }

  /** With a target, only the target. Without one, Focus defends as Retaliate does. */
  private static boolean focus(boolean mob, boolean enemy, ShockSignals signals) {
    if (signals.hasFocusTarget()) {
      return mob && signals.focused();
    }
    return enemy && signals.remembered();
  }
}
