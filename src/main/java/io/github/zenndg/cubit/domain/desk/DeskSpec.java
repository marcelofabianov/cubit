package io.github.zenndg.cubit.domain.desk;

import io.github.zenndg.cubit.domain.energy.Energy;

/**
 * The numbers of an Engineering Desk at a tier: its battery, the ticks of one craft, the slots of
 * each inventory, in whole rows of the screen, and the largest order (answers 1433 and 1434).
 */
public record DeskSpec(Energy capacity, int craftTicks, int storageSlots, int maxOrder) {
  public static final int ROW = 9;
  public static final int MAX_SLOTS = 36;
  private static final String NO_CAPACITY = "A desk needs a battery";
  private static final String NO_TIME = "A craft needs at least one tick: ";
  private static final String BAD_SLOTS = "The slots of a desk are whole rows, up to four: ";
  private static final String NO_ORDER = "A desk needs orders of at least one: ";

  public DeskSpec {
    if (capacity.isZero()) {
      throw new IllegalArgumentException(NO_CAPACITY);
    }
    if (craftTicks < 1) {
      throw new IllegalArgumentException(NO_TIME + craftTicks);
    }
    if (storageSlots < ROW || storageSlots > MAX_SLOTS || storageSlots % ROW != 0) {
      throw new IllegalArgumentException(BAD_SLOTS + storageSlots);
    }
    if (maxOrder < 1) {
      throw new IllegalArgumentException(NO_ORDER + maxOrder);
    }
  }
}
