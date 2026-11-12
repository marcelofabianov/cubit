package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InventoryWindowTest {
  private static final int PER_ROW = 9;
  private static final int VISIBLE_ROWS = 3;
  private static final int BASE_SLOTS = 27;
  private static final int ULTIMATE_SLOTS = 54;
  private static final int SMALL_SLOTS = 18;
  private static final int ULTIMATE_MAX_TOP = 3;
  private static final int FIRST_VIEW_SLOT = 0;
  private static final int LAST_VIEW_SLOT = 26;
  private static final int SECOND_ROW_TOP = 2;
  private static final int SECOND_ROW_FIRST = 18;
  private static final int BEYOND = 99;
  private static final int NEGATIVE = -1;

  private static InventoryWindow window(int slots) {
    return new InventoryWindow(slots, PER_ROW, VISIBLE_ROWS);
  }

  @Test
  void theBaseInventoryFitsWithoutScrolling() {
    InventoryWindow base = window(BASE_SLOTS);

    assertFalse(base.scrolls());
    assertEquals(0, base.maxTopRow());
    assertEquals(BASE_SLOTS, base.visibleSlots());
  }

  @Test
  void theUltimateInventoryScrollsThreeRows() {
    InventoryWindow ultimate = window(ULTIMATE_SLOTS);

    assertTrue(ultimate.scrolls());
    assertEquals(ULTIMATE_MAX_TOP, ultimate.maxTopRow());
    assertEquals(BASE_SLOTS, ultimate.visibleSlots());
  }

  @Test
  void aSmallInventoryShowsOnlyItsRows() {
    InventoryWindow small = window(SMALL_SLOTS);

    assertEquals(SMALL_SLOTS, small.visibleSlots());
    assertEquals(2, small.visibleRows());
  }

  @Test
  void theTopRowStaysWithinTheLimits() {
    InventoryWindow ultimate = window(ULTIMATE_SLOTS);

    assertEquals(0, ultimate.clampTopRow(NEGATIVE));
    assertEquals(ULTIMATE_MAX_TOP, ultimate.clampTopRow(BEYOND));
    assertEquals(SECOND_ROW_TOP, ultimate.clampTopRow(SECOND_ROW_TOP));
  }

  @Test
  void aViewSlotPointsToTheSlotOfItsRow() {
    InventoryWindow ultimate = window(ULTIMATE_SLOTS);

    assertEquals(FIRST_VIEW_SLOT, ultimate.slotOf(FIRST_VIEW_SLOT, 0));
    assertEquals(SECOND_ROW_FIRST, ultimate.slotOf(FIRST_VIEW_SLOT, SECOND_ROW_TOP));
    assertEquals(ULTIMATE_SLOTS - 1, ultimate.slotOf(LAST_VIEW_SLOT, ULTIMATE_MAX_TOP));
  }

  @Test
  void rejectsAnInvalidShape() {
    assertThrows(IllegalArgumentException.class, () -> new InventoryWindow(NEGATIVE, PER_ROW, 1));
    assertThrows(IllegalArgumentException.class, () -> new InventoryWindow(BASE_SLOTS, 0, 1));
    assertThrows(IllegalArgumentException.class, () -> new InventoryWindow(BASE_SLOTS, PER_ROW, 0));
  }
}
