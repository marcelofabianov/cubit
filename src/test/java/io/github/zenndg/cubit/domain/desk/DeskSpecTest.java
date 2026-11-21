package io.github.zenndg.cubit.domain.desk;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.zenndg.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class DeskSpecTest {
  private static final Energy CAPACITY = new Energy(100_000);
  private static final int CRAFT_TICKS = 100;
  private static final int SLOTS = 9;
  private static final int NOT_A_ROW = 10;
  private static final int TOO_MANY = 63;
  private static final int MAX_ORDER = 8;

  @Test
  void acceptsTheBaseNumbers() {
    assertDoesNotThrow(() -> new DeskSpec(CAPACITY, CRAFT_TICKS, SLOTS, MAX_ORDER));
  }

  @Test
  void rejectsAnEmptyBattery() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new DeskSpec(Energy.ZERO, CRAFT_TICKS, SLOTS, MAX_ORDER));
  }

  @Test
  void rejectsACraftWithoutTime() {
    assertThrows(IllegalArgumentException.class, () -> new DeskSpec(CAPACITY, 0, SLOTS, MAX_ORDER));
  }

  @Test
  void rejectsSlotsThatAreNotWholeRows() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new DeskSpec(CAPACITY, CRAFT_TICKS, NOT_A_ROW, MAX_ORDER));
    assertThrows(
        IllegalArgumentException.class,
        () -> new DeskSpec(CAPACITY, CRAFT_TICKS, TOO_MANY, MAX_ORDER));
  }

  @Test
  void rejectsAnOrderOfNothing() {
    assertThrows(
        IllegalArgumentException.class, () -> new DeskSpec(CAPACITY, CRAFT_TICKS, SLOTS, 0));
  }
}
