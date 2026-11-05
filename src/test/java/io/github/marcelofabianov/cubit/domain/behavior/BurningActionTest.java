package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class BurningActionTest {
  @Test
  void robotThatIsNotBurningDoesNotReact() {
    assertEquals(BurningAction.NONE, BurningAction.decide(PowerState.ACTIVE, false, true));
  }

  @Test
  void burningActiveRobotGoesToWaterWhenThereIsAny() {
    assertEquals(BurningAction.SEEK_WATER, BurningAction.decide(PowerState.ACTIVE, true, true));
  }

  @Test
  void burningActiveRobotWandersWithoutWater() {
    assertEquals(BurningAction.WANDER, BurningAction.decide(PowerState.ACTIVE, true, false));
  }

  @Test
  void onlyAnActiveRobotReactsToFire() {
    assertEquals(
        BurningAction.NONE, BurningAction.decide(PowerState.INACTIVE_EXHAUSTED, true, true));
    assertEquals(
        BurningAction.NONE, BurningAction.decide(PowerState.INACTIVE_SWITCHED_OFF, true, true));
    assertEquals(BurningAction.NONE, BurningAction.decide(PowerState.HIBERNATING, true, true));
  }
}
