package io.github.zenndg.cubit.domain.desk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DeskOrderTest {
  private static final int MAX = 8;
  private static final int ONE = 1;
  private static final int THREE = 3;
  private static final int FOUR = 4;
  private static final int SIX = 6;
  private static final int NINE = 9;
  private static final int FULL_SIGNAL = 15;
  private static final int HALF_SIGNAL = 8;
  private static final int NO_SIGNAL = 0;

  private static DeskOrder first() {
    return DeskOrder.NONE.adjust(DeskOrder.STEP, MAX);
  }

  @Test
  void plusOnNoOrderAsksForOne() {
    DeskOrder order = first();
    assertTrue(order.isActive());
    assertEquals(ONE, order.total());
    assertEquals(ONE, order.remaining());
  }

  @Test
  void plusWithShiftOnNoOrderAsksForEight() {
    assertEquals(DeskOrder.SHIFT_STEP, DeskOrder.NONE.adjust(DeskOrder.SHIFT_STEP, MAX).total());
  }

  @Test
  void noOrderIsNotActive() {
    assertFalse(DeskOrder.NONE.isActive());
    assertEquals(0, DeskOrder.NONE.remaining());
  }

  @Test
  void plusAddsOneAndShiftAddsEight() {
    assertEquals(THREE, first().adjust(2, MAX).total());
    assertEquals(MAX, first().adjust(DeskOrder.SHIFT_STEP, MAX).total());
  }

  @Test
  void theTotalStaysBetweenOneAndTheMaximum() {
    assertEquals(MAX, first().adjust(NINE, MAX).total());
    assertEquals(ONE, first().adjust(-DeskOrder.SHIFT_STEP, MAX).total());
  }

  @Test
  void eachCraftCountsAsDone() {
    DeskOrder order = first().adjust(THREE, MAX).crafted();
    assertEquals(ONE, order.done());
    assertEquals(THREE, order.remaining());
  }

  @Test
  void theLastCraftEndsTheOrder() {
    assertEquals(DeskOrder.NONE, first().crafted());
  }

  @Test
  void loweringTheTotalToWhatIsDoneEndsTheOrder() {
    DeskOrder order = first().adjust(THREE, MAX).crafted().crafted().crafted();
    assertEquals(DeskOrder.NONE, order.adjust(-THREE, MAX));
  }

  @Test
  void aSmallerMaximumAfterAConfigChangeLimitsTheTotal() {
    assertEquals(FOUR, first().adjust(SIX, MAX).limitedTo(FOUR).total());
  }

  @Test
  void theComparatorReadsWhatIsLeft() {
    DeskOrder order = first().adjust(MAX - ONE, MAX);
    assertEquals(FULL_SIGNAL, order.comparatorSignal());
    for (int i = 0; i < FOUR; i++) {
      order = order.crafted();
    }
    assertEquals(HALF_SIGNAL, order.comparatorSignal());
    assertEquals(NO_SIGNAL, DeskOrder.NONE.comparatorSignal());
  }

  @Test
  void rejectsMoreDoneThanAsked() {
    assertThrows(IllegalArgumentException.class, () -> new DeskOrder(ONE, THREE));
  }
}
