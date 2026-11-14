package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * What the guide does next: there, given up, waiting for the owner, or walking on (answers 1112,
 * 1121, 1123 and 1126). It never walks further ahead than the lead, so it stays in the chunks
 * around the owner.
 */
public enum DeathGuideStep {
  MOVE,
  WAIT,
  ARRIVED,
  STUCK;

  public static DeathGuideStep of(
      double toSpot, double toOwner, DeathGuideProgress progress, DeathGuideSpec spec) {
    if (toSpot <= spec.arrivalBlocks()) {
      return ARRIVED;
    }
    if (progress.stillTicks() >= spec.stuckTicks()) {
      return STUCK;
    }
    return toOwner > spec.leadBlocks() ? WAIT : MOVE;
  }
}
