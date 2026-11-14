package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class FollowCallTest {
  private static final Energy CAPACITY = new Energy(1_000);
  private static final Battery CHARGED = new Battery(CAPACITY, new Energy(500));
  private static final Battery EMPTY = new Battery(CAPACITY, Energy.ZERO);

  @Test
  void aSwitchedOffCubitWithChargeCanFollow() {
    assertTrue(FollowCall.of(PowerState.INACTIVE_SWITCHED_OFF, CHARGED, false, true).ready());
  }

  @Test
  void aCubitInTheDockOrActiveWithoutThePlaceCanFollow() {
    assertTrue(FollowCall.of(PowerState.HIBERNATING, CHARGED, false, true).ready());
    assertTrue(FollowCall.of(PowerState.ACTIVE, CHARGED, false, true).ready());
  }

  @Test
  void theCubitThatFollowsAlreadySaysSo() {
    assertEquals(FollowCall.FOLLOWING, FollowCall.of(PowerState.ACTIVE, CHARGED, true, true));
  }

  @Test
  void withoutChargeItCannotFollow() {
    assertEquals(
        FollowCall.NO_CHARGE, FollowCall.of(PowerState.INACTIVE_SWITCHED_OFF, EMPTY, false, true));
    assertEquals(
        FollowCall.NO_CHARGE, FollowCall.of(PowerState.INACTIVE_EXHAUSTED, CHARGED, false, true));
  }

  @Test
  void inAnotherDimensionItCannotFollow() {
    FollowCall call = FollowCall.of(PowerState.INACTIVE_SWITCHED_OFF, CHARGED, false, false);

    assertEquals(FollowCall.OTHER_DIMENSION, call);
    assertFalse(call.ready());
  }
}
