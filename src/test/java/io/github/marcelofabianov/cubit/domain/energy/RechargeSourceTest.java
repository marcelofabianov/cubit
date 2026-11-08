package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.marcelofabianov.cubit.domain.energy.RechargeSource.RechargeView;
import org.junit.jupiter.api.Test;

class RechargeSourceTest {
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Battery HALF = new Battery(CAPACITY, new Energy(150_000));
  private static final Battery FULL = new Battery(CAPACITY, CAPACITY);
  private static final boolean YES = true;
  private static final boolean NO = false;

  @Test
  void inTheBaseTheDockIsTheSource() {
    RechargeView powered = new RechargeView(YES, NO, NO);
    assertEquals(RechargeSource.DOCK, RechargeSource.of(PowerState.HIBERNATING, powered, HALF));
  }

  @Test
  void anEmptyDockSaysItHasNoPower() {
    RechargeView empty = new RechargeView(NO, NO, NO);
    assertEquals(
        RechargeSource.DOCK_NO_POWER, RechargeSource.of(PowerState.HIBERNATING, empty, HALF));
  }

  @Test
  void aFullBatteryInTheBaseWinsOverTheEmptyDock() {
    RechargeView empty = new RechargeView(NO, NO, NO);
    assertEquals(RechargeSource.DOCK_FULL, RechargeSource.of(PowerState.HIBERNATING, empty, FULL));
  }

  @Test
  void outsideTheBaseTheSlotComesBeforeThePanel() {
    RechargeView both = new RechargeView(NO, YES, YES);
    RechargeView solar = new RechargeView(NO, NO, YES);
    assertEquals(RechargeSource.SLOT, RechargeSource.of(PowerState.ACTIVE, both, HALF));
    assertEquals(RechargeSource.SOLAR, RechargeSource.of(PowerState.ACTIVE, solar, HALF));
  }

  @Test
  void nothingChargingSaysSo() {
    RechargeView nothing = new RechargeView(YES, NO, NO);
    assertEquals(RechargeSource.NONE, RechargeSource.of(PowerState.ACTIVE, nothing, HALF));
  }
}
