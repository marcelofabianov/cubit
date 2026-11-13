package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.Energy;

/**
 * The torches of one tier: the cost of each torch and the block light the robot keeps where mobs
 * could spawn. A light level of zero locks the torches, as on Base (answers 972, 975 and 981).
 */
public record TorchSpec(Energy cost, LightLevel lightLevel) {
  /** No torches, for a tier built without torch numbers. */
  public static final TorchSpec NONE = new TorchSpec(Energy.ZERO, new LightLevel(0));

  public boolean unlocked() {
    return lightLevel.value() > 0;
  }
}
