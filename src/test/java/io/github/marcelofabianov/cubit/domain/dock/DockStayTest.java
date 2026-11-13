package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class DockStayTest {
  private static final boolean HEALTH_FULL = true;
  private static final boolean HURT = false;
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Battery FULL = new Battery(CAPACITY, CAPACITY);
  private static final Battery NOT_FULL = new Battery(CAPACITY, new Energy(299_999));
  private static final int HOLD = 60;
  private static final int BASE_HEALTH = 25;
  private static final int ULTIMATE_HEALTH = 200;
  private static final int FULL_HEAL = 1_200;
  private static final float PRECISION = 1.0E-3f;

  @Test
  void aRobotThatIsNotFullStays() {
    assertFalse(DockStay.shouldLeave(NOT_FULL, HEALTH_FULL, HOLD, HOLD));
  }

  @Test
  void aFullRobotWaitsBeforeLeaving() {
    assertFalse(DockStay.shouldLeave(FULL, HEALTH_FULL, HOLD - 1, HOLD));
  }

  @Test
  void aFullRobotLeavesAfterWaiting() {
    assertTrue(DockStay.shouldLeave(FULL, HEALTH_FULL, HOLD, HOLD));
  }

  /** The robot waits for its health too, not only for its battery (answer 963). */
  @Test
  void aRobotWithAFullBatteryStaysUntilItsHealthIsFull() {
    assertFalse(DockStay.shouldLeave(FULL, HURT, HOLD, HOLD));
  }

  @Test
  void theRobotIsCompleteOnlyWithBatteryAndHealthFull() {
    assertTrue(DockStay.complete(FULL, HEALTH_FULL));
    assertFalse(DockStay.complete(FULL, HURT));
    assertFalse(DockStay.complete(NOT_FULL, HEALTH_FULL));
  }

  @Test
  void everyTierHealsInTheSameTime() {
    assertEquals(
        BASE_HEALTH, DockStay.healthPerTick(BASE_HEALTH, FULL_HEAL) * FULL_HEAL, PRECISION);
    assertEquals(
        ULTIMATE_HEALTH, DockStay.healthPerTick(ULTIMATE_HEALTH, FULL_HEAL) * FULL_HEAL, PRECISION);
  }
}
