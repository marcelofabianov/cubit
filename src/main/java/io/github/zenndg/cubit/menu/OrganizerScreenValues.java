package io.github.zenndg.cubit.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerData;

/**
 * The numbers of the Organizer for its screens: the mode, how many orders and marks, the first row
 * shown, and the destination, the face and the amount of each row shown. Each int of a position
 * takes two values, because each value travels in 16 bits.
 */
final class OrganizerScreenValues implements ContainerData {
  static final int ROWS = 5;
  private static final int MODE = 0;
  private static final int COUNT = 1;
  private static final int LIMIT = 2;
  private static final int OFFSET = 3;
  private static final int FIRST_ROW = 4;
  private static final int X = 0;
  private static final int Y = 2;
  private static final int Z = 4;
  private static final int FACE = 6;
  private static final int AMOUNT = 7;
  private static final int PER_ROW = 8;
  private static final int SIZE = FIRST_ROW + ROWS * PER_ROW;
  private static final int HALF_BITS = 16;
  private static final int HALF_MASK = 0xFFFF;

  private final int[] values = new int[SIZE];

  /** On the server, from the control and the first row the screen shows. */
  void refresh(OrganizerControl control, int offset) {
    values[MODE] = control.organizerMode().map(mode -> mode.ordinal() + 1).orElse(0);
    values[COUNT] = control.rows().size();
    values[LIMIT] = control.limit();
    values[OFFSET] = offset;
    for (int row = 0; row < ROWS; row++) {
      int index = offset + row;
      SupplyRow shown = index < control.rows().size() ? control.rows().get(index) : null;
      int base = FIRST_ROW + row * PER_ROW;
      putInt(base + X, shown == null ? 0 : shown.pos().getX());
      putInt(base + Y, shown == null ? 0 : shown.pos().getY());
      putInt(base + Z, shown == null ? 0 : shown.pos().getZ());
      values[base + FACE] = shown == null ? 0 : shown.face();
      values[base + AMOUNT] = shown == null ? 0 : shown.amount();
    }
  }

  private void putInt(int index, int value) {
    values[index] = value & HALF_MASK;
    values[index + 1] = value >>> HALF_BITS;
  }

  private static int intAt(ContainerData data, int index) {
    return (data.get(index + 1) << HALF_BITS) | (data.get(index) & HALF_MASK);
  }

  /** The mode by its order plus one, or zero without the card. */
  static int mode(ContainerData data) {
    return data.get(MODE);
  }

  static int count(ContainerData data) {
    return data.get(COUNT);
  }

  static int limit(ContainerData data) {
    return data.get(LIMIT);
  }

  static int offset(ContainerData data) {
    return data.get(OFFSET);
  }

  static BlockPos pos(ContainerData data, int row) {
    int base = FIRST_ROW + row * PER_ROW;
    return new BlockPos(intAt(data, base + X), intAt(data, base + Y), intAt(data, base + Z));
  }

  static int face(ContainerData data, int row) {
    return data.get(FIRST_ROW + row * PER_ROW + FACE);
  }

  static int amount(ContainerData data, int row) {
    return data.get(FIRST_ROW + row * PER_ROW + AMOUNT);
  }

  @Override
  public int get(int index) {
    return values[index];
  }

  /** The server never takes values from the client, but the client copy takes the server ones. */
  @Override
  public void set(int index, int value) {
    values[index] = value;
  }

  @Override
  public int getCount() {
    return SIZE;
  }
}
