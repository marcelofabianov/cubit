package io.github.zenndg.cubit.block;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.desk.DeskSpec;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;

/**
 * The tier of a desk, kept in the block as `Tier` and in its item as `cubit:engineering_desk_tier`.
 */
final class DeskTier {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String TIER_KEY = "Tier";
  private static final String UNKNOWN_TIER = "The desk at {} has an unknown tier {}. Using Base.";

  private RobotTier tier = RobotTier.BASE;

  RobotTier tier() {
    return tier;
  }

  void set(RobotTier tier) {
    this.tier = tier;
  }

  DeskSpec spec() {
    return CubitMod.CONFIG.deskCatalog().desk(tier);
  }

  void save(CompoundTag tag) {
    tag.putString(TIER_KEY, tier.key());
  }

  void load(CompoundTag tag, BlockPos pos) {
    String key = tag.getString(TIER_KEY);
    tier =
        key.isEmpty()
            ? RobotTier.BASE
            : RobotTier.fromKey(key)
                .orElseGet(() -> {
                  LOGGER.warn(UNKNOWN_TIER, pos, key);
                  return RobotTier.BASE;
                });
  }
}
