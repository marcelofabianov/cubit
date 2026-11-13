package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Optional;
import java.util.UUID;

/**
 * Where the robot keeps its eyes after a shock: on the target while it is alive and near, and then
 * on its last spot for a while (answers 991 to 995). The caller gives the current tick.
 */
public record ShockGaze(Optional<UUID> target, long lingerUntilTick) {
  public static final ShockGaze EMPTY = new ShockGaze(Optional.empty(), 0);

  private static final String AFTER_MESSAGE = "The time after the target must be positive: ";

  /** A strike fixes the gaze on its target, and a strike on another mob changes it. */
  public ShockGaze struck(UUID mob) {
    return new ShockGaze(Optional.of(mob), 0);
  }

  /** The target died, fled or is gone, so the gaze stays on its last spot for a while. */
  public ShockGaze lost(long nowTick, int afterTicks) {
    if (afterTicks < 1) {
      throw new IllegalArgumentException(AFTER_MESSAGE + afterTicks);
    }
    return new ShockGaze(Optional.empty(), nowTick + afterTicks);
  }

  public boolean lingering(long nowTick) {
    return target.isEmpty() && nowTick < lingerUntilTick;
  }

  /** Alive, in the same level and no farther from the owner than the forget distance (992). */
  public static boolean keeps(
      boolean alive, boolean sameLevel, double distanceToOwner, int forgetDistance) {
    return alive && sameLevel && distanceToOwner <= forgetDistance;
  }
}
