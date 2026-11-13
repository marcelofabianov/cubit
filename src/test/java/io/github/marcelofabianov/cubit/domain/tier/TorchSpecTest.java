package io.github.marcelofabianov.cubit.domain.tier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import org.junit.jupiter.api.Test;

class TorchSpecTest {
  private static final Energy COST = new Energy(20);

  @Test
  void aLightLevelOfZeroLocksTheTorches() {
    assertFalse(new TorchSpec(COST, new LightLevel(0)).unlocked());
    assertTrue(new TorchSpec(COST, new LightLevel(1)).unlocked());
    assertFalse(TorchSpec.NONE.unlocked());
  }
}
