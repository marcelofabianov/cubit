package io.github.marcelofabianov.cubit.domain.ownership;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.PowerRules;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import java.util.Optional;
import java.util.UUID;

/** Keeps a single robot following the player. The caller stores the follower and applies this. */
public record FollowingRobotPolicy(PowerRules powerRules) {
  /** Placing a robot and leaving the base follow the same rule. */
  public FollowingOutcome onArrival(UUID robot, Optional<UUID> follower, Battery battery) {
    if (otherRobotFollows(robot, follower)) {
      return new FollowingOutcome(PowerState.INACTIVE_SWITCHED_OFF, follower, Optional.empty());
    }
    return new FollowingOutcome(
        powerRules.leaveBase(battery), Optional.of(robot), Optional.empty());
  }

  public FollowingOutcome onTurnOn(
      UUID robot, Optional<UUID> follower, PowerState state, Battery battery) {
    PowerState newState = powerRules.turnOn(state, battery);
    if (newState == state) {
      return new FollowingOutcome(state, follower, Optional.empty());
    }
    return new FollowingOutcome(newState, Optional.of(robot), release(robot, follower));
  }

  public boolean otherRobotFollows(UUID robot, Optional<UUID> follower) {
    return release(robot, follower).isPresent();
  }

  /**
   * An active robot without the place cannot follow, and only a click on a switched-off one can.
   */
  public boolean holdsThePlace(UUID robot, Optional<UUID> follower) {
    return follower.filter(robot::equals).isPresent();
  }

  /**
   * Collecting a robot, its death and entering the base free the place only if it was following.
   */
  public Optional<UUID> release(UUID robot, Optional<UUID> follower) {
    return follower.filter(current -> !current.equals(robot));
  }
}
