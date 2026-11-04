package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BatteryTest {
  private static final Energy BASE_CAPACITY = new Energy(300_000);

  private static Battery charged(long amount) {
    return new Battery(BASE_CAPACITY, new Energy(amount));
  }

  @Test
  void rejectsChargeAboveCapacity() {
    assertThrows(IllegalArgumentException.class, () -> charged(300_001));
  }

  @Test
  void rejectsZeroCapacity() {
    assertThrows(IllegalArgumentException.class, () -> new Battery(Energy.ZERO, Energy.ZERO));
  }

  @Test
  void acceptsAllTheEnergyThatFits() {
    Battery.Charged result = charged(100_000).recharge(new Energy(5_000));
    assertEquals(new Energy(105_000), result.battery().charge());
    assertEquals(new Energy(5_000), result.accepted());
  }

  @Test
  void acceptsOnlyWhatFitsWithoutLosingTheRest() {
    Battery.Charged result = charged(299_000).recharge(new Energy(5_000));
    assertEquals(BASE_CAPACITY, result.battery().charge());
    assertEquals(new Energy(1_000), result.accepted());
  }

  @Test
  void consumesWhatIsAvailable() {
    Battery.Consumed result = charged(25).consume(new Energy(10));
    assertEquals(new Energy(15), result.battery().charge());
    assertEquals(new Energy(10), result.consumed());
  }

  @Test
  void consumesOnlyWhatRemains() {
    Battery.Consumed result = charged(5).consume(new Energy(10));
    assertTrue(result.battery().isEmpty());
    assertEquals(new Energy(5), result.consumed());
  }

  @Test
  void reportsEmptyAndFull() {
    assertTrue(charged(0).isEmpty());
    assertFalse(charged(1).isEmpty());
    assertTrue(charged(300_000).isFull());
    assertFalse(charged(299_999).isFull());
  }

  @Test
  void comparesChargeWithPercentageExactly() {
    assertTrue(charged(15_000).hasAtLeast(new Percentage(5)));
    assertFalse(charged(14_999).hasAtLeast(new Percentage(5)));
  }

  @Test
  void createsBatteryFromPercentageOfCapacity() {
    assertEquals(
        new Energy(75_000), Battery.withCharge(BASE_CAPACITY, new Percentage(25)).charge());
  }

  @Test
  void percentageMustBeBetweenZeroAndOneHundred() {
    assertThrows(IllegalArgumentException.class, () -> new Percentage(-1));
    assertThrows(IllegalArgumentException.class, () -> new Percentage(101));
  }
}
