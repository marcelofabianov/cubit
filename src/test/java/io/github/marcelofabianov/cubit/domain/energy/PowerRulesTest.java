package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PowerRulesTest {
  private static final Energy CAPACITY = new Energy(300_000);
  private static final PowerRules RULES = new PowerRules(new Percentage(5));
  private static final Battery EMPTY = new Battery(CAPACITY, Energy.ZERO);
  private static final Battery BELOW_WAKE = new Battery(CAPACITY, new Energy(14_999));
  private static final Battery AT_WAKE = new Battery(CAPACITY, new Energy(15_000));

  @Test
  void activeRobotShutsDownWhenBatteryEmpties() {
    assertEquals(PowerState.INACTIVE_EXHAUSTED, RULES.afterEnergyChange(PowerState.ACTIVE, EMPTY));
  }

  @Test
  void activeRobotStaysActiveWhileThereIsCharge() {
    assertEquals(PowerState.ACTIVE, RULES.afterEnergyChange(PowerState.ACTIVE, BELOW_WAKE));
  }

  @Test
  void exhaustedRobotWakesAtTheMinimumCharge() {
    assertEquals(
        PowerState.INACTIVE_EXHAUSTED,
        RULES.afterEnergyChange(PowerState.INACTIVE_EXHAUSTED, BELOW_WAKE));
    assertEquals(
        PowerState.ACTIVE, RULES.afterEnergyChange(PowerState.INACTIVE_EXHAUSTED, AT_WAKE));
  }

  @Test
  void switchedOffRobotDoesNotWakeByItself() {
    assertEquals(
        PowerState.INACTIVE_SWITCHED_OFF,
        RULES.afterEnergyChange(PowerState.INACTIVE_SWITCHED_OFF, AT_WAKE));
  }

  @Test
  void hibernatingRobotStaysHibernatingEvenWhenEmpty() {
    assertEquals(PowerState.HIBERNATING, RULES.afterEnergyChange(PowerState.HIBERNATING, EMPTY));
  }

  @Test
  void clickTurnsOnSwitchedOffRobotWithCharge() {
    assertEquals(PowerState.ACTIVE, RULES.turnOn(PowerState.INACTIVE_SWITCHED_OFF, BELOW_WAKE));
  }

  @Test
  void clickDoesNotTurnOnWithoutCharge() {
    assertEquals(
        PowerState.INACTIVE_SWITCHED_OFF, RULES.turnOn(PowerState.INACTIVE_SWITCHED_OFF, EMPTY));
  }

  @Test
  void clickDoesNotBypassTheMinimumChargeOfAnExhaustedRobot() {
    assertEquals(
        PowerState.INACTIVE_EXHAUSTED, RULES.turnOn(PowerState.INACTIVE_EXHAUSTED, BELOW_WAKE));
  }

  @Test
  void leavingTheBaseWithoutChargeShutsDown() {
    assertEquals(PowerState.INACTIVE_EXHAUSTED, RULES.leaveBase(EMPTY));
    assertEquals(PowerState.ACTIVE, RULES.leaveBase(BELOW_WAKE));
  }

  @Test
  void onlyHibernationKeepsTheInventoryOpenWhileCharging() {
    assertEquals(false, PowerState.INACTIVE_EXHAUSTED.isInteractive());
    assertEquals(false, PowerState.INACTIVE_SWITCHED_OFF.isInteractive());
    assertEquals(true, PowerState.ACTIVE.isInteractive());
    assertEquals(true, PowerState.HIBERNATING.isInteractive());
  }
}
