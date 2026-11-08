package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class EnergyUseCauseTest {
  private static final boolean YES = true;
  private static final boolean NO = false;
  private static final double ONE_BLOCK = 1.0;
  private static final double NO_CLIMB = 0.0;

  @Test
  void standingStillWithNothingOnCostsForNoReason() {
    RobotActivity idle = new RobotActivity(NO, NO, NO, NO, NO_CLIMB, NO);
    assertEquals(List.of(), EnergyUseCause.of(idle));
  }

  @Test
  void everyCauseShowsInTheOrderOfTheScreen() {
    RobotActivity everything = new RobotActivity(YES, YES, YES, YES, ONE_BLOCK, YES);
    assertEquals(
        List.of(
            EnergyUseCause.WALKING,
            EnergyUseCause.FULL_INVENTORY,
            EnergyUseCause.WATER,
            EnergyUseCause.CLIMBING,
            EnergyUseCause.DANGER,
            EnergyUseCause.LAMP),
        EnergyUseCause.of(everything));
  }

  @Test
  void loadWaterAndClimbingOnlyCountWhileWalking() {
    RobotActivity standing = new RobotActivity(NO, YES, YES, NO, ONE_BLOCK, NO);
    assertEquals(List.of(), EnergyUseCause.of(standing));
  }

  @Test
  void dangerAndTheLampCostEvenStandingStill() {
    RobotActivity standing = new RobotActivity(NO, NO, NO, YES, NO_CLIMB, YES);
    assertEquals(List.of(EnergyUseCause.DANGER, EnergyUseCause.LAMP), EnergyUseCause.of(standing));
  }
}
