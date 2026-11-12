package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;

/**
 * Who each defense mode shocks. The owner, players, robots and peaceful mobs never are, and a
 * neutral mob only when it turned against the owner (answers 13, 68 and 832).
 */
public final class DefenseRules {
  private DefenseRules() {}

  public static boolean shocks(
      DefenseMode mode, ShockCandidate candidate, boolean angryAtOwner, boolean remembered) {
    boolean enemy =
        candidate == ShockCandidate.HOSTILE_MOB
            || candidate == ShockCandidate.NEUTRAL_MOB && angryAtOwner;
    return switch (mode) {
      case PEACEFUL -> false;
      case RETALIATE -> enemy && remembered;
      case AGGRESSIVE -> enemy;
    };
  }
}
