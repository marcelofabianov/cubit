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
  private static final int FETCH_RADIUS = 4;

  @Test
  void aRadiusOfZeroLocksTheMagnet() {
    assertFalse(new PickupSpec(0, Energy.ZERO, SLOTS, false, FETCH_RADIUS).magnetUnlocked());
    assertTrue(new PickupSpec(RADIUS, COST, SLOTS, false, FETCH_RADIUS).magnetUnlocked());
    assertFalse(PickupSpec.NONE.magnetUnlocked());
  }

  @Test
  void aFetchRadiusOfZeroLocksTheFetch() {
    assertTrue(new PickupSpec(0, Energy.ZERO, SLOTS, false, FETCH_RADIUS).fetchUnlocked());
    assertFalse(new PickupSpec(RADIUS, COST, SLOTS, false, 0).fetchUnlocked());
    assertFalse(PickupSpec.NONE.fetchUnlocked());
  }

  @Test
  void negativeNumbersAreRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new PickupSpec(NEGATIVE, COST, SLOTS, false, FETCH_RADIUS));
    assertThrows(
        IllegalArgumentException.class,
        () -> new PickupSpec(RADIUS, COST, NEGATIVE, false, FETCH_RADIUS));
    assertThrows(
        IllegalArgumentException.class, () -> new PickupSpec(RADIUS, COST, SLOTS, false, NEGATIVE));
  }
}
