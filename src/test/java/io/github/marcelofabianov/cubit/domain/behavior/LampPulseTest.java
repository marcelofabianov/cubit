package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LampPulseTest {
  private static final double STRONGEST = 1;
  private static final double WEAKEST = 0.7;
  private static final double PERIOD = 40;
  private static final double HALF_PERIOD = 20;
  private static final double SOME_MOMENT = 13;
  private static final double TOLERANCE = 1.0E-9;
  private static final int MOMENTS = 200;

  @Test
  void startsAtItsStrongest() {
    assertEquals(STRONGEST, LampPulse.at(0), TOLERANCE);
  }

  @Test
  void isWeakestHalfwayThroughThePeriod() {
    assertEquals(WEAKEST, LampPulse.at(HALF_PERIOD), TOLERANCE);
  }

  @Test
  void repeatsEveryPeriod() {
    assertEquals(LampPulse.at(SOME_MOMENT), LampPulse.at(SOME_MOMENT + PERIOD), TOLERANCE);
  }

  @Test
  void neverLeavesItsLimits() {
    for (int tick = 0; tick < MOMENTS; tick++) {
      double pulse = LampPulse.at(tick);
      assertTrue(pulse >= WEAKEST - TOLERANCE && pulse <= STRONGEST + TOLERANCE);
    }
  }
}
