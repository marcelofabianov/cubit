package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.robot.PickupFilter;
import io.github.marcelofabianov.cubit.domain.robot.PickupMode;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.item.SavedPickupFilter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

/** The pickup settings in the entity save, under names that are a contract (answer 891). */
final class SavedPickup {
  private static final String PICKUP_KEY = "PickupEnabled";
  private static final String MAGNET_KEY = "MagnetEnabled";
  private static final String FILTER_KEY = "PickupFilter";
  private static final String MODE_KEY = "Mode";
  private static final String ENTRIES_KEY = "Entries";

  private SavedPickup() {}

  static void write(CompoundTag tag, PickupSettings settings) {
    tag.putBoolean(PICKUP_KEY, settings.pickupOn());
    tag.putBoolean(MAGNET_KEY, settings.magnetOn());
    CompoundTag filter = new CompoundTag();
    filter.putString(MODE_KEY, settings.filter().mode().key());
    ListTag entries = new ListTag();
    SavedPickupFilter.keysOf(settings.filter()).forEach(key -> entries.add(StringTag.valueOf(key)));
    filter.put(ENTRIES_KEY, entries);
    tag.put(FILTER_KEY, filter);
  }

  /** Without the keys, the robot was saved before pickup existed: on, on and an empty blacklist. */
  static PickupSettings read(CompoundTag tag) {
    return new PickupSettings(
        switchOn(tag, PICKUP_KEY), switchOn(tag, MAGNET_KEY), filter(tag.getCompound(FILTER_KEY)));
  }

  private static boolean switchOn(CompoundTag tag, String key) {
    return !tag.contains(key) || tag.getBoolean(key);
  }

  private static PickupFilter filter(CompoundTag saved) {
    ListTag entries = saved.getList(ENTRIES_KEY, Tag.TAG_STRING);
    List<String> keys = new ArrayList<>();
    for (int index = 0; index < entries.size(); index++) {
      keys.add(entries.getString(index));
    }
    return SavedPickupFilter.of(PickupMode.fromKeyOrInitial(saved.getString(MODE_KEY)), keys);
  }
}
