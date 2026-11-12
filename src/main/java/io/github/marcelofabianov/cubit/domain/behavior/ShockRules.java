package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.tier.ShockSpec;
import java.util.List;

/** When the robot shocks, and whom, from the numbers of its tier (answers 825 and 826). */
public final class ShockRules {
  private ShockRules() {}

  /**
   * Only an active robot, in a mode its tier allows and that attacks, after the interval, with
   * charge for the whole cost.
   */
  public static boolean ready(
      PowerState state,
      DefenseMode mode,
      ShockSpec spec,
      Battery battery,
      long lastShockTick,
      long nowTick) {
    return state == PowerState.ACTIVE
        && mode != DefenseMode.PEACEFUL
        && spec.allows(mode)
        && spec.canShock()
        && nowTick - lastShockTick >= spec.intervalTicks()
        && battery.charge().amount() >= spec.cost().amount();
  }

  /** The candidates come nearest first. Most tiers hit the nearest, and an area shock all. */
  public static <T> List<T> targets(ShockSpec spec, List<T> nearestFirst) {
    if (spec.hitsAllInRange() || nearestFirst.isEmpty()) {
      return List.copyOf(nearestFirst);
    }
    return List.of(nearestFirst.getFirst());
  }
}
