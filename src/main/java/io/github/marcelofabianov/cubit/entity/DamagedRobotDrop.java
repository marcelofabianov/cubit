package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Leaves the item of a dead robot. Added directly, because loot rules could lose the robot. */
final class DamagedRobotDrop {
  private DamagedRobotDrop() {}

  static void leave(
      ServerLevel level,
      RobotEntity robot,
      Item item,
      BlockPos lastSafePosition,
      RobotInventory inventory) {
    ItemStack stack = new ItemStack(item);
    stack.set(CubitDataComponents.ROBOT_STATE.get(), robot.state().damaged());
    stack.set(CubitDataComponents.ROBOT_OWNER.get(), robot.ownerId().orElse(null));
    stack.set(DataComponents.CUSTOM_NAME, robot.getCustomName());
    inventory.writeTo(stack);
    inventory.clear();
    if (lastSafePosition != null) {
      stack.set(
          CubitDataComponents.LAST_SAFE_POSITION.get(),
          GlobalPos.of(level.dimension(), lastSafePosition));
    }
    ItemEntity drop = new ItemEntity(level, robot.getX(), robot.getY(), robot.getZ(), stack);
    drop.setDefaultPickUpDelay();
    level.addFreshEntity(drop);
  }
}
