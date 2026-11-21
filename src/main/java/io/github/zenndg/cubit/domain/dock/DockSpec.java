package io.github.zenndg.cubit.domain.dock;

import io.github.zenndg.cubit.domain.energy.Energy;

/**
 * The numbers of an Induction Dock at a tier: the stock, the recharge of the Cubit and of the
 * stored Power Bank, the slots of the storage, in whole rows of the screen, and what the panel of a
 * Solar Dock gives under the sun, which may be zero (answers 1374 and 1385).
 */
public record DockSpec(
    Energy capacity, Energy rechargePerTick, int storageSlots, Energy solarPerTick) {
  public static final int ROW = 9;
  private static final String NO_CAPACITY = "A dock needs a stock";
  private static final String NO_RECHARGE = "A dock needs a recharge";
  private static final String BAD_SLOTS = "The slots of a dock are whole rows, up to six: ";

  public DockSpec {
    if (capacity.isZero()) {
      throw new IllegalArgumentException(NO_CAPACITY);
    }
    if (rechargePerTick.isZero()) {
      throw new IllegalArgumentException(NO_RECHARGE);
    }
    if (storageSlots < ROW
        || storageSlots > DockStorageRules.MAX_SLOTS
        || storageSlots % ROW != 0) {
      throw new IllegalArgumentException(BAD_SLOTS + storageSlots);
    }
  }

  public int storageRows() {
    return storageSlots / ROW;
  }
}
