package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.domain.tier.RobotTier;
import java.util.Optional;

/** The death point as the screen shows it: the first tier that marks the spot (answer 1118). */
public interface DeathPointControl {
  /** On the client, where the real values come from the server. */
  DeathPointControl NONE = Optional::empty;

  /** The first tier whose death point is not locked, read from the config. */
  Optional<RobotTier> unlockTier();
}
