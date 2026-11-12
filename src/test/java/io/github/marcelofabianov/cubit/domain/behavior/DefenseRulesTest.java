package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import org.junit.jupiter.api.Test;

class DefenseRulesTest {
  private static final boolean ANGRY_AT_OWNER = true;
  private static final boolean CALM = false;
  private static final boolean REMEMBERED = true;
  private static final boolean FORGOTTEN = false;

  @Test
  void peacefulNeverShocks() {
    for (ShockCandidate kind : ShockCandidate.values()) {
      assertFalse(DefenseRules.shocks(DefenseMode.PEACEFUL, kind, ANGRY_AT_OWNER, REMEMBERED));
    }
  }

  @Test
  void theOwnerPlayersRobotsAndPeacefulMobsAreNeverShocked() {
    for (DefenseMode mode : DefenseMode.values()) {
      for (ShockCandidate kind :
          new ShockCandidate[] {
            ShockCandidate.OWNER,
            ShockCandidate.PLAYER,
            ShockCandidate.ROBOT,
            ShockCandidate.PEACEFUL_MOB
          }) {
        assertFalse(DefenseRules.shocks(mode, kind, ANGRY_AT_OWNER, REMEMBERED));
      }
    }
  }

  @Test
  void retaliateOnlyShocksTheRememberedAttacker() {
    assertTrue(
        DefenseRules.shocks(DefenseMode.RETALIATE, ShockCandidate.HOSTILE_MOB, CALM, REMEMBERED));
    assertFalse(
        DefenseRules.shocks(DefenseMode.RETALIATE, ShockCandidate.HOSTILE_MOB, CALM, FORGOTTEN));
  }

  @Test
  void retaliateOnlyShocksANeutralMobAngryAtTheOwner() {
    assertTrue(
        DefenseRules.shocks(
            DefenseMode.RETALIATE, ShockCandidate.NEUTRAL_MOB, ANGRY_AT_OWNER, REMEMBERED));
    assertFalse(
        DefenseRules.shocks(DefenseMode.RETALIATE, ShockCandidate.NEUTRAL_MOB, CALM, REMEMBERED));
  }

  @Test
  void aggressiveShocksEveryHostileMob() {
    assertTrue(
        DefenseRules.shocks(DefenseMode.AGGRESSIVE, ShockCandidate.HOSTILE_MOB, CALM, FORGOTTEN));
  }

  @Test
  void aggressiveOnlyShocksANeutralMobAngryAtTheOwner() {
    assertTrue(
        DefenseRules.shocks(
            DefenseMode.AGGRESSIVE, ShockCandidate.NEUTRAL_MOB, ANGRY_AT_OWNER, FORGOTTEN));
    assertFalse(
        DefenseRules.shocks(DefenseMode.AGGRESSIVE, ShockCandidate.NEUTRAL_MOB, CALM, REMEMBERED));
  }
}
