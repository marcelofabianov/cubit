package io.github.marcelofabianov.cubit.entity;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.Arrays;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;

/** Reads saved robot values. An invalid one becomes the safest value and goes to the log. */
final class SavedRobotValues {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String LAST_SAFE_POSITION_KEY = "LastSafePosition";
  private static final String INVALID_TIER_MESSAGE = "Robot {} has an invalid tier: {}";
  private static final String INVALID_STATE_MESSAGE = "Robot {} has an invalid power state: {}";

  private SavedRobotValues() {}

  static void writeLastSafePosition(CompoundTag tag, BlockPos position) {
    if (position != null) {
      tag.putLong(LAST_SAFE_POSITION_KEY, position.asLong());
    }
  }

  static BlockPos lastSafePosition(CompoundTag tag) {
    return tag.contains(LAST_SAFE_POSITION_KEY)
        ? BlockPos.of(tag.getLong(LAST_SAFE_POSITION_KEY))
        : null;
  }

  static RobotTier tier(String key, UUID robot) {
    return RobotTier.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(INVALID_TIER_MESSAGE, robot, key);
          return RobotTier.BASE;
        });
  }

  static PowerState powerState(String name, UUID robot) {
    return Arrays.stream(PowerState.values())
        .filter(state -> state.name().equals(name))
        .findFirst()
        .orElseGet(() -> {
          LOGGER.warn(INVALID_STATE_MESSAGE, robot, name);
          return PowerState.INACTIVE_SWITCHED_OFF;
        });
  }
}
