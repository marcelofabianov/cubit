package io.github.zenndg.cubit.block;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.dock.DockSpec;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import org.slf4j.Logger;

/**
 * The tier of a dock, kept in the block as `Tier` and in its item as `cubit:dock_tier`. A dock
 * without it, placed before the tiers, is a Base one (answer 1373).
 */
final class DockTier {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String TIER_KEY = "Tier";
  private static final String UNKNOWN_TIER = "The dock at {} has an unknown tier {}. Using Base.";

  private RobotTier tier = RobotTier.BASE;

  RobotTier tier() {
    return tier;
  }

  void set(RobotTier tier) {
    this.tier = tier;
  }

  DockSpec spec() {
    return CubitMod.CONFIG.dockCatalog().dock(tier);
  }

  void save(CompoundTag tag) {
    tag.putString(TIER_KEY, tier.key());
  }

  void load(CompoundTag tag, BlockPos pos) {
    tier = read(tag.getString(TIER_KEY), pos);
  }

  void collect(DataComponentMap.Builder components) {
    components.set(CubitDataComponents.DOCK_TIER.get(), tier);
  }

  private static RobotTier read(String key, BlockPos pos) {
    if (key.isEmpty()) {
      return RobotTier.BASE;
    }
    return RobotTier.fromKey(key)
        .orElseGet(() -> {
          LOGGER.warn(UNKNOWN_TIER, pos, key);
          return RobotTier.BASE;
        });
  }
}
