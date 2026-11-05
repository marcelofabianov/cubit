package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class DockStockTest {
  private static final Energy CAPACITY = new Energy(100_000);
  private static final Energy INPUT = new Energy(5_000);
  private static final Energy NEAR_FULL = new Energy(98_000);
  private static final Energy WHAT_FITS = new Energy(2_000);
  private static final Energy DUST = new Energy(5_000);
  private static final Energy BLOCK = new Energy(45_000);
  private static final Energy RATE = new Energy(500);
  private static final Energy ROBOT_CAPACITY = new Energy(300_000);
  private static final Energy ROBOT_NEAR_FULL = new Energy(299_900);
  private static final Energy WHAT_THE_ROBOT_TAKES = new Energy(100);
  private static final Energy LITTLE = new Energy(200);

  private static DockStock stock(Energy amount) {
    return DockStock.of(CAPACITY, amount);
  }

  private static Battery robot(Energy charge) {
    return new Battery(ROBOT_CAPACITY, charge);
  }

  private static long total(DockStock.Transfer transfer) {
    return transfer.stock().energy().charge().amount() + transfer.robot().charge().amount();
  }

  @Test
  void aSavedAmountAboveTheCapacityIsLimited() {
    assertEquals(CAPACITY, stock(CAPACITY.plus(DUST)).energy().charge());
  }

  @Test
  void receivesUpToTheInputLeft() {
    DockStock.Received received = stock(Energy.ZERO).receive(BLOCK, INPUT);
    assertEquals(INPUT, received.accepted());
    assertEquals(INPUT, received.stock().energy().charge());
  }

  @Test
  void receivesNothingWithoutInputLeft() {
    assertEquals(Energy.ZERO, stock(Energy.ZERO).receive(DUST, Energy.ZERO).accepted());
  }

  @Test
  void receivesOnlyWhatFits() {
    DockStock.Received received = stock(NEAR_FULL).receive(INPUT, INPUT);
    assertEquals(WHAT_FITS, received.accepted());
    assertEquals(CAPACITY, received.stock().energy().charge());
  }

  @Test
  void takesAnItemOnlyWhenItFitsWhole() {
    assertTrue(stock(Energy.ZERO).canTake(DUST));
    assertFalse(stock(NEAR_FULL).canTake(DUST));
  }

  @Test
  void anItemWithoutEnergyIsNotTaken() {
    assertFalse(stock(Energy.ZERO).canTake(Energy.ZERO));
  }

  @Test
  void takingAnItemAddsItsEnergy() {
    assertEquals(DUST, stock(Energy.ZERO).take(DUST).energy().charge());
  }

  @Test
  void chargesTheRobotAtItsRate() {
    DockStock.Transfer transfer = stock(DUST).charge(robot(Energy.ZERO), RATE);
    assertEquals(RATE, transfer.robot().charge());
    assertEquals(DUST.minus(RATE), transfer.stock().energy().charge());
  }

  @Test
  void aLowStockGivesOnlyWhatItHas() {
    DockStock.Transfer transfer = stock(LITTLE).charge(robot(Energy.ZERO), RATE);
    assertEquals(LITTLE, transfer.robot().charge());
    assertEquals(Energy.ZERO, transfer.stock().energy().charge());
  }

  @Test
  void aRobotNearlyFullTakesOnlyWhatFits() {
    DockStock.Transfer transfer = stock(DUST).charge(robot(ROBOT_NEAR_FULL), RATE);
    assertEquals(ROBOT_CAPACITY, transfer.robot().charge());
    assertEquals(DUST.minus(WHAT_THE_ROBOT_TAKES), transfer.stock().energy().charge());
  }

  @Test
  void chargingNeitherCreatesNorLosesEnergy() {
    DockStock start = stock(DUST);
    long before = DUST.amount() + ROBOT_NEAR_FULL.amount();
    assertEquals(before, total(start.charge(robot(ROBOT_NEAR_FULL), RATE)));
  }
}
