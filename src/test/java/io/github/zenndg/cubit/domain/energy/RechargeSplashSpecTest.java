package io.github.zenndg.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RechargeSplashSpecTest {
  private static final Percentage HEALTH = new Percentage(20);
  private static final Percentage ENERGY = new Percentage(15);
  private static final int RADIUS = 4;
  private static final int DURATION = 60;
  private static final RechargeSplashSpec SPEC =
      new RechargeSplashSpec(HEALTH, ENERGY, RADIUS, DURATION);
  private static final Energy BASE_BATTERY = new Energy(300_000);
  private static final int BASE_HEALTH = 25;
  private static final Energy BASE_DOSE = new Energy(45_000);
  private static final double BASE_HEALTH_DOSE = 5;
  private static final Energy ODD_BATTERY = new Energy(1_001);
  private static final Energy ODD_DOSE = new Energy(150);
  private static final double PRECISION = 1e-9;
  private static final double ON_THE_EDGE = RADIUS * RADIUS;
  private static final double JUST_OUTSIDE = ON_THE_EDGE + 0.01;
  private static final int INVALID = 0;
  private static final int THREE_SECONDS = 3;

  @Test
  void theDoseIsAShareOfTheHealthAndTheBatteryOfTheTier() {
    RechargeSplashDose dose = SPEC.doseFor(BASE_BATTERY, BASE_HEALTH);

    assertEquals(BASE_DOSE, dose.energyLeft());
    assertEquals(BASE_HEALTH_DOSE, dose.healthLeft(), PRECISION);
    assertEquals(DURATION, dose.ticksLeft());
  }

  @Test
  void theEnergyOfTheDoseIsRoundedDown() {
    assertEquals(ODD_DOSE, SPEC.doseFor(ODD_BATTERY, BASE_HEALTH).energyLeft());
  }

  @Test
  void theRadiusCountsFromTheImpactUpToItsEdge() {
    assertTrue(SPEC.reaches(ON_THE_EDGE));
    assertFalse(SPEC.reaches(JUST_OUTSIDE));
  }

  @Test
  void theDurationInSecondsIsRoundedUp() {
    assertEquals(THREE_SECONDS, SPEC.durationSeconds());
    assertEquals(1, new RechargeSplashSpec(HEALTH, ENERGY, RADIUS, 1).durationSeconds());
  }

  @Test
  void theRadiusAndTheDurationMustBePositive() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new RechargeSplashSpec(HEALTH, ENERGY, INVALID, DURATION));
    assertThrows(
        IllegalArgumentException.class,
        () -> new RechargeSplashSpec(HEALTH, ENERGY, RADIUS, INVALID));
  }
}
