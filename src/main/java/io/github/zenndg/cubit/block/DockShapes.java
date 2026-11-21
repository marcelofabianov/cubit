package io.github.zenndg.cubit.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The shape of the dock: a low plate and the tower behind it, by facing, the spot where a robot
 * stands, and the item saved in its input slot.
 */
final class DockShapes {
  private static final double FULL = 16;
  private static final double PLATE_HEIGHT = 3;
  private static final double UPPER_TOWER_HEIGHT = 12;
  private static final double TOWER_NEAR = 3.5;
  private static final double TOWER_FAR = 12.5;
  private static final double TOWER_DEPTH = 4;
  private static final VoxelShape PLATE = Block.box(0, 0, 0, FULL, PLATE_HEIGHT, FULL);

  private static final double ROBOT_PLATE_HEIGHT = 3.0 / 16;

  /** 12 units away from the tower: exactly the width of a robot, and a little margin. */
  private static final double TOWARD_FRONT = 2.05 / 16;

  private DockShapes() {}

  /** Where a robot stands, between the front of the plate and the tower. */
  static Vec3 robotSpot(Direction front, BlockPos pos) {
    return Vec3.atBottomCenterOf(pos)
        .add(front.getStepX() * TOWARD_FRONT, ROBOT_PLATE_HEIGHT, front.getStepZ() * TOWARD_FRONT);
  }

  static ItemStack savedStack(CompoundTag tag, String key, HolderLookup.Provider registries) {
    return tag.contains(key)
        ? ItemStack.parse(registries, tag.getCompound(key)).orElse(ItemStack.EMPTY)
        : ItemStack.EMPTY;
  }

  static VoxelShape of(Direction facing, boolean lower) {
    return lower ? Shapes.or(PLATE, tower(facing, FULL)) : tower(facing, UPPER_TOWER_HEIGHT);
  }

  /** The tower stands on the side opposite to the front. */
  private static VoxelShape tower(Direction facing, double height) {
    return switch (facing) {
      case SOUTH -> Block.box(TOWER_NEAR, 0, 0, TOWER_FAR, height, TOWER_DEPTH);
      case EAST -> Block.box(0, 0, TOWER_NEAR, TOWER_DEPTH, height, TOWER_FAR);
      case WEST -> Block.box(FULL - TOWER_DEPTH, 0, TOWER_NEAR, FULL, height, TOWER_FAR);
      default -> Block.box(TOWER_NEAR, 0, FULL - TOWER_DEPTH, TOWER_FAR, height, FULL);
    };
  }
}
