package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class InventoryGridTest {
  private static final int BASE_SLOTS = 27;
  private static final int ADVANCED_SLOTS = 36;
  private static final int ELITE_SLOTS = 45;
  private static final int ULTIMATE_SLOTS = 54;
  private static final int ADVANCED_COLUMNS = 12;
  private static final int ELITE_COLUMNS = 15;
  private static final int ULTIMATE_COLUMNS = 18;
  private static final int THREE_ROWS = 3;
  private static final int SMALL_SLOTS = 5;
  private static final int LAST_SLOT_OF_ULTIMATE = 53;
  private static final int LAST_COLUMN_OF_ULTIMATE = 17;

  @Test
  void everyTierShowsInThreeRowsWithMoreColumns() {
    assertEquals(InventoryGrid.BASE_COLUMNS, new InventoryGrid(BASE_SLOTS).columns());
    assertEquals(ADVANCED_COLUMNS, new InventoryGrid(ADVANCED_SLOTS).columns());
    assertEquals(ELITE_COLUMNS, new InventoryGrid(ELITE_SLOTS).columns());
    assertEquals(ULTIMATE_COLUMNS, new InventoryGrid(ULTIMATE_SLOTS).columns());
    assertEquals(THREE_ROWS, new InventoryGrid(ULTIMATE_SLOTS).rows());
  }

  @Test
  void theExtraColumnsMakeTheScreenWider() {
    assertEquals(0, new InventoryGrid(BASE_SLOTS).extraColumns());
    assertEquals(
        ULTIMATE_COLUMNS - InventoryGrid.BASE_COLUMNS,
        new InventoryGrid(ULTIMATE_SLOTS).extraColumns());
  }

  @Test
  void aSlotFindsItsColumnAndRow() {
    InventoryGrid ultimate = new InventoryGrid(ULTIMATE_SLOTS);

    assertEquals(LAST_COLUMN_OF_ULTIMATE, ultimate.columnOf(LAST_SLOT_OF_ULTIMATE));
    assertEquals(THREE_ROWS - 1, ultimate.rowOf(LAST_SLOT_OF_ULTIMATE));
  }

  @Test
  void aSmallInventoryKeepsTheBaseColumnsInOneRow() {
    assertEquals(1, new InventoryGrid(SMALL_SLOTS).rows());
    assertThrows(IllegalArgumentException.class, () -> new InventoryGrid(-1));
  }
}
