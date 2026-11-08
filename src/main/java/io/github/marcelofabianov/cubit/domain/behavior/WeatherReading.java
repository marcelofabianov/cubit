package io.github.marcelofabianov.cubit.domain.behavior;

/** What the robot reads of the world for its chatter. The caller reads it. */
public record WeatherReading(
    long dayTime, boolean skyVisible, boolean raining, boolean thundering, boolean cold) {}
