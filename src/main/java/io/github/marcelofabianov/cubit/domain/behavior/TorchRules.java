package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.TorchSpec;

/** When the robot places torches and which spots are dangerous (answers 971 to 979). */
public final class TorchRules {
  private TorchRules() {}

  /** Active, with the switch on, an unlocked tier, a torch and charge for the whole cost. */
  public static boolean places(
      PowerState state, boolean switchedOn, TorchSpec spec, boolean hasTorch, Battery battery) {
    return state == PowerState.ACTIVE
        && switchedOn
        && spec.unlocked()
        && hasTorch
        && battery.charge().amount() >= spec.cost().amount();
  }

  /**
   * A mob may spawn where the block light is below the level of the tier, because the sky light
   * does not protect at night (answer 975). Under the sky, the spot only counts once it gets dark,
   * by the same reading and limit as the lamp (answers 978 and 979).
   */
  public static boolean dangerous(
      LightLevel blockLight,
      TorchSpec spec,
      boolean seesSky,
      LightLevel ambient,
      LightLevel lampOnBelow) {
    boolean dark = blockLight.value() < spec.lightLevel().value();
    return dark && (!seesSky || ambient.value() < lampOnBelow.value());
  }

  public static Battery pay(Battery battery, TorchSpec spec) {
    return battery.consume(spec.cost()).battery();
  }
}
