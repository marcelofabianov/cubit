package io.github.zenndg.cubit.domain.desk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DeskStatusTest {
  private static final DeskCheck READY = new DeskCheck(true, false, true, true, true, true);

  @Test
  void craftsWhenEverythingIsThere() {
    assertEquals(DeskStatus.CRAFTING, DeskStatus.decide(READY));
    assertTrue(DeskStatus.CRAFTING.isWorking());
  }

  @Test
  void withoutAnOrderItIsIdle() {
    DeskCheck check = new DeskCheck(false, true, false, false, false, false);
    assertEquals(DeskStatus.IDLE, DeskStatus.decide(check));
    assertFalse(DeskStatus.IDLE.isPaused());
  }

  @Test
  void theRedstoneSignalComesFirst() {
    DeskCheck check = new DeskCheck(true, true, false, false, false, false);
    assertEquals(DeskStatus.REDSTONE, DeskStatus.decide(check));
  }

  @Test
  void missingMaterialsComeBeforeEnergy() {
    DeskCheck check = new DeskCheck(true, false, false, true, false, true);
    assertEquals(DeskStatus.MISSING_MATERIALS, DeskStatus.decide(check));
  }

  @Test
  void aRefusedRecipeIsShown() {
    DeskCheck check = new DeskCheck(true, false, true, false, true, true);
    assertEquals(DeskStatus.REFUSED, DeskStatus.decide(check));
  }

  @Test
  void withoutEnergyItPauses() {
    DeskCheck check = new DeskCheck(true, false, true, true, false, true);
    assertEquals(DeskStatus.NO_ENERGY, DeskStatus.decide(check));
    assertTrue(DeskStatus.NO_ENERGY.isPaused());
  }

  @Test
  void aFullOutputPauses() {
    DeskCheck check = new DeskCheck(true, false, true, true, true, false);
    assertEquals(DeskStatus.OUTPUT_FULL, DeskStatus.decide(check));
  }
}
