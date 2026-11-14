package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class FetchRulesTest {
  private static final int SLOTS = 3;
  private static final int RADIUS = 4;
  private static final PickupSpec BASE = new PickupSpec(0, Energy.ZERO, SLOTS, false, RADIUS);
  private static final PickupSpec LOCKED = new PickupSpec(0, Energy.ZERO, SLOTS, false, 0);
  private static final PickupSettings ON = PickupSettings.INITIAL;
  private static final boolean FOLLOWING = true;
  private static final boolean NOT_FOLLOWING = false;
  private static final boolean BUSY = true;
  private static final boolean FREE = false;
  private static final double AT_THE_EDGE = RADIUS;
  private static final double PAST_THE_EDGE = RADIUS + 1;
  private static final double NEAR_DISTANCE = 1;
  private static final double MIDDLE_DISTANCE = RADIUS - 1;
  private static final String NEAR = "near";
  private static final String MIDDLE = "middle";
  private static final String FAR = "far";
  private static final Map<String, Double> TO_OWNER =
      Map.of(NEAR, NEAR_DISTANCE, MIDDLE, MIDDLE_DISTANCE, FAR, PAST_THE_EDGE);
  private static final long START = 100;
  private static final int GIVE_UP = 100;

  @Test
  void anActiveFollowingRobotWithNothingElseToDoFetches() {
    assertTrue(FetchRules.fetches(PowerState.ACTIVE, ON, BASE, FOLLOWING, FREE));
  }

  /** In an area, only pickup counts, even with fetch off (answer 1080). */
  @Test
  void inAnAreaOnlyPickupCounts() {
    PickupSettings fetchOff = ON.toggledFetch();

    assertTrue(FetchRules.fetchesInArea(PowerState.ACTIVE, fetchOff, FREE));
    assertFalse(FetchRules.fetchesInArea(PowerState.ACTIVE, ON.toggledPickup(), FREE));
    assertFalse(FetchRules.fetchesInArea(PowerState.ACTIVE, ON, BUSY));
    assertFalse(FetchRules.fetchesInArea(PowerState.HIBERNATING, ON, FREE));
  }

  @Test
  void onlyAnActiveRobotFetches() {
    for (PowerState state : PowerState.values()) {
      assertEquals(
          state == PowerState.ACTIVE, FetchRules.fetches(state, ON, BASE, FOLLOWING, FREE));
    }
  }

  @Test
  void fetchingNeedsPickupItsSwitchAndAnUnlockedTier() {
    assertFalse(FetchRules.fetches(PowerState.ACTIVE, ON.toggledPickup(), BASE, FOLLOWING, FREE));
    assertFalse(FetchRules.fetches(PowerState.ACTIVE, ON.toggledFetch(), BASE, FOLLOWING, FREE));
    assertFalse(FetchRules.fetches(PowerState.ACTIVE, ON, LOCKED, FOLLOWING, FREE));
  }

  @Test
  void theMagnetSwitchDoesNotMatter() {
    assertTrue(FetchRules.fetches(PowerState.ACTIVE, ON.toggledMagnet(), BASE, FOLLOWING, FREE));
  }

  @Test
  void aRobotThatDoesNotFollowOrIsBusyDoesNotFetch() {
    assertFalse(FetchRules.fetches(PowerState.ACTIVE, ON, BASE, NOT_FOLLOWING, FREE));
    assertFalse(FetchRules.fetches(PowerState.ACTIVE, ON, BASE, FOLLOWING, BUSY));
  }

  @Test
  void anItemIsInReachUpToTheRadiusAroundTheOwner() {
    assertTrue(FetchRules.inReach(AT_THE_EDGE, BASE));
    assertFalse(FetchRules.inReach(PAST_THE_EDGE, BASE));
    assertFalse(FetchRules.inReach(0, LOCKED));
  }

  @Test
  void theTargetIsTheItemNearestToTheOwnerInReach() {
    Optional<String> target = FetchRules.target(List.of(FAR, MIDDLE, NEAR), TO_OWNER::get, BASE);

    assertEquals(Optional.of(NEAR), target);
  }

  @Test
  void withoutAnItemInReachThereIsNoTarget() {
    assertEquals(Optional.empty(), FetchRules.target(List.of(FAR), TO_OWNER::get, BASE));
    assertEquals(Optional.empty(), FetchRules.target(List.of(NEAR), TO_OWNER::get, LOCKED));
  }

  @Test
  void theRobotGivesUpWhenTheTimeRunsOut() {
    assertFalse(FetchRules.gaveUp(START, START + GIVE_UP - 1, GIVE_UP));
    assertTrue(FetchRules.gaveUp(START, START + GIVE_UP, GIVE_UP));
  }
}
