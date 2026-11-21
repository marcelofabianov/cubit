package io.github.zenndg.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.zenndg.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class DockSpecTest {
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Energy RECHARGE = new Energy(500);
  private static final int ROWS_OF_THREE = 27;
  private static final int NOT_A_ROW = 28;
  private static final int TOO_MANY = 63;
  private static final int ROWS = 3;

  /** The numbers of the Base (answer 1374), with the slots in whole rows of the screen. */
  @Test
  void aSpecKeepsItsNumbersAndCountsItsRows() {
    DockSpec spec = new DockSpec(CAPACITY, RECHARGE, ROWS_OF_THREE);

    assertEquals(CAPACITY, spec.capacity());
    assertEquals(RECHARGE, spec.rechargePerTick());
    assertEquals(ROWS, spec.storageRows());
  }

  @Test
  void anInvalidSpecIsRejected() {
    assertThrows(
        IllegalArgumentException.class, () -> new DockSpec(Energy.ZERO, RECHARGE, ROWS_OF_THREE));
    assertThrows(
        IllegalArgumentException.class, () -> new DockSpec(CAPACITY, Energy.ZERO, ROWS_OF_THREE));
    assertThrows(IllegalArgumentException.class, () -> new DockSpec(CAPACITY, RECHARGE, NOT_A_ROW));
    assertThrows(IllegalArgumentException.class, () -> new DockSpec(CAPACITY, RECHARGE, TOO_MANY));
  }
}
