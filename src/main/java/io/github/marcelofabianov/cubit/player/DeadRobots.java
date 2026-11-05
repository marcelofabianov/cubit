package io.github.marcelofabianov.cubit.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * Robots that died while the owner was offline. The data of an offline player cannot be changed, so
 * the world keeps this until the owner joins.
 */
public final class DeadRobots extends SavedData {
  private static final String DATA_NAME = "cubit_dead_robots";
  private static final String ENTRIES_KEY = "Entries";
  private static final String OWNER_KEY = "Owner";
  private static final String ROBOT_KEY = "Robot";

  private final Map<UUID, Set<UUID>> robotsByOwner = new HashMap<>();

  static DeadRobots of(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(new Factory<>(DeadRobots::new, DeadRobots::load), DATA_NAME);
  }

  void add(UUID owner, UUID robot) {
    robotsByOwner.computeIfAbsent(owner, key -> new HashSet<>()).add(robot);
    setDirty();
  }

  Set<UUID> takeRobotsOf(UUID owner) {
    Set<UUID> robots = robotsByOwner.remove(owner);
    if (robots == null) {
      return Set.of();
    }
    setDirty();
    return robots;
  }

  private static DeadRobots load(CompoundTag tag, HolderLookup.Provider registries) {
    DeadRobots data = new DeadRobots();
    ListTag entries = tag.getList(ENTRIES_KEY, Tag.TAG_COMPOUND);
    for (int index = 0; index < entries.size(); index++) {
      CompoundTag entry = entries.getCompound(index);
      if (entry.hasUUID(OWNER_KEY) && entry.hasUUID(ROBOT_KEY)) {
        data.add(entry.getUUID(OWNER_KEY), entry.getUUID(ROBOT_KEY));
      }
    }
    return data;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    ListTag entries = new ListTag();
    robotsByOwner.forEach((owner, robots) ->
        robots.forEach(robot -> {
          CompoundTag entry = new CompoundTag();
          entry.putUUID(OWNER_KEY, owner);
          entry.putUUID(ROBOT_KEY, robot);
          entries.add(entry);
        }));
    tag.put(ENTRIES_KEY, entries);
    return tag;
  }
}
