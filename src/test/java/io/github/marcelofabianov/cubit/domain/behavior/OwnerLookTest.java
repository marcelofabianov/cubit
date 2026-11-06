package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class OwnerLookTest {
  private static final double NEAR = 3;
  private static final double AT_THE_LIMIT = 8;
  private static final double TOO_FAR = 8.1;

  @Test
  void anActiveRobotLooksAtAnOwnerNearby() {
    assertTrue(OwnerLook.applies(PowerState.ACTIVE, true, NEAR));
    assertTrue(OwnerLook.applies(PowerState.ACTIVE, true, AT_THE_LIMIT));
  }

  @Test
  void anActiveRobotIgnoresAnOwnerTooFar() {
    assertFalse(OwnerLook.applies(PowerState.ACTIVE, true, TOO_FAR));
  }

  @Test
  void anActiveRobotIgnoresAnOwnerInAnotherDimension() {
    assertFalse(OwnerLook.applies(PowerState.ACTIVE, false, NEAR));
  }

  @Test
  void onlyAnActiveRobotLooks() {
    assertFalse(OwnerLook.applies(PowerState.INACTIVE_EXHAUSTED, true, NEAR));
    assertFalse(OwnerLook.applies(PowerState.INACTIVE_SWITCHED_OFF, true, NEAR));
    assertFalse(OwnerLook.applies(PowerState.HIBERNATING, true, NEAR));
  }

  @Test
  void anInvalidDistanceMeansNoLook() {
    assertFalse(OwnerLook.applies(PowerState.ACTIVE, true, Double.NaN));
  }
}
