package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * What the robot knows about a candidate besides what it is: turned against the owner, remembered
 * as an attacker by Retaliate, the target of Focus, and whether Focus has a target at all.
 */
public record ShockSignals(
    boolean angryAtOwner, boolean remembered, boolean focused, boolean hasFocusTarget) {}
