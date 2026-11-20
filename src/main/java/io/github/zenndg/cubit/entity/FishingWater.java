package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.behavior.OpenWater;
import io.github.zenndg.cubit.domain.behavior.WaterCell;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

/**
 * Reads the water for the Fisher as FishingHook does, never in an unloaded chunk (rule 22), and
 * counts the blocks it read, so the scan keeps to its budget (rule 21).
 */
final class FishingWater {
  private static final int LAYER_SIDE = OpenWater.HALF_SIDE * 2 + 1;
  private static final int LAYER_BLOCKS = LAYER_SIDE * LAYER_SIDE;

  private final Level level;
  private int reads;

  FishingWater(Level level) {
    this.level = level;
  }

  int reads() {
    return reads;
  }

  /** As FishingHook.getOpenWaterTypeForBlock, with an unloaded block as broken water. */
  WaterCell cell(BlockPos pos) {
    reads++;
    if (!level.isLoaded(pos)) {
      return WaterCell.INVALID;
    }
    BlockState state = level.getBlockState(pos);
    if (state.isAir() || state.is(Blocks.LILY_PAD)) {
      return WaterCell.ABOVE_WATER;
    }
    FluidState fluid = state.getFluidState();
    boolean still = fluid.is(FluidTags.WATER) && fluid.isSource();
    return still && state.getCollisionShape(level, pos).isEmpty()
        ? WaterCell.INSIDE_WATER
        : WaterCell.INVALID;
  }

  /** Still water with room above, where a bobber would float. */
  boolean fishable(BlockPos water) {
    return cell(water) == WaterCell.INSIDE_WATER && cell(water.above()) == WaterCell.ABOVE_WATER;
  }

  /** The open water of the game around the point (answer 1183). */
  boolean open(BlockPos water) {
    List<WaterCell> layers = new ArrayList<>();
    for (int dy = OpenWater.LOWEST_LAYER; dy <= OpenWater.HIGHEST_LAYER; dy++) {
      layers.add(OpenWater.layer(layer(water.above(dy))));
    }
    return OpenWater.isOpen(layers);
  }

  private List<WaterCell> layer(BlockPos center) {
    List<WaterCell> cells = new ArrayList<>(LAYER_BLOCKS);
    for (int dx = -OpenWater.HALF_SIDE; dx <= OpenWater.HALF_SIDE; dx++) {
      for (int dz = -OpenWater.HALF_SIDE; dz <= OpenWater.HALF_SIDE; dz++) {
        cells.add(cell(center.offset(dx, 0, dz)));
      }
    }
    return cells;
  }
}
