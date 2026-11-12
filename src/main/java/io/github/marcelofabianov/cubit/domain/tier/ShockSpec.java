package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import java.util.EnumSet;
import java.util.Set;

/**
 * The shock of one tier and the defense modes it allows. Peaceful is always allowed, because a
 * robot must always be able to stop attacking.
 */
public record ShockSpec(
    int damage,
    int rangeBlocks,
    int intervalTicks,
    Energy cost,
    boolean hitsAllInRange,
    Set<DefenseMode> modes) {
  /** A tier without a shock, like Base: only Peaceful, and nothing to shock with. */
  public static final ShockSpec NONE = new ShockSpec(0, 0, 1, Energy.ZERO, false, Set.of());

  private static final String NEGATIVE_MESSAGE = "A shock number cannot be negative: ";
  private static final String INTERVAL_MESSAGE = "The shock interval must be positive: ";

  public ShockSpec {
    requireNotNegative(damage);
    requireNotNegative(rangeBlocks);
    if (intervalTicks < 1) {
      throw new IllegalArgumentException(INTERVAL_MESSAGE + intervalTicks);
    }
    EnumSet<DefenseMode> allowed = EnumSet.of(DefenseMode.PEACEFUL);
    allowed.addAll(modes);
    modes = Set.copyOf(allowed);
  }

  public boolean allows(DefenseMode mode) {
    return modes.contains(mode);
  }

  /** Without damage or range, there is no shock, whatever the mode. */
  public boolean canShock() {
    return damage > 0 && rangeBlocks > 0;
  }

  private static void requireNotNegative(int value) {
    if (value < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + value);
    }
  }
}
