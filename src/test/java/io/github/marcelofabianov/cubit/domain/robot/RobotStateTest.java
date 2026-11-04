package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import org.junit.jupiter.api.Test;

class RobotStateTest {
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Energy INITIAL_ENERGY = new Energy(75_000);
  private static final Energy SOME_ENERGY = new Energy(1_234);
  private static final Energy TOO_MUCH_ENERGY = new Energy(999_999);
  private static final int INITIAL_PERCENT = 25;
  private static final int MAX_HEALTH = 25;
  private static final int SLOTS = 27;
  private static final int RADIUS = 15;
  private static final float SOME_HEALTH = 7.5f;
  private static final float TOO_MUCH_HEALTH = 99f;
  private static final float MIN_PLACED_HEALTH = 1f;
  private static final Energy CONSUMPTION = new Energy(10);
  private static final Energy RECHARGE = new Energy(500);
  private static final TierSpec SPEC =
      new TierSpec(CAPACITY, CONSUMPTION, RECHARGE, SLOTS, MAX_HEALTH, RADIUS, 1);

  @Test
  void newRobotHasTheInitialChargeAndFullHealth() {
    RobotState state = RobotState.initial(RobotTier.BASE, SPEC, new Percentage(INITIAL_PERCENT));

    assertEquals(new RobotState(RobotTier.BASE, INITIAL_ENERGY, MAX_HEALTH), state);
  }

  @Test
  void validStateIsKeptAsIs() {
    RobotState state = new RobotState(RobotTier.BASE, SOME_ENERGY, SOME_HEALTH);

    assertEquals(new Battery(CAPACITY, SOME_ENERGY), state.batteryFor(SPEC));
    assertEquals(SOME_HEALTH, state.healthFor(SPEC));
  }

  @Test
  void energyAboveTheCapacityUsesTheCapacity() {
    RobotState state = new RobotState(RobotTier.BASE, TOO_MUCH_ENERGY, SOME_HEALTH);

    assertEquals(new Battery(CAPACITY, CAPACITY), state.batteryFor(SPEC));
  }

  @Test
  void healthStaysBetweenOneAndTheMaximum() {
    assertEquals(
        (float) MAX_HEALTH,
        new RobotState(RobotTier.BASE, SOME_ENERGY, TOO_MUCH_HEALTH).healthFor(SPEC));
    assertEquals(
        MIN_PLACED_HEALTH, new RobotState(RobotTier.BASE, SOME_ENERGY, 0f).healthFor(SPEC));
  }

  @Test
  void rejectsHealthThatIsNotAValidNumber() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new RobotState(RobotTier.BASE, SOME_ENERGY, Float.NaN));
    assertThrows(
        IllegalArgumentException.class, () -> new RobotState(RobotTier.BASE, SOME_ENERGY, -1f));
  }
}
