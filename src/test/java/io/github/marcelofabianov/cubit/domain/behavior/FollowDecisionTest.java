package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class FollowDecisionTest {
  private static final int STOP_BLOCKS = 3;
  private static final int TELEPORT_BLOCKS = 20;
  private static final double FAR_AWAY_BLOCKS = 100;
  private static final double JUST_ABOVE_STOP = 3.1;
  private static final double JUST_BELOW_TELEPORT = 19.9;
  private static final FollowDecision DECISION = new FollowDecision(STOP_BLOCKS, TELEPORT_BLOCKS);

  @Test
  void onlyAnActiveRobotMoves() {
    assertEquals(
        FollowAction.STOP, DECISION.decide(PowerState.INACTIVE_EXHAUSTED, FAR_AWAY_BLOCKS, true));
    assertEquals(
        FollowAction.STOP,
        DECISION.decide(PowerState.INACTIVE_SWITCHED_OFF, FAR_AWAY_BLOCKS, true));
    assertEquals(FollowAction.STOP, DECISION.decide(PowerState.HIBERNATING, FAR_AWAY_BLOCKS, true));
  }

  @Test
  void activeRobotStopsUpToTheStopDistance() {
    assertEquals(FollowAction.STOP, DECISION.decide(PowerState.ACTIVE, STOP_BLOCKS, true));
  }

  @Test
  void activeRobotFollowsBetweenTheTwoDistances() {
    assertEquals(FollowAction.FOLLOW, DECISION.decide(PowerState.ACTIVE, JUST_ABOVE_STOP, true));
    assertEquals(
        FollowAction.FOLLOW, DECISION.decide(PowerState.ACTIVE, JUST_BELOW_TELEPORT, true));
  }

  @Test
  void activeRobotTeleportsFromTheTeleportDistance() {
    assertEquals(FollowAction.TELEPORT, DECISION.decide(PowerState.ACTIVE, TELEPORT_BLOCKS, true));
    assertEquals(FollowAction.TELEPORT, DECISION.decide(PowerState.ACTIVE, FAR_AWAY_BLOCKS, true));
  }

  @Test
  void robotNeverTeleportsToAnotherDimension() {
    assertEquals(FollowAction.STOP, DECISION.decide(PowerState.ACTIVE, FAR_AWAY_BLOCKS, false));
  }

  @Test
  void invalidDistanceFailsSafeByStopping() {
    assertEquals(FollowAction.STOP, DECISION.decide(PowerState.ACTIVE, Double.NaN, true));
  }

  @Test
  void rejectsDistancesThatCannotWorkTogether() {
    assertThrows(IllegalArgumentException.class, () -> new FollowDecision(0, TELEPORT_BLOCKS));
    assertThrows(
        IllegalArgumentException.class, () -> new FollowDecision(TELEPORT_BLOCKS, TELEPORT_BLOCKS));
  }
}
