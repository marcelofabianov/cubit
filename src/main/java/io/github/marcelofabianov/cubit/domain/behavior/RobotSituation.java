package io.github.marcelofabianov.cubit.domain.behavior;

/** What holds for an active robot right now, for {@link ExpressionRules} to choose. */
public record RobotSituation(
    boolean dying,
    boolean hurt,
    boolean worried,
    boolean alert,
    boolean surprised,
    boolean criticalBattery,
    boolean happy) {}
