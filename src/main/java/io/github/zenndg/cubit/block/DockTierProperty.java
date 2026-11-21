package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.domain.tier.RobotTier;
import net.minecraft.util.StringRepresentable;

/**
 * The tier of a dock as a block state, which picks its band and its body (answer 1379). The names
 * are the keys of the tiers, a contract with the saves.
 */
public enum DockTierProperty implements StringRepresentable {
  BASE(RobotTier.BASE),
  ADVANCED(RobotTier.ADVANCED),
  ELITE(RobotTier.ELITE),
  ULTIMATE(RobotTier.ULTIMATE);

  private final RobotTier tier;

  DockTierProperty(RobotTier tier) {
    this.tier = tier;
  }

  public static DockTierProperty of(RobotTier tier) {
    for (DockTierProperty property : values()) {
      if (property.tier == tier) {
        return property;
      }
    }
    return BASE;
  }

  public RobotTier tier() {
    return tier;
  }

  @Override
  public String getSerializedName() {
    return tier.key();
  }
}
