package io.github.marcelofabianov.cubit.domain.robot;

/**
 * How the inventory of the robot shows on the screen: always three rows, as the Base, and more
 * columns in the bigger tiers, so the whole inventory shows at once and the screen only grows
 * sideways: 9, 12, 15 and 18 columns for 27, 36, 45 and 54 slots (answer 1101).
 */
public record InventoryGrid(int slots) {
  public static final int BASE_COLUMNS = 9;
  public static final int ROWS = 3;
  private static final String SLOTS_MESSAGE = "The slots cannot be negative: ";

  public InventoryGrid {
    if (slots < 0) {
      throw new IllegalArgumentException(SLOTS_MESSAGE + slots);
    }
  }

  public int columns() {
    return Math.max(BASE_COLUMNS, (slots + ROWS - 1) / ROWS);
  }

  /** Three rows, or fewer only for an inventory smaller than the Base one. */
  public int rows() {
    return Math.max((slots + columns() - 1) / columns(), 1);
  }

  /** The columns past the nine of the Base, which make the screen wider. */
  public int extraColumns() {
    return columns() - BASE_COLUMNS;
  }

  public int columnOf(int slot) {
    return slot % columns();
  }

  public int rowOf(int slot) {
    return slot / columns();
  }
}
