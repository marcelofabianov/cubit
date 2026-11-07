package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.HashSet;
import java.util.Set;

/** What a scanner already told about, so only what is new warns the owner again. */
public record FreshFinds<T>(Set<T> found) {
  private static final String UNCHECKED = "unchecked";
  private static final FreshFinds<Object> NONE = new FreshFinds<>(Set.of());

  public FreshFinds {
    found = Set.copyOf(found);
  }

  @SuppressWarnings(UNCHECKED)
  public static <T> FreshFinds<T> none() {
    return (FreshFinds<T>) NONE;
  }

  /** What left the range is forgotten, so it counts as new if it comes back. */
  public Update<T> seen(Set<T> now) {
    Set<T> fresh = new HashSet<>(now);
    fresh.removeAll(found);
    return new Update<>(Set.copyOf(fresh), new FreshFinds<>(now));
  }

  /** What is new in a scan, and what to remember for the next one. */
  public record Update<T>(Set<T> fresh, FreshFinds<T> known) {}
}
