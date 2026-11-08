package io.github.marcelofabianov.cubit.domain.dock;

/**
 * A robot on its way to a dock: how close it got and for how long it has not advanced. Whoever
 * calls gives the distance and the limit from the config.
 */
public record DockTrip(double bestDistance, int stuckTicks) {
  public static final DockTrip START = new DockTrip(Double.MAX_VALUE, 0);

  private static final double ENTER_DISTANCE = 1.5;
  private static final double PROGRESS_BLOCKS = 0.1;
  private static final String INVALID_MESSAGE = "Invalid dock trip: ";
  private static final String SEPARATOR = ", ";

  public DockTrip {
    if (Double.isNaN(bestDistance) || stuckTicks < 0) {
      throw new IllegalArgumentException(INVALID_MESSAGE + bestDistance + SEPARATOR + stuckTicks);
    }
  }

  /**
   * Stuck on the way to a busy dock, the robot waits where it is, because teleporting would not let
   * it in. Near the dock, time does not count, because it may be waiting for its turn.
   */
  public Step advance(double distance, int stuckLimit, boolean dockOccupied) {
    if (distance <= ENTER_DISTANCE) {
      return new Step(this, Action.ARRIVE);
    }
    DockTrip next = after(distance);
    if (next.stuckTicks <= stuckLimit) {
      return new Step(next, Action.WALK);
    }
    return new Step(next, dockOccupied ? Action.ARRIVE : Action.TELEPORT);
  }

  /**
   * A small movement does not count as advancing, or a robot pushing a wall would never give up.
   */
  private DockTrip after(double distance) {
    return distance < bestDistance - PROGRESS_BLOCKS
        ? new DockTrip(distance, 1)
        : new DockTrip(bestDistance, stuckTicks + 1);
  }

  /** Whoever arrives first enters first, and a free dock waits for the next in line. */
  public static boolean entersOnArrival(boolean dockOccupied, boolean nextInLine) {
    return !dockOccupied && nextInLine;
  }

  /** What the robot does in this tick of the trip. */
  public enum Action {
    WALK,
    ARRIVE,
    TELEPORT
  }

  /** The trip after this tick and what the robot does. */
  public record Step(DockTrip trip, Action action) {}
}
