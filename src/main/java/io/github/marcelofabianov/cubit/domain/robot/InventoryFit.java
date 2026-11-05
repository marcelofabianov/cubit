package io.github.marcelofabianov.cubit.domain.robot;

import java.util.List;

/** Splits what fits in the slots from the surplus, which is never deleted. */
public record InventoryFit<T>(List<T> kept, List<T> surplus) {
  private static final String NEGATIVE_MESSAGE = "Capacity cannot be negative: ";

  public static <T> InventoryFit<T> of(List<T> items, int capacity) {
    if (capacity < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + capacity);
    }
    int split = Math.min(items.size(), capacity);
    return new InventoryFit<>(
        List.copyOf(items.subList(0, split)), List.copyOf(items.subList(split, items.size())));
  }
}
