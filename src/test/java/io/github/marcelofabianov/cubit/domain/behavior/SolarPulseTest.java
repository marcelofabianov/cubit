package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SolarPulseTest {
  private static final double WEAKEST = 0.1;
  private static final double STRONGEST = 0.6;
  private static final double PERIOD = 60;
  private static final double HALF_PERIOD = 30;
  private static final double SOME_MOMENT = 17;
  private static final double TOLERANCE = 1.0E-9;
  private static final int MOMENTS = 300;

  @Test
  void startsAtItsWeakest() {
    assertEquals(WEAKEST, SolarPulse.at(0), TOLERANCE);
  }

  @Test
  void isStrongestHalfwayThroughThePeriod() {
    assertEquals(STRONGEST, SolarPulse.at(HALF_PERIOD), TOLERANCE);
  }

  @Test
  void repeatsEveryPeriod() {
    assertEquals(SolarPulse.at(SOME_MOMENT), SolarPulse.at(SOME_MOMENT + PERIOD), TOLERANCE);
  }

  @Test
  void neverLeavesItsLimits() {
    for (int tick = 0; tick < MOMENTS; tick++) {
      double pulse = SolarPulse.at(tick);
      assertTrue(pulse >= WEAKEST - TOLERANCE && pulse <= STRONGEST + TOLERANCE);
    }
  }
}
