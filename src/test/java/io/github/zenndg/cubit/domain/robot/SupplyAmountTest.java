package io.github.zenndg.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class SupplyAmountTest {
  private static final int UP = 1;
  private static final int DOWN = -1;
  private static final boolean ONE = false;
  private static final boolean EIGHT = true;
  private static final int TEN = 10;
  private static final int NINE = 9;
  private static final int ELEVEN = 11;
  private static final int EIGHTEEN = 18;
  private static final int SIXTY = 60;
  private static final int FOUR = 4;
  private static final int TWO = 2;
  private static final int TOO_MANY = 65;

  /** − and + go 1 at a time and, with Shift, 8, from 1 to 64 (answer 1300). */
  @Test
  void theButtonsStepByOneOrByEight() {
    SupplyAmount ten = new SupplyAmount(TEN);
    assertEquals(new SupplyAmount(ELEVEN), ten.step(UP, ONE));
    assertEquals(new SupplyAmount(NINE), ten.step(DOWN, ONE));
    assertEquals(new SupplyAmount(EIGHTEEN), ten.step(UP, EIGHT));
    assertEquals(new SupplyAmount(TWO), ten.step(DOWN, EIGHT));
    assertEquals(SupplyAmount.MIN, new SupplyAmount(FOUR).step(DOWN, EIGHT));
    assertEquals(SupplyAmount.MAX, new SupplyAmount(SIXTY).step(UP, EIGHT));
  }

  /** It completes the destination up to the amount and never past it (answer 1294). */
  @Test
  void whatIsMissingNeverPassesTheAmount() {
    assertEquals(TEN - FOUR, new SupplyAmount(TEN).missing(FOUR));
    assertEquals(0, new SupplyAmount(TEN).missing(ELEVEN));
  }

  @Test
  void rejectsAmountsOutsideTheRange() {
    assertThrows(IllegalArgumentException.class, () -> new SupplyAmount(0));
    assertThrows(IllegalArgumentException.class, () -> new SupplyAmount(TOO_MANY));
  }
}
