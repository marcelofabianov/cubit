package io.github.zenndg.cubit.domain.desk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.zenndg.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class DeskStockTest {
  private static final Energy CAPACITY = new Energy(100_000);
  private static final Energy INPUT = new Energy(5_000);
  private static final Energy NEAR_FULL = new Energy(98_000);
  private static final Energy WHAT_FITS = new Energy(2_000);
  private static final Energy DUST = new Energy(5_000);
  private static final Energy BLOCK = new Energy(45_000);
  private static final Energy COST = new Energy(2_500);

  private static DeskStock stock(Energy amount) {
    return DeskStock.of(CAPACITY, amount);
  }

  @Test
  void aSavedAmountAboveTheCapacityIsLimited() {
    assertEquals(CAPACITY, stock(CAPACITY.plus(DUST)).charge());
  }

  @Test
  void receivesUpToTheInputLeftAndWhatFits() {
    assertEquals(INPUT, stock(Energy.ZERO).receive(BLOCK, INPUT).accepted());
    assertEquals(WHAT_FITS, stock(NEAR_FULL).receive(INPUT, INPUT).accepted());
  }

  @Test
  void takesRedstoneOnlyWhenItFitsWhole() {
    assertTrue(stock(Energy.ZERO).canTake(DUST));
    assertFalse(stock(NEAR_FULL).canTake(DUST));
    assertFalse(stock(Energy.ZERO).canTake(Energy.ZERO));
    assertEquals(DUST, stock(Energy.ZERO).take(DUST).charge());
  }

  @Test
  void paysTheCraftOnlyWithEnoughEnergy() {
    assertTrue(stock(COST).canPay(COST));
    assertFalse(stock(WHAT_FITS).canPay(COST));
    assertEquals(DUST.minus(COST), stock(DUST).pay(COST).charge());
  }

  @Test
  void aNewCapacityKeepsTheCharge() {
    Energy bigger = CAPACITY.plus(CAPACITY);
    assertEquals(DUST, stock(DUST).withCapacity(bigger).charge());
    assertEquals(bigger, stock(DUST).withCapacity(bigger).capacity());
  }
}
