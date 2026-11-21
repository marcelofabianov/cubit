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

  @Test
  void aNewOrderAsksForOne() {
    DeskOrder order = DeskOrder.start();
    assertTrue(order.isActive());
    assertEquals(ONE, order.total());
    assertEquals(ONE, order.remaining());
  }

  @Test
  void noOrderIsNotActive() {
    assertFalse(DeskOrder.NONE.isActive());
    assertEquals(0, DeskOrder.NONE.remaining());
  }

  @Test
  void plusAddsOneAndShiftAddsEight() {
    assertEquals(THREE, DeskOrder.start().adjust(2, MAX).total());
    assertEquals(MAX, DeskOrder.start().adjust(DeskOrder.SHIFT_STEP, MAX).total());
  }

  @Test
  void theTotalStaysBetweenOneAndTheMaximum() {
    assertEquals(MAX, DeskOrder.start().adjust(NINE, MAX).total());
    assertEquals(ONE, DeskOrder.start().adjust(-DeskOrder.SHIFT_STEP, MAX).total());
  }

  @Test
  void eachCraftCountsAsDone() {
    DeskOrder order = DeskOrder.start().adjust(THREE, MAX).crafted();
    assertEquals(ONE, order.done());
    assertEquals(THREE, order.remaining());
  }

  @Test
  void theLastCraftEndsTheOrder() {
    assertEquals(DeskOrder.NONE, DeskOrder.start().crafted());
  }

  @Test
  void loweringTheTotalToWhatIsDoneEndsTheOrder() {
    DeskOrder order = DeskOrder.start().adjust(THREE, MAX).crafted().crafted().crafted();
    assertEquals(DeskOrder.NONE, order.adjust(-THREE, MAX));
  }

  @Test
  void aSmallerMaximumAfterAConfigChangeLimitsTheTotal() {
    assertEquals(FOUR, DeskOrder.start().adjust(SIX, MAX).limitedTo(FOUR).total());
  }

  @Test
  void theComparatorReadsWhatIsLeft() {
    DeskOrder order = DeskOrder.start().adjust(MAX - ONE, MAX);
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
