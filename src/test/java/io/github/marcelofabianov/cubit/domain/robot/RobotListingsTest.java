package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RobotListingsTest {
  private static final String BOLT = "Bolt";
  private static final String NUT = "Nut";
  private static final String GEAR = "Gear";
  private static final String ASH = "Ash";
  private static final String ANT = "Ant";
  private static final String PART = "bo";
  private static final String EMPTY = "";
  private static final String OTHER_NAME = "nut";
  private static final boolean FOLLOWING = true;
  private static final boolean NOT_FOLLOWING = false;
  private static final boolean HERE = true;
  private static final boolean ELSEWHERE = false;
  private static final boolean LOADED = true;
  private static final boolean UNLOADED = false;
  private static final double NEAR = 5;
  private static final double FAR = 50;
  private static final RobotListing FOLLOWER =
      new RobotListing(new UUID(0, 1), BOLT, FOLLOWING, HERE, LOADED, FAR);
  private static final RobotListing NEAR_ONE =
      new RobotListing(new UUID(0, 2), NUT, NOT_FOLLOWING, HERE, LOADED, NEAR);
  private static final RobotListing FAR_ONE =
      new RobotListing(new UUID(0, 3), GEAR, NOT_FOLLOWING, HERE, LOADED, FAR);
  private static final RobotListing NETHER =
      new RobotListing(new UUID(0, 4), ASH, NOT_FOLLOWING, ELSEWHERE, LOADED, 0);
  private static final RobotListing ASLEEP =
      new RobotListing(new UUID(0, 5), ANT, NOT_FOLLOWING, HERE, UNLOADED, NEAR);

  @Test
  void theFollowerComesFirstThenTheNearestAndTheRestLast() {
    List<RobotListing> ordered =
        RobotListings.ordered(List.of(ASLEEP, NETHER, FAR_ONE, NEAR_ONE, FOLLOWER));

    assertEquals(List.of(FOLLOWER, NEAR_ONE, FAR_ONE, NETHER, ASLEEP), ordered);
  }

  @Test
  void theSearchIgnoresCaseAndAnEmptySearchKeepsEveryone() {
    assertTrue(RobotListings.matches(BOLT, PART));
    assertTrue(RobotListings.matches(BOLT, EMPTY));
    assertFalse(RobotListings.matches(BOLT, OTHER_NAME));
  }
}
