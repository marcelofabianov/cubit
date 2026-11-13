package io.github.marcelofabianov.cubit.domain.robot;

import java.util.UUID;

/**
 * A Cubit as the selection screen of the Tablet orders it (answer 1003). The distance only counts
 * in the same dimension as the player.
 */
public record RobotListing(
    UUID id,
    String name,
    boolean following,
    boolean sameDimension,
    boolean loaded,
    double distance) {}
