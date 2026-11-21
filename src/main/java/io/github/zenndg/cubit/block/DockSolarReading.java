package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.domain.dock.DockSunState;
import io.github.zenndg.cubit.domain.energy.Energy;

/**
 * What the panel of a Solar Dock gives now, what it gives at most at the tier, what it is doing,
 * and, on the client, whether it pulses (answers 1391, 1392 and 1396).
 */
public record DockSolarReading(Energy given, Energy max, DockSunState state, boolean sunlit) {}
