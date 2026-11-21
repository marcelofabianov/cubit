package io.github.zenndg.cubit.player;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.domain.robot.DoorMarks;
import io.github.zenndg.cubit.domain.robot.MarkedDoor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.slf4j.Logger;

/**
 * The doors each owner marked with the Doors app. They stay in the world, not in the player, so a
 * Cubit reads them with its owner offline (answers 1364 and 1371). The names are a contract with
 * the saves, and a door that cannot be read is skipped and logged (rule 17).
 */
public final class SavedDoorMarks extends SavedData {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String DATA_NAME = "cubit_door_marks";
  private static final String ENTRIES_KEY = "Entries";
  private static final String OWNER_KEY = "Owner";
  private static final String BLOCK_KEY = "Block";
  private static final String DIMENSION_KEY = "Dimension";
  private static final String INVALID_DOOR = "Skipping a marked door that cannot be read: {}";

  private final Map<UUID, DoorMarks> marksByOwner = new HashMap<>();

  public static SavedDoorMarks of(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(new Factory<>(SavedDoorMarks::new, SavedDoorMarks::load), DATA_NAME);
  }

  public DoorMarks marksOf(UUID owner) {
    return marksByOwner.getOrDefault(owner, DoorMarks.NONE);
  }

  public void set(UUID owner, DoorMarks marks) {
    if (marks.isEmpty()) {
      marksByOwner.remove(owner);
    } else {
      marksByOwner.put(owner, marks);
    }
    setDirty();
  }

  private static SavedDoorMarks load(CompoundTag tag, HolderLookup.Provider registries) {
    Map<UUID, List<MarkedDoor>> read = new HashMap<>();
    ListTag entries = tag.getList(ENTRIES_KEY, Tag.TAG_COMPOUND);
    for (int index = 0; index < entries.size(); index++) {
      CompoundTag entry = entries.getCompound(index);
      if (!entry.hasUUID(OWNER_KEY)) {
        continue;
      }
      try {
        MarkedDoor door = new MarkedDoor(entry.getLong(BLOCK_KEY), entry.getString(DIMENSION_KEY));
        read.computeIfAbsent(entry.getUUID(OWNER_KEY), key -> new ArrayList<>()).add(door);
      } catch (IllegalArgumentException invalid) {
        LOGGER.warn(INVALID_DOOR, invalid.getMessage());
      }
    }
    SavedDoorMarks data = new SavedDoorMarks();
    read.forEach((owner, doors) -> data.marksByOwner.put(owner, new DoorMarks(doors)));
    return data;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    ListTag entries = new ListTag();
    marksByOwner.forEach((owner, marks) ->
        marks
            .doors()
            .forEach(door -> {
              CompoundTag entry = new CompoundTag();
              entry.putUUID(OWNER_KEY, owner);
              entry.putLong(BLOCK_KEY, door.block());
              entry.putString(DIMENSION_KEY, door.dimension());
              entries.add(entry);
            }));
    tag.put(ENTRIES_KEY, entries);
    return tag;
  }
}
