package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class DamagingBlockReactionTest {
  @Test
  void anActiveRobotLeavesADamagingBlock() {
    assertTrue(DamagingBlockReaction.shouldLeave(PowerState.ACTIVE, true, false, false));
  }

  @Test
  void aRobotOnASafeBlockStays() {
    assertFalse(DamagingBlockReaction.shouldLeave(PowerState.ACTIVE, false, false, false));
  }

  @Test
  void fireComesFirst() {
    assertFalse(DamagingBlockReaction.shouldLeave(PowerState.ACTIVE, true, true, false));
  }

  @Test
  void lavaComesFirst() {
    assertFalse(DamagingBlockReaction.shouldLeave(PowerState.ACTIVE, true, false, true));
  }

  @Test
  void onlyAnActiveRobotReacts() {
    for (PowerState state : PowerState.values()) {
      if (state != PowerState.ACTIVE) {
        assertFalse(DamagingBlockReaction.shouldLeave(state, true, false, false));
      }
    }
  }
}
