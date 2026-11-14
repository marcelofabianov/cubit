package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.InventoryGrid;

/**
 * Where the screen of the robot puts its slots and whether they can be used: the screen of the
 * robot, and the Inventory tab of the Tablet, only to read, with the energy slot first in the row
 * of the exclusive slots, since the bars it sat by are in the header there (answers 999 and 1104).
 */
public enum RobotMenuLayout {
  ROBOT(false, false),
  TABLET(true, true);

  private static final int TABLET_FILTER_COLUMNS = 15;

  private final boolean readOnly;
  private final boolean energyInRow;

  RobotMenuLayout(boolean readOnly, boolean energyInRow) {
    this.readOnly = readOnly;
    this.energyInRow = energyInRow;
  }

  public boolean readOnly() {
    return readOnly;
  }

  public int energySlotX() {
    return energyInRow ? RobotMenu.FIRST_SLOT_X : RobotMenu.ENERGY_SLOT_X;
  }

  public int energySlotY() {
    return energyInRow ? RobotMenu.EXCLUSIVE_SLOT_Y : RobotMenu.ENERGY_SLOT_Y;
  }

  /**
   * The columns of the pickup filter: as many as the inventory of the robot, which grow with the
   * window, and on the Tablet as many as its window of 300 px holds (answer 1105).
   */
  public int filterColumns(InventoryGrid grid) {
    return energyInRow ? Math.max(grid.columns(), TABLET_FILTER_COLUMNS) : grid.columns();
  }

  /** The torches come first in the row, or right after the energy slot. */
  public int torchSlotX() {
    return RobotMenu.FIRST_SLOT_X + (energyInRow ? RobotMenu.SLOT_SIZE : 0);
  }
}
