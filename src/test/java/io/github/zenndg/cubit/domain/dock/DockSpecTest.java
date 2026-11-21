package io.github.zenndg.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.zenndg.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class DockSpecTest {
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Energy RECHARGE = new Energy(500);
  private static final Energy SOLAR = new Energy(25);
  private static final int ROWS_OF_THREE = 27;
  private static final int NOT_A_ROW = 28;
  private static final int TOO_MANY = 63;
  private static final int ROWS = 3;

  /** The numbers of the Base (1374 and 1385), with the slots in whole rows of the screen. */
  @Test
  void aSpecKeepsItsNumbersAndCountsItsRows() {
    DockSpec spec = new DockSpec(CAPACITY, RECHARGE, ROWS_OF_THREE, SOLAR);

    assertEquals(CAPACITY, spec.capacity());
    assertEquals(RECHARGE, spec.rechargePerTick());
    assertEquals(ROWS, spec.storageRows());
    assertEquals(SOLAR, spec.solarPerTick());
  }

  @Test
  void anInvalidSpecIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new DockSpec(Energy.ZERO, RECHARGE, ROWS_OF_THREE, SOLAR));
    assertThrows(
        IllegalArgumentException.class,
        () -> new DockSpec(CAPACITY, Energy.ZERO, ROWS_OF_THREE, SOLAR));
    assertThrows(
        IllegalArgumentException.class, () -> new DockSpec(CAPACITY, RECHARGE, NOT_A_ROW, SOLAR));
    assertThrows(
        IllegalArgumentException.class, () -> new DockSpec(CAPACITY, RECHARGE, TOO_MANY, SOLAR));
  }
}
