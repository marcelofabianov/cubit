package io.github.marcelofabianov.cubit.domain.behavior;

/** Where a damaged robot that fell into the void goes. It is never destroyed. */
public enum VoidRescueTarget {
  SAFE_POSITION,
  OWNER,
  WORLD_SPAWN,
  WAIT;

  public static VoidRescueTarget decide(
      boolean safePositionUsable, boolean ownerOnline, boolean worldSpawnLoaded) {
    if (safePositionUsable) {
      return SAFE_POSITION;
    }
    if (ownerOnline) {
      return OWNER;
    }
    return worldSpawnLoaded ? WORLD_SPAWN : WAIT;
  }
}
