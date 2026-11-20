package io.github.zenndg.cubit.domain.robot;

import java.util.ArrayList;
import java.util.List;

/**
 * The marks of an Organizer, in the order they were made: a click on a marked block removes it, and
 * a click on a new one adds it while the tier allows, as the chests of the Unload app (1297).
 */
public record OrganizerMarks(List<OrganizerMark> marks) {
  public static final OrganizerMarks NONE = new OrganizerMarks(List.of());

  /** The marks after a click, and what the click did. */
  public record Clicked(OrganizerMarks marks, UnloadChests.Mark mark) {}

  public OrganizerMarks {
    marks = List.copyOf(marks);
  }

  public Clicked clicked(OrganizerMark mark, int limit) {
    List<OrganizerMark> next = new ArrayList<>(marks);
    if (next.removeIf(found -> found.samePlace(mark.block(), mark.dimension()))) {
      return new Clicked(new OrganizerMarks(next), UnloadChests.Mark.REMOVED);
    }
    if (next.size() >= limit) {
      return new Clicked(this, UnloadChests.Mark.FULL);
    }
    next.add(mark);
    return new Clicked(new OrganizerMarks(next), UnloadChests.Mark.ADDED);
  }

  /** Shift and a click away from a marked block take the last one out. */
  public OrganizerMarks withoutLast() {
    return marks.isEmpty() ? this : new OrganizerMarks(marks.subList(0, marks.size() - 1));
  }

  /** A config that lowered the limit keeps the first marks. */
  public OrganizerMarks fitting(int limit) {
    return marks.size() <= limit ? this : new OrganizerMarks(marks.subList(0, Math.max(limit, 0)));
  }

  public boolean contains(long block, String dimension) {
    return marks.stream().anyMatch(mark -> mark.samePlace(block, dimension));
  }

  public boolean isEmpty() {
    return marks.isEmpty();
  }
}
