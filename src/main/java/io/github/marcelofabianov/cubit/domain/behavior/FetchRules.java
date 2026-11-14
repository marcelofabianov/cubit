package io.github.marcelofabianov.cubit.domain.behavior;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.ToDoubleFunction;

/**
 * When the following robot walks to the items on the ground, and which one (answers 927 and 936 to
 * 942). The radius is around the owner, so an item thrown in front of the owner is in reach.
 */
public final class FetchRules {
  private FetchRules() {}

  /**
   * Fetching without picking up would only walk around, so it needs pickup on, as the magnet does.
   * Busy covers the dock trip, fire, blocks that hurt and the shock, which come first (answer 939).
   */
  public static boolean fetches(
      PowerState state, PickupSettings settings, PickupSpec spec, boolean following, boolean busy) {
    return PickupRules.collects(state, settings.pickupOn())
        && settings.fetchOn()
        && spec.fetchUnlocked()
        && following
        && !busy;
  }

  /**
   * In an area, fetching is the work of a Cubit without a card, so only pickup counts, not the
   * fetch switch of the one that follows (answers 340 and 1080).
   */
  public static boolean fetchesInArea(PowerState state, PickupSettings settings, boolean busy) {
    return PickupRules.collects(state, settings.pickupOn()) && !busy;
  }

  public static boolean inReach(double distanceToOwnerBlocks, PickupSpec spec) {
    return spec.fetchUnlocked() && distanceToOwnerBlocks <= spec.fetchRadiusBlocks();
  }

  /** One item at a time, the one nearest to the owner. */
  public static <T> Optional<T> target(
      Collection<T> candidates, ToDoubleFunction<T> distanceToOwner, PickupSpec spec) {
    return candidates.stream()
        .filter(item -> inReach(distanceToOwner.applyAsDouble(item), spec))
        .min(Comparator.comparingDouble(distanceToOwner));
  }

  /** Without reaching the item in time, the robot gives up on it. */
  public static boolean gaveUp(long startTick, long nowTick, int giveUpTicks) {
    return nowTick - startTick >= giveUpTicks;
  }
}
