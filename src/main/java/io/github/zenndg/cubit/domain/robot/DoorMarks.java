package io.github.zenndg.cubit.domain.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The doors an owner marked, for every Cubit of the owner, in every dimension, in the order they
 * were marked (answers 1364 and 1369). A click on a marked door unmarks it, and a click on a new
 * one marks it while the limit allows.
 */
public record DoorMarks(List<MarkedDoor> doors) {
  public static final DoorMarks NONE = new DoorMarks(List.of());

  /** What a click with the Doors app did. */
  public enum Mark {
    ADDED,
    REMOVED,
    FULL
  }

  /** The marks after a click, and what the click did. */
  public record Clicked(DoorMarks marks, Mark mark) {}

  public DoorMarks {
    doors = List.copyOf(doors);
  }

  public Clicked clicked(MarkedDoor door, int limit) {
    if (doors.contains(door)) {
      return new Clicked(without(door), Mark.REMOVED);
    }
    if (doors.size() >= limit) {
      return new Clicked(this, Mark.FULL);
    }
    List<MarkedDoor> next = new ArrayList<>(doors);
    next.add(door);
    return new Clicked(new DoorMarks(next), Mark.ADDED);
  }

  public DoorMarks without(MarkedDoor door) {
    List<MarkedDoor> next = new ArrayList<>(doors);
    next.remove(door);
    return new DoorMarks(next);
  }

  /** Shift and a click away from a marked door take the last one out, as in the other apps. */
  public DoorMarks withoutLast() {
    return doors.isEmpty() ? this : new DoorMarks(doors.subList(0, doors.size() - 1));
  }

  /** The blocks marked in a dimension, which the route of a Cubit may go through. */
  public Set<Long> blocksIn(String dimension) {
    return doors.stream()
        .filter(door -> door.isIn(dimension))
        .map(MarkedDoor::block)
        .collect(Collectors.toUnmodifiableSet());
  }

  public boolean contains(MarkedDoor door) {
    return doors.contains(door);
  }

  public boolean isEmpty() {
    return doors.isEmpty();
  }
}
