package io.github.marcelofabianov.cubit.domain.tier;

/**
 * What a module does to a robot. It only leads to the tier right after the robot's, never skips.
 */
public enum TierUpgrade {
  UPGRADES,
  NEEDS_PREVIOUS_TIER,
  ALREADY_THERE;

  public static TierUpgrade of(RobotTier current, RobotTier target) {
    if (current.compareTo(target) >= 0) {
      return ALREADY_THERE;
    }
    return current.next().filter(target::equals).isPresent() ? UPGRADES : NEEDS_PREVIOUS_TIER;
  }

  /** A module that does not fit stays in the hand. */
  public boolean consumesModule() {
    return this == UPGRADES;
  }
}
