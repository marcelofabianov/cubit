package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Which ores signal and which one leads. The rare kinds are listed from rarest to least rare. */
public record OreSignals(List<OreKind> rareKinds) {
  public OreSignals {
    rareKinds = List.copyOf(rareKinds);
  }

  public boolean isRare(OreKind kind) {
    return rareKinds.contains(kind);
  }

  /** The kind a text names: the rarest of those that signal, and ores of the filter come after. */
  public Optional<OreKind> lead(Collection<OreKind> kinds, OreFilter filter) {
    return kinds.stream()
        .filter(kind -> isRare(kind) || filter.contains(kind))
        .min(Comparator.comparingInt(this::rank));
  }

  private int rank(OreKind kind) {
    int rank = rareKinds.indexOf(kind);
    return rank < 0 ? rareKinds.size() : rank;
  }
}
