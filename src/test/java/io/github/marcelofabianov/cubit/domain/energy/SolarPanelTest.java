package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SolarPanelTest {
  private static final Energy DAY = new Energy(12);
  private static final Energy NIGHT = new Energy(3);
  private static final SolarOutput ELITE = new SolarOutput(DAY, NIGHT);
  private static final SolarOutput BASE = new SolarOutput(new Energy(5), Energy.ZERO);
  private static final long LAST_TICK_OF_DAY = 11_999;
  private static final long FIRST_TICK_OF_NIGHT = 12_000;
  private static final long LAST_TICK_OF_NIGHT = 23_999;
  private static final long NEXT_MORNING = 24_000;
  private static final long NEXT_NIGHT = 36_000;
  private static final long BEFORE_THE_START = -1;

  @Test
  void theDayIsTheFirstHalfOfTheClock() {
    assertTrue(SolarPanel.isDay(0));
    assertTrue(SolarPanel.isDay(LAST_TICK_OF_DAY));
    assertFalse(SolarPanel.isDay(FIRST_TICK_OF_NIGHT));
    assertFalse(SolarPanel.isDay(LAST_TICK_OF_NIGHT));
  }

  @Test
  void theClockRepeatsEveryDay() {
    assertTrue(SolarPanel.isDay(NEXT_MORNING));
    assertFalse(SolarPanel.isDay(NEXT_NIGHT));
    assertFalse(SolarPanel.isDay(BEFORE_THE_START));
  }

  @Test
  void anActiveRobotInTheSunGetsTheOutputOfTheDay() {
    assertEquals(DAY, SolarPanel.output(ELITE, PowerState.ACTIVE, true, true));
  }

  @Test
  void atNightOnlyTheTiersWithANightOutputGetEnergy() {
    assertEquals(NIGHT, SolarPanel.output(ELITE, PowerState.ACTIVE, true, false));
    assertEquals(Energy.ZERO, SolarPanel.output(BASE, PowerState.ACTIVE, true, false));
  }

  @Test
  void withoutTheSunItGivesNothing() {
    assertEquals(Energy.ZERO, SolarPanel.output(ELITE, PowerState.ACTIVE, false, true));
  }

  @Test
  void onlyAnActiveRobotUsesThePanel() {
    for (PowerState state : PowerState.values()) {
      if (state != PowerState.ACTIVE) {
        assertEquals(Energy.ZERO, SolarPanel.output(ELITE, state, true, true));
      }
    }
  }

  @Test
  void aFullBatteryAcceptsNothingAndCreatesNoEnergy() {
    Battery full = new Battery(DAY, DAY);

    Battery.Charged charged = full.recharge(DAY);

    assertEquals(Energy.ZERO, charged.accepted());
    assertEquals(DAY, charged.battery().charge());
  }
}
