package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.Optional;
import java.util.UUID;

/**
 * The mob that Focus shocks: the last one the owner hurt. Only what a shock may hit becomes the
 * target, and the target ends when it dies, leaves the dimension or gets far from the owner
 * (answers 845, 846 and 852).
 */
public record FocusTarget(Optional<UUID> target) {
  public static final FocusTarget NONE = new FocusTarget(Optional.empty());

  public boolean hasTarget() {
    return target.isPresent();
  }

  public boolean is(UUID mob) {
    return target.filter(mob::equals).isPresent();
  }

  /** The owner hurt a mob. The owner, players, robots and peaceful mobs are never the target. */
  public FocusTarget hurtByOwner(UUID mob, ShockCandidate candidate) {
    boolean markable =
        candidate == ShockCandidate.HOSTILE_MOB || candidate == ShockCandidate.NEUTRAL_MOB;
    return markable ? new FocusTarget(Optional.of(mob)) : this;
  }

  /**
   * The distance from the target to the owner, or nothing when the target is dead or in another
   * dimension. Beyond the limit, the target ends.
   */
  public FocusTarget followed(Optional<Double> distanceToOwner, int forgetDistanceBlocks) {
    boolean stays =
        distanceToOwner.filter(distance -> distance <= forgetDistanceBlocks).isPresent();
    return stays ? this : NONE;
  }

  /** A new target, to tell the owner. Losing the target is not a change worth a text. */
  public boolean changedFrom(FocusTarget previous) {
    return hasTarget() && !target.equals(previous.target);
  }
}
