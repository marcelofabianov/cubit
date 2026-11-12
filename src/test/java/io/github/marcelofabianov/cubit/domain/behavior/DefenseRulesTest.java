package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import org.junit.jupiter.api.Test;

class DefenseRulesTest {
  private static final ShockSignals NOTHING = new ShockSignals(false, false, false, false);
  private static final ShockSignals EVERYTHING = new ShockSignals(true, true, true, true);
  private static final ShockSignals ANGRY = new ShockSignals(true, false, false, false);
  private static final ShockSignals REMEMBERED = new ShockSignals(false, true, false, false);
  private static final ShockSignals ANGRY_AND_REMEMBERED =
      new ShockSignals(true, true, false, false);
  private static final ShockSignals THE_TARGET = new ShockSignals(false, false, true, true);
  private static final ShockSignals NOT_THE_TARGET = new ShockSignals(false, true, false, true);
  private static final ShockSignals NO_TARGET_REMEMBERED =
      new ShockSignals(false, true, false, false);
  private static final ShockCandidate[] NEVER_SHOCKED = {
    ShockCandidate.OWNER, ShockCandidate.PLAYER, ShockCandidate.ROBOT, ShockCandidate.PEACEFUL_MOB
  };

  @Test
  void peacefulNeverShocks() {
    for (ShockCandidate kind : ShockCandidate.values()) {
      assertFalse(DefenseRules.shocks(DefenseMode.PEACEFUL, kind, EVERYTHING));
    }
  }

  @Test
  void theOwnerPlayersRobotsAndPeacefulMobsAreNeverShocked() {
    for (DefenseMode mode : DefenseMode.values()) {
      for (ShockCandidate kind : NEVER_SHOCKED) {
        assertFalse(DefenseRules.shocks(mode, kind, EVERYTHING));
      }
    }
  }

  @Test
  void retaliateOnlyShocksTheRememberedAttacker() {
    assertTrue(DefenseRules.shocks(DefenseMode.RETALIATE, ShockCandidate.HOSTILE_MOB, REMEMBERED));
    assertFalse(DefenseRules.shocks(DefenseMode.RETALIATE, ShockCandidate.HOSTILE_MOB, NOTHING));
  }

  @Test
  void retaliateOnlyShocksANeutralMobAngryAtTheOwner() {
    assertTrue(
        DefenseRules.shocks(
            DefenseMode.RETALIATE, ShockCandidate.NEUTRAL_MOB, ANGRY_AND_REMEMBERED));
    assertFalse(DefenseRules.shocks(DefenseMode.RETALIATE, ShockCandidate.NEUTRAL_MOB, REMEMBERED));
  }

  @Test
  void aggressiveShocksEveryHostileMob() {
    assertTrue(DefenseRules.shocks(DefenseMode.AGGRESSIVE, ShockCandidate.HOSTILE_MOB, NOTHING));
  }

  @Test
  void aggressiveOnlyShocksANeutralMobAngryAtTheOwner() {
    assertTrue(DefenseRules.shocks(DefenseMode.AGGRESSIVE, ShockCandidate.NEUTRAL_MOB, ANGRY));
    assertFalse(DefenseRules.shocks(DefenseMode.AGGRESSIVE, ShockCandidate.NEUTRAL_MOB, NOTHING));
  }

  @Test
  void focusOnlyShocksTheTarget() {
    assertTrue(DefenseRules.shocks(DefenseMode.FOCUS, ShockCandidate.HOSTILE_MOB, THE_TARGET));
    assertFalse(DefenseRules.shocks(DefenseMode.FOCUS, ShockCandidate.HOSTILE_MOB, NOT_THE_TARGET));
  }

  @Test
  void focusShocksANeutralTargetEvenIfCalm() {
    assertTrue(DefenseRules.shocks(DefenseMode.FOCUS, ShockCandidate.NEUTRAL_MOB, THE_TARGET));
  }

  @Test
  void focusWithoutATargetRetaliates() {
    assertTrue(
        DefenseRules.shocks(DefenseMode.FOCUS, ShockCandidate.HOSTILE_MOB, NO_TARGET_REMEMBERED));
    assertFalse(DefenseRules.shocks(DefenseMode.FOCUS, ShockCandidate.HOSTILE_MOB, NOTHING));
  }
}
