package io.github.marcelofabianov.cubit.domain.dock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class AutoReturnTest {
  private static final Energy CAPACITY = new Energy(1_000);
  private static final Battery AT_RETURN = new Battery(CAPACITY, new Energy(100));
  private static final Battery ABOVE_RETURN = new Battery(CAPACITY, new Energy(101));
  private static final Percentage RETURN_CHARGE = new Percentage(10);
  private static final AutoReturn ON = new AutoReturn(true, RETURN_CHARGE);

  @Test
  void anActiveCubitThatFollowsGoesBackAtTheReturnCharge() {
    assertTrue(ON.returns(PowerState.ACTIVE, AT_RETURN, true, false));
  }

  @Test
  void aboveTheReturnChargeItKeepsGoing() {
    assertFalse(ON.returns(PowerState.ACTIVE, ABOVE_RETURN, true, false));
  }

  @Test
  void theSwitchOffKeepsItFromGoingBack() {
    assertFalse(
        new AutoReturn(false, RETURN_CHARGE).returns(PowerState.ACTIVE, AT_RETURN, true, false));
  }

  @Test
  void onlyTheCubitThatFollowsGoesBack() {
    assertFalse(ON.returns(PowerState.ACTIVE, AT_RETURN, false, false));
  }

  @Test
  void aCubitOnItsWayAlreadyDoesNotStartAgain() {
    assertFalse(ON.returns(PowerState.ACTIVE, AT_RETURN, true, true));
  }

  @Test
  void anInactiveCubitDoesNotGoBack() {
    assertFalse(ON.returns(PowerState.HIBERNATING, AT_RETURN, true, false));
    assertFalse(ON.returns(PowerState.INACTIVE_EXHAUSTED, AT_RETURN, true, false));
  }
}
