package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class RobotScreenStatusTest {
  private static final Energy CAPACITY = new Energy(300_000);
  private static final Percentage CRITICAL = new Percentage(5);
  private static final Battery HEALTHY = new Battery(CAPACITY, new Energy(214_000));
  private static final Battery AT_CRITICAL = new Battery(CAPACITY, new Energy(15_000));
  private static final Battery ABOVE_CRITICAL = new Battery(CAPACITY, new Energy(15_001));
  private static final boolean FOLLOWING = true;
  private static final boolean NOT_FOLLOWING = false;

  private static RobotScreenStatus status(PowerState state, boolean following, Battery battery) {
    return RobotScreenStatus.of(state, following, battery, CRITICAL);
  }

  @Test
  void anActiveRobotThatFollowsShowsFollowing() {
    assertEquals(RobotScreenStatus.FOLLOWING, status(PowerState.ACTIVE, FOLLOWING, HEALTHY));
  }

  @Test
  void anActiveRobotThatDoesNotFollowShowsActive() {
    assertEquals(RobotScreenStatus.ACTIVE, status(PowerState.ACTIVE, NOT_FOLLOWING, HEALTHY));
  }

  @Test
  void theCriticalBatteryWinsOverFollowingAndActive() {
    assertEquals(
        RobotScreenStatus.CRITICAL_BATTERY, status(PowerState.ACTIVE, FOLLOWING, AT_CRITICAL));
    assertEquals(
        RobotScreenStatus.CRITICAL_BATTERY, status(PowerState.ACTIVE, NOT_FOLLOWING, AT_CRITICAL));
    assertEquals(RobotScreenStatus.FOLLOWING, status(PowerState.ACTIVE, FOLLOWING, ABOVE_CRITICAL));
  }

  @Test
  void aHibernatingRobotShowsHibernatingEvenWithACriticalBattery() {
    assertEquals(
        RobotScreenStatus.HIBERNATING, status(PowerState.HIBERNATING, NOT_FOLLOWING, AT_CRITICAL));
  }

  @Test
  void anInactiveRobotShowsOffSoTheScreenNeverBreaksBeforeClosing() {
    assertEquals(
        RobotScreenStatus.OFF, status(PowerState.INACTIVE_EXHAUSTED, NOT_FOLLOWING, AT_CRITICAL));
    assertEquals(
        RobotScreenStatus.OFF, status(PowerState.INACTIVE_SWITCHED_OFF, FOLLOWING, HEALTHY));
  }
}
