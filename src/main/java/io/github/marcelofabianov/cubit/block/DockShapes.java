package io.github.marcelofabianov.cubit.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/** The collision of the dock: a low plate, and the tower behind it, by facing. */
final class DockShapes {
  private static final double FULL = 16;
  private static final double PLATE_HEIGHT = 3;
  private static final double UPPER_TOWER_HEIGHT = 12;
  private static final double TOWER_NEAR = 3.5;
  private static final double TOWER_FAR = 12.5;
  private static final double TOWER_DEPTH = 4;
  private static final VoxelShape PLATE = Block.box(0, 0, 0, FULL, PLATE_HEIGHT, FULL);

  private DockShapes() {}

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
