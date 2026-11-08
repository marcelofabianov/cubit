package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class EnergyReadingTest {
  private static final long BASE_BATTERY = 300_000;
  private static final long SOME_CHARGE = 222_000;
  private static final int SOME_PERCENT = 74;
  private static final int FULL = 100;
  private static final long TINY_CHARGE = 1;
  private static final long PAST_CAPACITY = 400_000;
  private static final long NEGATIVE = -5;

  @Test
  void readsAPercentage() {
    EnergyReading reading = EnergyReading.capped(SOME_CHARGE, BASE_BATTERY);

    assertEquals(SOME_PERCENT, reading.percent());
    assertEquals(SOME_CHARGE, reading.charge().amount());
    assertEquals(BASE_BATTERY, reading.capacity().amount());
  }

  @Test
  void neverShowsZeroWhileSomeEnergyIsLeft() {
    assertEquals(1, EnergyReading.capped(TINY_CHARGE, BASE_BATTERY).percent());
    assertEquals(0, EnergyReading.capped(0, BASE_BATTERY).percent());
  }

  @Test
  void aFullChargeIsAHundred() {
    assertEquals(FULL, EnergyReading.capped(BASE_BATTERY, BASE_BATTERY).percent());
  }

  @Test
  void readDataIsCappedToSafeValues() {
    assertEquals(FULL, EnergyReading.capped(PAST_CAPACITY, BASE_BATTERY).percent());
    assertEquals(0, EnergyReading.capped(NEGATIVE, BASE_BATTERY).percent());
    assertEquals(0, EnergyReading.capped(SOME_CHARGE, NEGATIVE).percent());
  }

  @Test
  void rejectsAChargePastTheCapacity() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new EnergyReading(new Energy(PAST_CAPACITY), new Energy(BASE_BATTERY)));
  }
}
