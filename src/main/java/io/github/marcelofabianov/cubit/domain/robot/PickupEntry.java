package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Optional;

/**
 * One slot of the pickup filter: an item id or, on Elite and Ultimate, a tag (answer 882). Only the
 * id is compared, never the components (answer 883).
 */
public record PickupEntry(String id, boolean tag) {
  /** Marks a tag in saves, as in {@code #minecraft:logs} (answer 889). */
  public static final String TAG_MARK = "#";

  private static final String BLANK_MESSAGE = "A pickup entry needs an id";

  public PickupEntry {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException(BLANK_MESSAGE);
    }
  }

  public static PickupEntry item(String id) {
    return new PickupEntry(id, false);
  }

  public static PickupEntry tag(String id) {
    return new PickupEntry(id, true);
  }

  public String key() {
    return tag ? TAG_MARK + id : id;
  }

  public static Optional<PickupEntry> fromKey(String key) {
    boolean isTag = key.startsWith(TAG_MARK);
    String id = isTag ? key.substring(TAG_MARK.length()) : key;
    if (id.isBlank()) {
      return Optional.empty();
    }
    return Optional.of(new PickupEntry(id, isTag));
  }

  public boolean matches(PickupItem item) {
    return tag ? item.tags().contains(id) : item.id().equals(id);
  }
}
