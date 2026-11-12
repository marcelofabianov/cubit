package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.marcelofabianov.cubit.domain.energy.SolarStatus.SolarView;
import org.junit.jupiter.api.Test;

class SolarStatusTest {
  private static final boolean SWITCHED_ON = true;
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Battery HALF = new Battery(CAPACITY, new Energy(150_000));
  private static final Battery FULL = new Battery(CAPACITY, CAPACITY);
  private static final SolarOutput DAY_ONLY = new SolarOutput(new Energy(5), Energy.ZERO);
  private static final SolarOutput DAY_AND_NIGHT = new SolarOutput(new Energy(12), new Energy(3));
  private static final boolean YES = true;
  private static final boolean NO = false;

  private static SolarStatus status(SolarView view, Battery battery, SolarOutput output) {
    return SolarStatus.of(view, battery, output);
  }

  private static SolarView active(boolean sunExists, boolean skyOpen, boolean day) {
    return new SolarView(PowerState.ACTIVE, sunExists, skyOpen, day, SWITCHED_ON);
  }

  @Test
  void anOpenSkyByDayCharges() {
    assertEquals(SolarStatus.CHARGING, status(active(YES, YES, YES), HALF, DAY_ONLY));
  }

  @Test
  void aCoveredPanelDoesNotCharge() {
    assertEquals(SolarStatus.NO_SKY, status(active(YES, NO, YES), HALF, DAY_ONLY));
  }

  @Test
  void theNightOnlyStopsTheTiersWithoutANightOutput() {
    assertEquals(SolarStatus.NIGHT, status(active(YES, YES, NO), HALF, DAY_ONLY));
    assertEquals(SolarStatus.CHARGING, status(active(YES, YES, NO), HALF, DAY_AND_NIGHT));
  }

  @Test
  void aDimensionWithoutSunWinsOverTheSkyAndTheBattery() {
    assertEquals(SolarStatus.NO_SUN, status(active(NO, NO, YES), FULL, DAY_ONLY));
  }

  @Test
  void aFullBatteryWinsOverTheSkyAndTheNight() {
    assertEquals(SolarStatus.FULL, status(active(YES, NO, NO), FULL, DAY_ONLY));
  }

  @Test
  void theBaseAndTheInactiveStatesTurnThePanelOff() {
    SolarView hibernating = new SolarView(PowerState.HIBERNATING, YES, YES, YES, SWITCHED_ON);
    SolarView exhausted = new SolarView(PowerState.INACTIVE_EXHAUSTED, YES, YES, YES, SWITCHED_ON);
    assertEquals(SolarStatus.OFF, status(hibernating, HALF, DAY_ONLY));
    assertEquals(SolarStatus.OFF, status(exhausted, HALF, DAY_ONLY));
  }

  @Test
  void aSwitchedOffPanelSaysSo() {
    SolarView off = new SolarView(PowerState.ACTIVE, YES, YES, YES, NO);

    assertEquals(SolarStatus.SWITCHED_OFF, status(off, HALF, DAY_AND_NIGHT));
  }
}
