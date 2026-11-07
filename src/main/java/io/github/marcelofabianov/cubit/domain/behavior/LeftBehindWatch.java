package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * When the robot complains that the owner is getting away. It speaks once, and only again after it
 * gets close. A jump past the teleport distance, such as by a command, is not the owner running.
 */
public record LeftBehindWatch(boolean ready) {
  public static final LeftBehindWatch READY = new LeftBehindWatch(true);
  private static final LeftBehindWatch WAITING = new LeftBehindWatch(false);

  public Step next(double distance, int nearBlocks, int farBlocks, int teleportBlocks) {
    if (distance <= nearBlocks) {
      return new Step(false, READY);
    }
    if (ready && distance >= farBlocks) {
      return new Step(distance < teleportBlocks, WAITING);
    }
    return new Step(false, this);
  }

  /** Whether the robot speaks now, and what to compare the next distance with. */
  public record Step(boolean speaks, LeftBehindWatch watch) {}
}
