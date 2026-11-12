package io.github.marcelofabianov.cubit.domain.robot;

/**
 * The rows of the robot inventory that the screen shows at once. A bigger inventory scrolls, so the
 * screen keeps the height of the Base one and never grows past the game window (answer 838).
 */
public record InventoryWindow(int slots, int perRow, int maxVisibleRows) {
  private static final String SLOTS_MESSAGE = "The slots cannot be negative: ";
  private static final String SHAPE_MESSAGE = "A row and the window need at least one slot: ";
  private static final String SEPARATOR = ", ";

  public InventoryWindow {
    if (slots < 0) {
      throw new IllegalArgumentException(SLOTS_MESSAGE + slots);
    }
    if (perRow < 1 || maxVisibleRows < 1) {
      throw new IllegalArgumentException(SHAPE_MESSAGE + perRow + SEPARATOR + maxVisibleRows);
    }
  }

  public int rows() {
    return (slots + perRow - 1) / perRow;
  }

  public int visibleRows() {
    return Math.min(rows(), maxVisibleRows);
  }

  public int visibleSlots() {
    return Math.min(slots, visibleRows() * perRow);
  }

  public boolean scrolls() {
    return rows() > maxVisibleRows;
  }

  public int maxTopRow() {
    return Math.max(rows() - maxVisibleRows, 0);
  }

  public int clampTopRow(int topRow) {
    return Math.clamp(topRow, 0, maxTopRow());
  }

  /** The slot of the inventory that a slot of the screen shows, with this row on top. */
  public int slotOf(int viewSlot, int topRow) {
    return viewSlot + clampTopRow(topRow) * perRow;
  }
}
