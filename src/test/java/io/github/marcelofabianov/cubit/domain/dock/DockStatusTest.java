package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DockStatusTest {
  @Test
  void withoutEnergyAndWithoutARobotItIsOff() {
    assertEquals(DockStatus.OFF, DockStatus.decide(true, false, false));
  }

  @Test
  void withEnergyAndWithoutARobotItIsIdle() {
    assertEquals(DockStatus.IDLE, DockStatus.decide(false, false, false));
  }

  @Test
  void aRobotReceivingEnergyIsCharging() {
    assertEquals(DockStatus.CHARGING, DockStatus.decide(false, true, false));
  }

  @Test
  void aRobotWithAFullBatteryIsComplete() {
    assertEquals(DockStatus.COMPLETE, DockStatus.decide(false, true, true));
  }

  @Test
  void aFullRobotIsCompleteEvenWithoutEnergyInTheStock() {
    assertEquals(DockStatus.COMPLETE, DockStatus.decide(true, true, true));
  }

  @Test
  void aRobotWithAnEmptyStockHasNoEnergy() {
    assertEquals(DockStatus.NO_ENERGY, DockStatus.decide(true, true, false));
  }
}
