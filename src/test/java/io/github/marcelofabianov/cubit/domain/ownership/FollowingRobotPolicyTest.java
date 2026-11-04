package io.github.marcelofabianov.cubit.domain.ownership;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.Percentage;
import io.github.marcelofabianov.cubit.domain.energy.PowerRules;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class FollowingRobotPolicyTest {
  private static final long CAPACITY_AMOUNT = 300_000;
  private static final long LOW_CHARGE_AMOUNT = 1;
  private static final int WAKE_PERCENT = 5;
  private static final Energy CAPACITY = new Energy(CAPACITY_AMOUNT);
  private static final Battery EMPTY = new Battery(CAPACITY, Energy.ZERO);
  private static final Battery LOW_CHARGE = new Battery(CAPACITY, new Energy(LOW_CHARGE_AMOUNT));
  private static final UUID ROBOT = new UUID(0, 1);
  private static final UUID OTHER_ROBOT = new UUID(0, 2);
  private static final Optional<UUID> NOBODY = Optional.empty();
  private static final FollowingRobotPolicy POLICY =
      new FollowingRobotPolicy(new PowerRules(new Percentage(WAKE_PERCENT)));

  @Test
  void robotThatArrivesFollowsWhenNobodyElseDoes() {
    FollowingOutcome outcome = POLICY.onArrival(ROBOT, NOBODY, LOW_CHARGE);

    assertEquals(PowerState.ACTIVE, outcome.state());
    assertEquals(Optional.of(ROBOT), outcome.follower());
    assertEquals(NOBODY, outcome.robotToSwitchOff());
  }

  @Test
  void robotThatArrivesWithoutChargeTakesThePlaceAndWaitsToWake() {
    FollowingOutcome outcome = POLICY.onArrival(ROBOT, NOBODY, EMPTY);

    assertEquals(PowerState.INACTIVE_EXHAUSTED, outcome.state());
    assertEquals(Optional.of(ROBOT), outcome.follower());
  }

  @Test
  void robotThatArrivesStaysSwitchedOffWhenAnotherIsFollowing() {
    FollowingOutcome outcome = POLICY.onArrival(ROBOT, Optional.of(OTHER_ROBOT), LOW_CHARGE);

    assertEquals(PowerState.INACTIVE_SWITCHED_OFF, outcome.state());
    assertEquals(Optional.of(OTHER_ROBOT), outcome.follower());
    assertEquals(NOBODY, outcome.robotToSwitchOff());
  }

  @Test
  void robotThatArrivesKeepsItsOwnPlaceWhenTheLinkIsStale() {
    FollowingOutcome outcome = POLICY.onArrival(ROBOT, Optional.of(ROBOT), LOW_CHARGE);

    assertEquals(PowerState.ACTIVE, outcome.state());
    assertEquals(Optional.of(ROBOT), outcome.follower());
  }

  @Test
  void turningOnTakesThePlaceAndSwitchesOffOnlyTheFollower() {
    FollowingOutcome outcome =
        POLICY.onTurnOn(
            ROBOT, Optional.of(OTHER_ROBOT), PowerState.INACTIVE_SWITCHED_OFF, LOW_CHARGE);

    assertEquals(PowerState.ACTIVE, outcome.state());
    assertEquals(Optional.of(ROBOT), outcome.follower());
    assertEquals(Optional.of(OTHER_ROBOT), outcome.robotToSwitchOff());
  }

  @Test
  void turningOnWithNobodyFollowingSwitchesOffNobody() {
    FollowingOutcome outcome =
        POLICY.onTurnOn(ROBOT, NOBODY, PowerState.INACTIVE_SWITCHED_OFF, LOW_CHARGE);

    assertEquals(Optional.of(ROBOT), outcome.follower());
    assertEquals(NOBODY, outcome.robotToSwitchOff());
  }

  @Test
  void turningOnTheFollowerItselfNeverSwitchesItOff() {
    FollowingOutcome outcome =
        POLICY.onTurnOn(ROBOT, Optional.of(ROBOT), PowerState.INACTIVE_SWITCHED_OFF, LOW_CHARGE);

    assertEquals(PowerState.ACTIVE, outcome.state());
    assertEquals(NOBODY, outcome.robotToSwitchOff());
  }

  @Test
  void turningOnWithoutChargeChangesNothing() {
    FollowingOutcome outcome =
        POLICY.onTurnOn(ROBOT, Optional.of(OTHER_ROBOT), PowerState.INACTIVE_SWITCHED_OFF, EMPTY);

    assertEquals(PowerState.INACTIVE_SWITCHED_OFF, outcome.state());
    assertEquals(Optional.of(OTHER_ROBOT), outcome.follower());
    assertEquals(NOBODY, outcome.robotToSwitchOff());
  }

  @Test
  void turningOnARobotThatIsAlreadyActiveChangesNothing() {
    FollowingOutcome outcome =
        POLICY.onTurnOn(ROBOT, Optional.of(OTHER_ROBOT), PowerState.ACTIVE, LOW_CHARGE);

    assertEquals(PowerState.ACTIVE, outcome.state());
    assertEquals(Optional.of(OTHER_ROBOT), outcome.follower());
    assertEquals(NOBODY, outcome.robotToSwitchOff());
  }

  @Test
  void releasingFreesThePlaceOnlyForTheFollower() {
    assertEquals(NOBODY, POLICY.release(ROBOT, Optional.of(ROBOT)));
    assertEquals(Optional.of(OTHER_ROBOT), POLICY.release(ROBOT, Optional.of(OTHER_ROBOT)));
    assertEquals(NOBODY, POLICY.release(ROBOT, NOBODY));
  }
}
