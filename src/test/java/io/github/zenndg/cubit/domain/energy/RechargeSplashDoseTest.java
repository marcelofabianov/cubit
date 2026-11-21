package io.github.zenndg.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RechargeSplashDoseTest {
  private static final Energy TOTAL = new Energy(45_000);
  private static final Energy PER_TICK = new Energy(750);
  private static final double HEALTH = 6;
  private static final double HEALTH_PER_TICK = 0.1;
  private static final int TICKS = 60;
  private static final Energy UNEVEN_TOTAL = new Energy(100);
  private static final int UNEVEN_TICKS = 3;
  private static final Energy UNEVEN_PART = new Energy(33);
  private static final Energy UNEVEN_LAST = new Energy(34);
  private static final int TICKS_IN_ONE_SECOND_AND_A_BIT = 21;
  private static final int TWO_SECONDS = 2;
  private static final double PRECISION = 1e-9;
  private static final double NEGATIVE = -1;

  @Test
  void eachTickGivesAnEqualPart() {
    RechargeSplashDose.Step step = new RechargeSplashDose(TOTAL, HEALTH, TICKS).next();

    assertEquals(PER_TICK, step.energy());
    assertEquals(HEALTH_PER_TICK, step.health(), PRECISION);
    assertEquals(TICKS - 1, step.rest().ticksLeft());
  }

  @Test
  void theRestOfTheDivisionComesInTheLastTickAndNothingIsLost() {
    RechargeSplashDose dose = new RechargeSplashDose(UNEVEN_TOTAL, HEALTH, UNEVEN_TICKS);
    RechargeSplashDose.Step first = dose.next();
    RechargeSplashDose.Step second = first.rest().next();
    RechargeSplashDose.Step last = second.rest().next();

    assertEquals(UNEVEN_PART, first.energy());
    assertEquals(UNEVEN_PART, second.energy());
    assertEquals(UNEVEN_LAST, last.energy());
    assertEquals(HEALTH, first.health() + second.health() + last.health(), PRECISION);
    assertEquals(RechargeSplashDose.NONE, last.rest());
  }

  @Test
  void anEndedDoseGivesNothing() {
    RechargeSplashDose.Step step = RechargeSplashDose.NONE.next();

    assertFalse(RechargeSplashDose.NONE.isActive());
    assertEquals(Energy.ZERO, step.energy());
    assertEquals(0, step.health(), PRECISION);
    assertEquals(RechargeSplashDose.NONE, step.rest());
  }

  @Test
  void theSecondsLeftAreRoundedUp() {
    RechargeSplashDose dose = new RechargeSplashDose(TOTAL, HEALTH, TICKS_IN_ONE_SECOND_AND_A_BIT);

    assertTrue(dose.isActive());
    assertEquals(TWO_SECONDS, dose.secondsLeft());
  }

  @Test
  void anInvalidDoseIsRejected() {
    assertThrows(
        IllegalArgumentException.class, () -> new RechargeSplashDose(TOTAL, NEGATIVE, TICKS));
    assertThrows(IllegalArgumentException.class, () -> new RechargeSplashDose(TOTAL, HEALTH, -1));
    assertThrows(
        IllegalArgumentException.class, () -> new RechargeSplashDose(TOTAL, Double.NaN, TICKS));
  }
}
