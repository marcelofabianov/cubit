package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VoidRescueTargetTest {
  @Test
  void theSafePositionComesFirst() {
    assertEquals(VoidRescueTarget.SAFE_POSITION, VoidRescueTarget.decide(true, true, true));
    assertEquals(VoidRescueTarget.SAFE_POSITION, VoidRescueTarget.decide(true, false, false));
  }

  @Test
  void withoutASafePositionTheOnlineOwnerReceivesTheItem() {
    assertEquals(VoidRescueTarget.OWNER, VoidRescueTarget.decide(false, true, true));
    assertEquals(VoidRescueTarget.OWNER, VoidRescueTarget.decide(false, true, false));
  }

  @Test
  void withoutTheOwnerTheItemGoesToTheWorldSpawn() {
    assertEquals(VoidRescueTarget.WORLD_SPAWN, VoidRescueTarget.decide(false, false, true));
  }

  @Test
  void withoutAnyLoadedDestinationTheItemWaits() {
    assertEquals(VoidRescueTarget.WAIT, VoidRescueTarget.decide(false, false, false));
  }
}
