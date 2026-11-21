package io.github.zenndg.cubit.domain.behavior;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.PowerState;

/** When a Cubit opens a marked door and when it closes it again (answers 1366 to 1370). */
public final class DoorRules {
  private DoorRules() {}

  /** Active and with charge for the whole cost, or the door stays shut, as before. */
  public static boolean opens(PowerState state, Battery battery, Energy cost) {
    return state == PowerState.ACTIVE && battery.charge().amount() >= cost.amount();
  }

  public static Battery pay(Battery battery, Energy cost) {
    return battery.consume(cost).battery();
  }

  /**
   * Only a door it opened, once it is no longer near it, after passing or after taking another way,
   * and nothing else stands in it.
   */
  public static boolean closes(boolean openedByRobot, boolean robotNear, boolean occupied) {
    return openedByRobot && !robotNear && !occupied;
  }
}
