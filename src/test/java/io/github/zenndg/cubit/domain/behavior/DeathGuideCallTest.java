package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.zenndg.cubit.domain.tier.DeathPointLevel;
import org.junit.jupiter.api.Test;

class DeathGuideCallTest {
  private static final boolean ON = true;
  private static final boolean OFF = false;
  private static final boolean OVERWORLD = true;
  private static final boolean ELSEWHERE = false;
  private static final boolean FOLLOWING = true;
  private static final boolean NOT_FOLLOWING = false;

  /** Only Elite and Ultimate guide, in the Overworld, with the Cubit that follows (1117, 1120). */
  @Test
  void aGuidingTierCanGuideTheCubitThatFollows() {
    assertTrue(
        DeathGuideCall.of(DeathPointLevel.GUIDE, ON, OVERWORLD, OVERWORLD, FOLLOWING).ready());
  }

  @Test
  void theRefusalSaysWhy() {
    assertEquals(
        DeathGuideCall.LOCKED,
        DeathGuideCall.of(DeathPointLevel.MARK, ON, OVERWORLD, OVERWORLD, FOLLOWING));
    assertEquals(
        DeathGuideCall.SWITCHED_OFF,
        DeathGuideCall.of(DeathPointLevel.GUIDE, OFF, OVERWORLD, OVERWORLD, FOLLOWING));
    assertEquals(
        DeathGuideCall.DEATH_ELSEWHERE,
        DeathGuideCall.of(DeathPointLevel.GUIDE, ON, ELSEWHERE, OVERWORLD, FOLLOWING));
    assertEquals(
        DeathGuideCall.CUBIT_ELSEWHERE,
        DeathGuideCall.of(DeathPointLevel.GUIDE, ON, OVERWORLD, ELSEWHERE, FOLLOWING));
    assertEquals(
        DeathGuideCall.NOT_FOLLOWING,
        DeathGuideCall.of(DeathPointLevel.GUIDE, ON, OVERWORLD, OVERWORLD, NOT_FOLLOWING));
  }

  /** The tier comes first, so Base and Advanced always read `Unlocks at Elite`. */
  @Test
  void theTierIsCheckedFirst() {
    assertEquals(
        DeathGuideCall.LOCKED,
        DeathGuideCall.of(DeathPointLevel.LOCKED, OFF, ELSEWHERE, ELSEWHERE, NOT_FOLLOWING));
  }

  /** From Advanced on, with the switch on, the Cubit that follows tells where (answer 1111). */
  @Test
  void theCubitThatFollowsTellsTheSpotOnRespawn() {
    assertTrue(DeathGuideCall.tellsOnRespawn(DeathPointLevel.MARK, ON, FOLLOWING));
    assertTrue(DeathGuideCall.tellsOnRespawn(DeathPointLevel.GUIDE, ON, FOLLOWING));
    assertFalse(DeathGuideCall.tellsOnRespawn(DeathPointLevel.LOCKED, ON, FOLLOWING));
    assertFalse(DeathGuideCall.tellsOnRespawn(DeathPointLevel.MARK, OFF, FOLLOWING));
    assertFalse(DeathGuideCall.tellsOnRespawn(DeathPointLevel.MARK, ON, NOT_FOLLOWING));
  }
}
