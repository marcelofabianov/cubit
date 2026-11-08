package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class RespawnReturnTest {
  private static final boolean TO_DOCK = true;
  private static final boolean FREE = false;
  private static final boolean SAME = true;
  private static final boolean OTHER = false;
  private static final int LIMIT = 20;
  private static final double FAR = 500;
  private static final double AT_THE_LIMIT = 20;
  private static final double NEAR = 19.9;

  @Test
  void anActiveRobotFarAwayComesBack() {
    assertTrue(RespawnReturn.bringsBack(PowerState.ACTIVE, FREE, SAME, FAR, LIMIT));
    assertTrue(RespawnReturn.bringsBack(PowerState.ACTIVE, FREE, SAME, AT_THE_LIMIT, LIMIT));
  }

  @Test
  void aRobotCloseByWalksInstead() {
    assertFalse(RespawnReturn.bringsBack(PowerState.ACTIVE, FREE, SAME, NEAR, LIMIT));
  }

  @Test
  void anotherDimensionComesBackAtAnyDistance() {
    assertTrue(RespawnReturn.bringsBack(PowerState.ACTIVE, FREE, OTHER, NEAR, LIMIT));
  }

  @Test
  void onlyTheActiveStateComesBack() {
    assertFalse(RespawnReturn.bringsBack(PowerState.HIBERNATING, FREE, SAME, FAR, LIMIT));
    assertFalse(RespawnReturn.bringsBack(PowerState.INACTIVE_EXHAUSTED, FREE, SAME, FAR, LIMIT));
    assertFalse(
        RespawnReturn.bringsBack(PowerState.INACTIVE_SWITCHED_OFF, FREE, OTHER, FAR, LIMIT));
  }

  @Test
  void aRobotGoingToTheDockKeepsGoing() {
    assertFalse(RespawnReturn.bringsBack(PowerState.ACTIVE, TO_DOCK, SAME, FAR, LIMIT));
  }
}
