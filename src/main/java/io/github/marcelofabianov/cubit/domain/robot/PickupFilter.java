package io.github.marcelofabianov.cubit.domain.robot;

import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * The items the robot takes from the ground (answers 422, 423 and 880 to 883). Entries the tier
 * does not allow stay saved without counting, so a smaller config never deletes them (answer 892).
 */
public record PickupFilter(PickupMode mode, List<PickupEntry> entries) {
  public static final PickupFilter EMPTY = new PickupFilter(PickupMode.INITIAL, List.of());

  public PickupFilter {
    entries = List.copyOf(new LinkedHashSet<>(entries));
  }

  /** What counts at this tier: the first slots, without tags where the tier has none. */
  public List<PickupEntry> effective(PickupSpec spec) {
    List<PickupEntry> counted = entries.subList(0, Math.min(entries.size(), spec.filterSlots()));
    return counted.stream().filter(entry -> spec.filterTags() || !entry.tag()).toList();
  }

  public boolean allows(PickupItem item, PickupSpec spec) {
    boolean listed = effective(spec).stream().anyMatch(entry -> entry.matches(item));
    return mode == PickupMode.WHITELIST ? listed : !listed;
  }

  public PickupFilter toggledMode() {
    return new PickupFilter(mode.toggled(), entries);
  }

  /** A filled slot is replaced, and an empty one appends while the tier has room. */
  public PickupFilter placed(int slot, PickupEntry entry, PickupSpec spec) {
    if (entries.contains(entry) || (entry.tag() && !spec.filterTags())) {
      return this;
    }
    List<PickupEntry> next = new ArrayList<>(entries);
    if (slot >= 0 && slot < next.size()) {
      next.set(slot, entry);
    } else if (next.size() < spec.filterSlots()) {
      next.add(entry);
    }
    return new PickupFilter(mode, next);
  }

  public PickupFilter cleared(int slot) {
    if (slot < 0 || slot >= entries.size()) {
      return this;
    }
    List<PickupEntry> next = new ArrayList<>(entries);
    next.remove(slot);
    return new PickupFilter(mode, next);
  }

  /** The item, then each of its tags, then the item again (answer 882). */
  public PickupFilter cycled(int slot, String itemId, List<String> itemTags, PickupSpec spec) {
    if (!spec.filterTags() || itemTags.isEmpty() || slot < 0 || slot >= entries.size()) {
      return this;
    }
    PickupEntry current = entries.get(slot);
    int next = current.tag() ? itemTags.indexOf(current.id()) + 1 : 0;
    PickupEntry replacement =
        next < itemTags.size() ? PickupEntry.tag(itemTags.get(next)) : PickupEntry.item(itemId);
    List<PickupEntry> changed = new ArrayList<>(entries);
    changed.set(slot, replacement);
    return new PickupFilter(mode, changed);
  }
}
