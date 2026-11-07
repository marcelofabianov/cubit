package io.github.marcelofabianov.cubit.entity;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.SavedEnergy;
import io.github.marcelofabianov.cubit.item.SavedOreFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import org.slf4j.Logger;

/**
 * Writes and reads what a robot saves, under names that are a contract with existing worlds. An
 * invalid value becomes the safest one and goes to the log.
 */
final class SavedRobotValues {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String OWNER_KEY = "Owner";
  private static final String TIER_KEY = "Tier";
  private static final String ENERGY_KEY = "Energy";
  private static final String POWER_STATE_KEY = "PowerState";
  private static final String INVENTORY_KEY = "Inventory";
  private static final String ENERGY_SLOT_KEY = "EnergySlot";
  private static final String LAMP_KEY = "LampEnabled";
  private static final String COLOR_KEY = "Color";
  private static final String SCANNERS_KEY = "ScannersEnabled";
  private static final String ORE_FILTER_KEY = "OreFilter";
  private static final String CHATTER_KEY = "ChatterEnabled";
  private static final String LAST_SAFE_POSITION_KEY = "LastSafePosition";
  private static final String NO_OWNER_MESSAGE = "Robot {} has no owner. Nobody can interact.";
  private static final String INVALID_TIER_MESSAGE = "Robot {} has an invalid tier: {}";
  private static final String INVALID_COLOR_MESSAGE = "Robot {} has an invalid color: {}";
  private static final String INVALID_STATE_MESSAGE = "Robot {} has an invalid power state: {}";

  private SavedRobotValues() {}

  static void write(CompoundTag tag, RobotEntity robot) {
    robot.ownerId().ifPresent(owner -> tag.putUUID(OWNER_KEY, owner));
    tag.putString(TIER_KEY, robot.state().tier().key());
    tag.putLong(ENERGY_KEY, robot.battery().charge().amount());
    tag.putString(POWER_STATE_KEY, robot.powerState().name());
    RobotInventory inventory = robot.robotInventory();
    tag.put(INVENTORY_KEY, inventory.save(robot.registryAccess()));
    tag.put(ENERGY_SLOT_KEY, inventory.energySlot().save(robot.registryAccess()));
    robot.docking().save(tag);
    tag.putBoolean(LAMP_KEY, robot.state().lampOn());
    tag.putString(COLOR_KEY, robot.state().color().key());
    tag.putBoolean(SCANNERS_KEY, robot.state().scannersOn());
    tag.putBoolean(CHATTER_KEY, robot.state().chatterOn());
    ListTag filter = new ListTag();
    SavedOreFilter.tagsOf(robot.state().oreFilter())
        .forEach(ore -> filter.add(StringTag.valueOf(ore)));
    tag.put(ORE_FILTER_KEY, filter);
    robot
        .lastSafePosition()
        .ifPresent(position -> tag.putLong(LAST_SAFE_POSITION_KEY, position.asLong()));
  }

  static UUID owner(CompoundTag tag, UUID robot) {
    if (tag.hasUUID(OWNER_KEY)) {
      return tag.getUUID(OWNER_KEY);
    }
    LOGGER.warn(NO_OWNER_MESSAGE, robot);
    return null;
  }

  static Energy energy(CompoundTag tag) {
    return SavedEnergy.of(tag.getLong(ENERGY_KEY));
  }

  static void readContents(
      CompoundTag tag, RobotInventory inventory, HolderLookup.Provider registries, int slots) {
    inventory.load(tag.getList(INVENTORY_KEY, Tag.TAG_COMPOUND), registries, slots);
    inventory.energySlot().load(tag.getCompound(ENERGY_SLOT_KEY), registries);
  }

  /** Without the key, the robot was saved before the lamp existed, so the lamp stays on. */
  static boolean lampOn(CompoundTag tag) {
    return !tag.contains(LAMP_KEY) || tag.getBoolean(LAMP_KEY);
  }

  /** Without the key, the robot was saved before the chatter existed, so it stays on. */
  static boolean chatterOn(CompoundTag tag) {
    return !tag.contains(CHATTER_KEY) || tag.getBoolean(CHATTER_KEY);
  }

  /** Without the key, the robot was saved before the scanners existed, so they stay on. */
  static boolean scannersOn(CompoundTag tag) {
    return !tag.contains(SCANNERS_KEY) || tag.getBoolean(SCANNERS_KEY);
  }

  /** Without the key, the list is empty, which is the filter of a robot saved before it existed. */
  static OreFilter oreFilter(CompoundTag tag) {
    ListTag saved = tag.getList(ORE_FILTER_KEY, Tag.TAG_STRING);
    List<String> tags = new ArrayList<>();
    for (int index = 0; index < saved.size(); index++) {
      tags.add(saved.getString(index));
    }
    return SavedOreFilter.of(tags);
  }

  /** Without the key, the robot was saved before the colors existed, so it is white. */
  static RobotColor color(CompoundTag tag, UUID robot) {
    if (!tag.contains(COLOR_KEY)) {
      return RobotColor.WHITE;
    }
    String key = tag.getString(COLOR_KEY);
    return RobotColor.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(INVALID_COLOR_MESSAGE, robot, key);
          return RobotColor.WHITE;
        });
  }

  static BlockPos lastSafePosition(CompoundTag tag) {
    return tag.contains(LAST_SAFE_POSITION_KEY)
        ? BlockPos.of(tag.getLong(LAST_SAFE_POSITION_KEY))
        : null;
  }

  static RobotTier tier(CompoundTag tag, UUID robot) {
    String key = tag.getString(TIER_KEY);
    return RobotTier.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(INVALID_TIER_MESSAGE, robot, key);
          return RobotTier.BASE;
        });
  }

  static PowerState powerState(CompoundTag tag, UUID robot) {
    String name = tag.getString(POWER_STATE_KEY);
    return Arrays.stream(PowerState.values())
        .filter(state -> state.name().equals(name))
        .findFirst()
        .orElseGet(() -> {
          LOGGER.warn(INVALID_STATE_MESSAGE, robot, name);
          return PowerState.INACTIVE_SWITCHED_OFF;
        });
  }
}
