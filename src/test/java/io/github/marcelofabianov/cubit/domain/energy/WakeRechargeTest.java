package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WakeRechargeTest {
  private static final Energy ROBOT_CAPACITY = new Energy(300_000);
  private static final Energy BANK_CAPACITY = new Energy(210_000);
  private static final Percentage WAKE = new Percentage(5);
  private static final Energy WAKE_CHARGE = new Energy(15_000);
  private static final Energy SOME = new Energy(4_000);
  private static final Energy PLENTY = new Energy(100_000);
  private static final Energy SHORT = new Energy(10_999);

  @Test
  void anEmptyRobotGetsTheWakeChargeFromTheBank() {
    WakeRecharge.Result result =
        WakeRecharge.of(
                new Battery(ROBOT_CAPACITY, Energy.ZERO), WAKE, new Battery(BANK_CAPACITY, PLENTY))
            .orElseThrow();

    assertEquals(WAKE_CHARGE, result.robot().charge());
    assertEquals(PLENTY.minus(WAKE_CHARGE), result.bank().charge());
    assertTrue(result.robot().hasAtLeast(WAKE));
  }

  @Test
  void aRobotWithSomeChargeGetsOnlyWhatIsMissing() {
    WakeRecharge.Result result =
        WakeRecharge.of(new Battery(ROBOT_CAPACITY, SOME), WAKE, new Battery(BANK_CAPACITY, PLENTY))
            .orElseThrow();

    assertEquals(WAKE_CHARGE, result.robot().charge());
    assertEquals(PLENTY.minus(WAKE_CHARGE.minus(SOME)), result.bank().charge());
  }

  @Test
  void nothingHappensAtTheWakeChargeOrWithAShortBank() {
    assertTrue(
        WakeRecharge.of(
                new Battery(ROBOT_CAPACITY, WAKE_CHARGE), WAKE, new Battery(BANK_CAPACITY, PLENTY))
            .isEmpty());
    assertTrue(
        WakeRecharge.of(new Battery(ROBOT_CAPACITY, SOME), WAKE, new Battery(BANK_CAPACITY, SHORT))
            .isEmpty());
  }
}
