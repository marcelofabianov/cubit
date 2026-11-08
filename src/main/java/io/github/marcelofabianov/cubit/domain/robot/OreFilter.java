package io.github.marcelofabianov.cubit.domain.robot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/** The ores the owner asked the scanner to look for, oldest first. */
public record OreFilter(List<OreKind> kinds) {
  public static final OreFilter EMPTY = new OreFilter(List.of());

  public OreFilter {
    kinds = List.copyOf(new LinkedHashSet<>(kinds));
  }

  public boolean contains(OreKind kind) {
    return kinds.contains(kind);
  }

  /** The same kind again clears it. A full filter drops the oldest, so a click always works. */
  public OreFilter toggled(OreKind kind, int slots) {
    List<OreKind> next = new ArrayList<>(kinds);
    if (!next.remove(kind)) {
      next.add(kind);
    }
    return new OreFilter(next).fitting(slots);
  }

  /** The config can shrink while a robot is stored, so the newest kinds are the ones kept. */
  public OreFilter fitting(int slots) {
    return kinds.size() <= slots
        ? this
        : new OreFilter(kinds.subList(kinds.size() - slots, kinds.size()));
  }
}
