package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SlotRechargeTest {
  private static final Energy CAPACITY = new Energy(10_000);
  private static final Energy RATE = new Energy(500);
  private static final Energy DUST = new Energy(5_000);
  private static final Energy AFTER_ONE_TICK = new Energy(4_500);
  private static final Energy NEAR_FULL = new Energy(9_800);
  private static final Energy REMAINING_NEAR_FULL = new Energy(4_800);
  private static final Energy LITTLE = new Energy(100);
  private static final Energy TOO_FULL_FOR_DUST = new Energy(5_001);

  private static Battery batteryWith(Energy charge) {
    return new Battery(CAPACITY, charge);
  }

  @Test
  void oneTickMovesTheRateFromTheReserveToTheBattery() {
    SlotRecharge.Step step = new SlotRecharge(DUST).charge(batteryWith(Energy.ZERO), RATE);

    assertEquals(RATE, step.battery().charge());
    assertEquals(new SlotRecharge(AFTER_ONE_TICK), step.slot());
  }

  @Test
  void whatDoesNotFitStaysInTheReserve() {
    SlotRecharge.Step step = new SlotRecharge(DUST).charge(batteryWith(NEAR_FULL), RATE);

    assertEquals(CAPACITY, step.battery().charge());
    assertEquals(new SlotRecharge(REMAINING_NEAR_FULL), step.slot());
  }

  @Test
  void aSmallReserveMovesOnlyWhatItHas() {
    SlotRecharge.Step step = new SlotRecharge(LITTLE).charge(batteryWith(Energy.ZERO), RATE);

    assertEquals(LITTLE, step.battery().charge());
    assertEquals(SlotRecharge.EMPTY, step.slot());
  }

  @Test
  void anItemIsTakenOnlyWithAnEmptyReserveAndWhenItsEnergyFits() {
    assertTrue(SlotRecharge.EMPTY.canTake(DUST, batteryWith(Energy.ZERO)));
    assertTrue(SlotRecharge.EMPTY.canTake(DUST, batteryWith(DUST)));
    assertFalse(SlotRecharge.EMPTY.canTake(DUST, batteryWith(TOO_FULL_FOR_DUST)));
    assertFalse(new SlotRecharge(LITTLE).canTake(DUST, batteryWith(Energy.ZERO)));
  }

  @Test
  void takingAnItemFillsTheReserve() {
    assertEquals(new SlotRecharge(DUST), SlotRecharge.EMPTY.take(DUST));
  }
}
