package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class RobotClickTest {
  private static final boolean OWNER = true;
  private static final boolean STRANGER = false;
  private static final boolean SHIFT = true;
  private static final boolean NO_SHIFT = false;
  private static final boolean DYE = true;
  private static final boolean NO_DYE = false;

  @Test
  void aStrangerIsIgnoredWhateverTheClick() {
    assertEquals(RobotClick.IGNORE, RobotClick.of(STRANGER, SHIFT, DYE, PowerState.ACTIVE));
    assertEquals(RobotClick.IGNORE, RobotClick.of(STRANGER, NO_SHIFT, NO_DYE, PowerState.ACTIVE));
  }

  @Test
  void shiftCollectsInAnyState() {
    for (PowerState state : PowerState.values()) {
      assertEquals(RobotClick.COLLECT, RobotClick.of(OWNER, SHIFT, NO_DYE, state));
    }
  }

  @Test
  void shiftWinsOverADye() {
    assertEquals(RobotClick.COLLECT, RobotClick.of(OWNER, SHIFT, DYE, PowerState.ACTIVE));
  }

  @Test
  void aDyePaintsInAnyState() {
    for (PowerState state : PowerState.values()) {
      assertEquals(RobotClick.PAINT, RobotClick.of(OWNER, NO_SHIFT, DYE, state));
    }
  }

  @Test
  void anActiveOrHibernatingRobotOpensItsInventory() {
    assertEquals(RobotClick.OPEN, RobotClick.of(OWNER, NO_SHIFT, NO_DYE, PowerState.ACTIVE));
    assertEquals(RobotClick.OPEN, RobotClick.of(OWNER, NO_SHIFT, NO_DYE, PowerState.HIBERNATING));
  }

  @Test
  void aRobotThatIsOffTakesEnergyOrTurnsOn() {
    assertEquals(
        RobotClick.CHARGE_OR_TURN_ON,
        RobotClick.of(OWNER, NO_SHIFT, NO_DYE, PowerState.INACTIVE_SWITCHED_OFF));
    assertEquals(
        RobotClick.CHARGE_OR_TURN_ON,
        RobotClick.of(OWNER, NO_SHIFT, NO_DYE, PowerState.INACTIVE_EXHAUSTED));
  }
}
