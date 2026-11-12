package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class PickupSpecTest {
  private static final Energy COST = new Energy(20);
  private static final int RADIUS = 3;
  private static final int SLOTS = 9;
  private static final int NEGATIVE = -1;

  @Test
  void aRadiusOfZeroLocksTheMagnet() {
    assertFalse(new PickupSpec(0, Energy.ZERO, SLOTS, false).magnetUnlocked());
    assertTrue(new PickupSpec(RADIUS, COST, SLOTS, false).magnetUnlocked());
    assertFalse(PickupSpec.NONE.magnetUnlocked());
  }

  @Test
  void negativeNumbersAreRejected() {
    assertThrows(
        IllegalArgumentException.class, () -> new PickupSpec(NEGATIVE, COST, SLOTS, false));
    assertThrows(
        IllegalArgumentException.class, () -> new PickupSpec(RADIUS, COST, NEGATIVE, false));
  }
}
