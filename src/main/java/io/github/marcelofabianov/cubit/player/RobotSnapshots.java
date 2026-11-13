package io.github.marcelofabianov.cubit.player;

import com.mojang.logging.LogUtils;
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
 * The last snapshot of each Cubit whose chunk is unloaded, by owner, so the Tablet lists it with
 * its last place and state (answers 1004 and 1026). A loaded Cubit is read live instead.
 */
public final class RobotSnapshots extends SavedData {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String DATA_NAME = "cubit_robot_snapshots";
  private static final String ENTRIES_KEY = "Entries";
  private static final String SKIPPED_MESSAGE = "Skipping a broken Cubit snapshot: {}";

  private final Map<UUID, RobotSnapshot> byRobot = new HashMap<>();

  static RobotSnapshots of(MinecraftServer server) {
    return server
        .overworld()
        .getDataStorage()
        .computeIfAbsent(new Factory<>(RobotSnapshots::new, RobotSnapshots::load), DATA_NAME);
  }

  void keep(RobotSnapshot snapshot) {
    byRobot.put(snapshot.robot(), snapshot);
    setDirty();
  }

  void forget(UUID robot) {
    if (byRobot.remove(robot) != null) {
      setDirty();
    }
  }

  List<RobotSnapshot> of(UUID owner) {
    return byRobot.values().stream().filter(snapshot -> snapshot.owner().equals(owner)).toList();
  }

  private static RobotSnapshots load(CompoundTag tag, HolderLookup.Provider registries) {
    RobotSnapshots data = new RobotSnapshots();
    ListTag entries = tag.getList(ENTRIES_KEY, Tag.TAG_COMPOUND);
    for (int index = 0; index < entries.size(); index++) {
      CompoundTag entry = entries.getCompound(index);
      RobotSnapshot.load(entry)
          .ifPresentOrElse(
              snapshot -> data.byRobot.put(snapshot.robot(), snapshot),
              () -> LOGGER.warn(SKIPPED_MESSAGE, entry));
    }
    return data;
  }

  @Override
  public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
    ListTag entries = new ListTag();
    byRobot.values().forEach(snapshot -> entries.add(snapshot.save()));
    tag.put(ENTRIES_KEY, entries);
    return tag;
  }
}
