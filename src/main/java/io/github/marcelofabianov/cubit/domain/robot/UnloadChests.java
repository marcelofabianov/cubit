package io.github.marcelofabianov.cubit.domain.robot;

import java.util.ArrayList;
import java.util.List;

/**
 * The chests of a Cubit, in the order they were marked, which is the order it fills them (answers
 * 1086 and 1088). A click on a marked chest removes it, and a click on a new one adds it while the
 * tier allows.
 */
public record UnloadChests(List<UnloadChest> chests) {
  public static final UnloadChests NONE = new UnloadChests(List.of());

  /** What a click with the Unload app did. */
  public enum Mark {
    ADDED,
    REMOVED,
    FULL
  }

  /** The chests after a click, and what the click did. */
  public record Clicked(UnloadChests chests, Mark mark) {}

  public UnloadChests {
    chests = List.copyOf(chests);
  }

  public Clicked clicked(UnloadChest chest, int limit) {
    List<UnloadChest> next = new ArrayList<>(chests);
    if (next.remove(chest)) {
      return new Clicked(new UnloadChests(next), Mark.REMOVED);
    }
    if (next.size() >= limit) {
      return new Clicked(this, Mark.FULL);
    }
    next.add(chest);
    return new Clicked(new UnloadChests(next), Mark.ADDED);
  }

  /** Shift and a click away from a marked chest take the last one out (answer 1099). */
  public UnloadChests withoutLast() {
    return chests.isEmpty() ? this : new UnloadChests(chests.subList(0, chests.size() - 1));
  }

  /** A config that lowered the limit keeps the first chests marked. */
  public UnloadChests fitting(int limit) {
    return chests.size() <= limit ? this : new UnloadChests(chests.subList(0, Math.max(limit, 0)));
  }

  public boolean contains(UnloadChest chest) {
    return chests.contains(chest);
  }

  public boolean isEmpty() {
    return chests.isEmpty();
  }
}
