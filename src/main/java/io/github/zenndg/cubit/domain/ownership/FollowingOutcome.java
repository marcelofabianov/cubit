package io.github.zenndg.cubit.domain.ownership;

import io.github.zenndg.cubit.domain.energy.PowerState;
import java.util.Optional;
import java.util.UUID;

/**
 * The result of a following rule: the state of the robot that acted, who follows the player now,
 * and which other robot must be switched off, if any.
 */
public record FollowingOutcome(
    PowerState state, Optional<UUID> follower, Optional<UUID> robotToSwitchOff) {}
