package io.github.marcelofabianov.cubit.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;

/**
 * A bobber that never enters the world, only for the fishing table: the game gives treasure only
 * when the bobber in the loot context says it is in open water (answers 248 and 1183).
 */
final class VirtualFishingHook extends FishingHook {
  private final boolean open;

  VirtualFishingHook(Level level, boolean open) {
    super(EntityType.FISHING_BOBBER, level);
    this.open = open;
  }

  @Override
  public boolean isOpenWaterFishing() {
    return open;
  }
}
