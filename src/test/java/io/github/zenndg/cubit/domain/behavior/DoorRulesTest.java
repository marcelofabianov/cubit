package io.github.zenndg.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class DoorRulesTest {
  private static final Energy CAPACITY = new Energy(1_000);
  private static final Energy COST = new Energy(50);
  private static final Energy SHORT = new Energy(49);
  private static final Battery ENOUGH = new Battery(CAPACITY, COST);
  private static final Battery NOT_ENOUGH = new Battery(CAPACITY, SHORT);

  /** Active and with the whole cost, or the door stays shut (answer 1368). */
  @Test
  void anActiveRobotWithTheWholeCostOpens() {
    assertTrue(DoorRules.opens(PowerState.ACTIVE, ENOUGH, COST));
    assertFalse(DoorRules.opens(PowerState.ACTIVE, NOT_ENOUGH, COST));
    assertFalse(DoorRules.opens(PowerState.INACTIVE_SWITCHED_OFF, ENOUGH, COST));
  }

  @Test
  void openingTakesTheCostFromTheBattery() {
    assertEquals(Energy.ZERO, DoorRules.pay(ENOUGH, COST).charge());
  }

  /** Only the door it opened, once it left the block and nobody stands in it (answer 1370). */
  @Test
  void itClosesOnlyItsOwnDoorOnceTheBlockIsFree() {
    assertTrue(DoorRules.closes(true, false, false));
    assertFalse(DoorRules.closes(false, false, false));
    assertFalse(DoorRules.closes(true, true, false));
    assertFalse(DoorRules.closes(true, false, true));
  }
}
