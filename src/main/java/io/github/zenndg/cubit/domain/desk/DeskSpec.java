package io.github.zenndg.cubit.domain.desk;

import io.github.zenndg.cubit.domain.energy.Energy;

/**
 * The numbers of an Engineering Desk at a tier: its battery, the ticks of one craft, the slots of
 * each inventory, up to 36, and the largest order (answers 1433, 1434 and 1505).
 */
public record DeskSpec(Energy capacity, int craftTicks, int storageSlots, int maxOrder) {
  public static final int MAX_SLOTS = 36;
  private static final String NO_CAPACITY = "A desk needs a battery";
  private static final String NO_TIME = "A craft needs at least one tick: ";
  private static final String BAD_SLOTS = "The slots of a desk go from 1 to 36: ";
  private static final String NO_ORDER = "A desk needs orders of at least one: ";

  public DeskSpec {
    if (capacity.isZero()) {
      throw new IllegalArgumentException(NO_CAPACITY);
    }
    if (craftTicks < 1) {
      throw new IllegalArgumentException(NO_TIME + craftTicks);
    }
    if (storageSlots < 1 || storageSlots > MAX_SLOTS) {
      throw new IllegalArgumentException(BAD_SLOTS + storageSlots);
    }
    if (maxOrder < 1) {
      throw new IllegalArgumentException(NO_ORDER + maxOrder);
    }
  }
}
