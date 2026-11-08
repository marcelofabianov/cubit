package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BatteryTest {
  private static final long CAPACITY_AMOUNT = 300_000;
  private static final Energy BASE_CAPACITY = new Energy(CAPACITY_AMOUNT);
  private static final Energy OFFERED = new Energy(5_000);
  private static final Energy TO_CONSUME = new Energy(10);
  private static final long NEARLY_FULL = 299_000;
  private static final Energy ACCEPTED_WHEN_NEARLY_FULL = new Energy(1_000);
  private static final long PARTIAL = 100_000;
  private static final Energy PARTIAL_PLUS_OFFERED = new Energy(105_000);
  private static final long ENOUGH_TO_CONSUME = 25;
  private static final Energy LEFT_AFTER_CONSUMING = new Energy(15);
  private static final long NOT_ENOUGH_TO_CONSUME = 5;
  private static final Energy ALL_THAT_REMAINED = new Energy(5);
  private static final Percentage WAKE_THRESHOLD = new Percentage(5);
  private static final long AT_WAKE_THRESHOLD = 15_000;
  private static final Percentage CRAFT_CHARGE = new Percentage(25);
  private static final Energy CHARGE_AFTER_CRAFT = new Energy(75_000);
  private static final int ABOVE_MAX_PERCENTAGE = 101;
  private static final int BAR_STEPS = 13;
  private static final int BAR_AT_CRAFT_CHARGE = 3;
  private static final long ONE_FE = 1;

  private static Battery charged(long amount) {
    return new Battery(BASE_CAPACITY, new Energy(amount));
  }

  @Test
  void rejectsChargeAboveCapacity() {
    assertThrows(IllegalArgumentException.class, () -> charged(CAPACITY_AMOUNT + 1));
  }

  @Test
  void rejectsZeroCapacity() {
    assertThrows(IllegalArgumentException.class, () -> new Battery(Energy.ZERO, Energy.ZERO));
  }

  @Test
  void acceptsAllTheEnergyThatFits() {
    Battery.Charged result = charged(PARTIAL).recharge(OFFERED);
    assertEquals(PARTIAL_PLUS_OFFERED, result.battery().charge());
    assertEquals(OFFERED, result.accepted());
  }

  @Test
  void acceptsOnlyWhatFitsWithoutLosingTheRest() {
    Battery.Charged result = charged(NEARLY_FULL).recharge(OFFERED);
    assertEquals(BASE_CAPACITY, result.battery().charge());
    assertEquals(ACCEPTED_WHEN_NEARLY_FULL, result.accepted());
  }

  @Test
  void consumesWhatIsAvailable() {
    Battery.Consumed result = charged(ENOUGH_TO_CONSUME).consume(TO_CONSUME);
    assertEquals(LEFT_AFTER_CONSUMING, result.battery().charge());
    assertEquals(TO_CONSUME, result.consumed());
  }

  @Test
  void consumesOnlyWhatRemains() {
    Battery.Consumed result = charged(NOT_ENOUGH_TO_CONSUME).consume(TO_CONSUME);
    assertTrue(result.battery().isEmpty());
    assertEquals(ALL_THAT_REMAINED, result.consumed());
  }

  @Test
  void reportsEmptyAndFull() {
    assertTrue(charged(0).isEmpty());
    assertFalse(charged(1).isEmpty());
    assertTrue(charged(CAPACITY_AMOUNT).isFull());
    assertFalse(charged(CAPACITY_AMOUNT - 1).isFull());
  }

  @Test
  void comparesChargeWithPercentageExactly() {
    assertTrue(charged(AT_WAKE_THRESHOLD).hasAtLeast(WAKE_THRESHOLD));
    assertFalse(charged(AT_WAKE_THRESHOLD - 1).hasAtLeast(WAKE_THRESHOLD));
  }

  @Test
  void createsBatteryFromPercentageOfCapacity() {
    assertEquals(CHARGE_AFTER_CRAFT, Battery.withCharge(BASE_CAPACITY, CRAFT_CHARGE).charge());
  }

  @Test
  void percentageMustBeBetweenZeroAndOneHundred() {
    assertThrows(IllegalArgumentException.class, () -> new Percentage(-1));
    assertThrows(IllegalArgumentException.class, () -> new Percentage(ABOVE_MAX_PERCENTAGE));
  }

  @Test
  void anEmptyBatteryHasLevelZero() {
    assertEquals(0, charged(0).level(BAR_STEPS));
  }

  @Test
  void anyChargeShowsAtLeastOneStep() {
    assertEquals(1, charged(ONE_FE).level(BAR_STEPS));
  }

  @Test
  void aFullBatteryFillsEveryStep() {
    assertEquals(BAR_STEPS, charged(CAPACITY_AMOUNT).level(BAR_STEPS));
  }

  @Test
  void theLevelIsProportionalToTheCharge() {
    assertEquals(BAR_AT_CRAFT_CHARGE, charged(CHARGE_AFTER_CRAFT.amount()).level(BAR_STEPS));
  }

  @Test
  void rejectsALevelWithoutSteps() {
    assertThrows(IllegalArgumentException.class, () -> charged(PARTIAL).level(0));
  }
}
