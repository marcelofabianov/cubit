package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.block.StorableRobotItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * What a robot leaves when it dies. It cannot be placed, only repaired, and it survives anything
 * except a command, because it holds what the robot carried.
 */
public final class DamagedRobotItem extends Item implements StorableRobotItem {
  public DamagedRobotItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean canFitInsideContainerItems() {
    return false;
  }

  @Override
  public boolean canBeHurtBy(ItemStack stack, DamageSource source) {
    return false;
  }

  @Override
  public int getEntityLifespan(ItemStack stack, Level level) {
    return Integer.MAX_VALUE;
  }

  /** Returning true skips the rest of the tick, which would destroy an item far below the world. */
  @Override
  public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
    entity.setUnlimitedLifetime();
    return entity.level() instanceof ServerLevel level
        && entity.getY() < level.getMinBuildHeight()
        && VoidRescue.rescue(level, entity, stack);
  }
}
