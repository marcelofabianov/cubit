package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.Optional;

/** The numbers of the torches as the screen shows them: the cost and the tier that unlocks them. */
public interface TorchControl {
  /** On the client, where the real values come from the server. */
  TorchControl NONE =
      new TorchControl() {
        @Override
        public Energy cost() {
          return Energy.ZERO;
        }

        @Override
        public Optional<RobotTier> unlockTier() {
          return Optional.empty();
        }
      };

  Energy cost();

  /** The first tier with torches, read from the config, for the locked slot (answer 984). */
  Optional<RobotTier> unlockTier();
}
