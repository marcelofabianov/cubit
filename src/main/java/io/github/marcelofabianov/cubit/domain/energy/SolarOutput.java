package io.github.marcelofabianov.cubit.domain.energy;

/** What a solar panel gives per tick, by day and by night. The numbers come from the config. */
public record SolarOutput(Energy dayPerTick, Energy nightPerTick) {}
