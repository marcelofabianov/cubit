package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.GridCell;
import io.github.marcelofabianov.cubit.domain.behavior.LightLevel;
import io.github.marcelofabianov.cubit.domain.behavior.TorchGrid;
import io.github.marcelofabianov.cubit.domain.behavior.TorchRules;
import io.github.marcelofabianov.cubit.domain.tier.TorchSpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Reads the blocks around the robot once into a {@link TorchGrid}: what stops the light, where a
 * mob could spawn and where a torch could stand (answers 971 and 975 to 977). Only loaded chunks
 * are read, and a box with an unloaded chunk is skipped (rule 22).
 */
final class TorchScan {
  private final BlockPos corner;
  private final TorchGrid grid;
  private final Map<GridCell, BlockState> placements = new HashMap<>();

  private TorchScan(BlockPos corner, int size) {
    this.corner = corner;
    this.grid = new TorchGrid(size, size, size);
  }

  /** The torch the robot places, whose light the planner spreads. */
  static LightLevel torchLight() {
    return new LightLevel(Blocks.TORCH.defaultBlockState().getLightEmission());
  }

  static Optional<TorchScan> around(Level level, BlockPos center, Settings settings) {
    int radius = settings.radius();
    BlockPos corner = center.offset(-radius, -radius, -radius);
    BlockPos far = center.offset(radius, radius, radius);
    if (!level.hasChunksAt(corner.below(), far)) {
      return Optional.empty();
    }
    TorchScan scan = new TorchScan(corner, radius * 2 + 1);
    scan.read(level, center, settings);
    return Optional.of(scan);
  }

  TorchGrid grid() {
    return grid;
  }

  /** The block and the state of the torch for a cell the planner chose. */
  Optional<Placement> placement(GridCell cell) {
    return Optional.ofNullable(placements.get(cell))
        .map(state -> new Placement(corner.offset(cell.x(), cell.y(), cell.z()), state));
  }

  private void read(Level level, BlockPos center, Settings settings) {
    int size = settings.radius() * 2 + 1;
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
    for (int y = 0; y < size; y++) {
      for (int z = 0; z < size; z++) {
        for (int x = 0; x < size; x++) {
          pos.setWithOffset(corner, x, y, z);
          readCell(level, pos.immutable(), new GridCell(x, y, z), center, settings);
        }
      }
    }
  }

  private void readCell(
      Level level, BlockPos pos, GridCell cell, BlockPos center, Settings settings) {
    BlockState state = level.getBlockState(pos);
    if (state.getLightBlock(level, pos) >= LightLevel.MAX) {
      grid.markBlocking(cell);
      return;
    }
    if (dangerous(level, pos, state, settings)) {
      grid.markDangerous(cell);
    }
    if (pos.distSqr(center) <= (double) settings.reach() * settings.reach()) {
      torchFor(level, pos, state)
          .ifPresent(torch -> {
            grid.addCandidate(cell);
            placements.put(cell, torch);
          });
    }
  }

  /** A spawn space, as the game checks it, with too little block light now (answer 975). */
  private static boolean dangerous(Level level, BlockPos pos, BlockState state, Settings settings) {
    BlockPos above = pos.above();
    BlockPos below = pos.below();
    boolean space =
        NaturalSpawner.isValidEmptySpawnBlock(
                level, pos, state, state.getFluidState(), EntityType.ZOMBIE)
            && NaturalSpawner.isValidEmptySpawnBlock(
                level,
                above,
                level.getBlockState(above),
                level.getFluidState(above),
                EntityType.ZOMBIE)
            && level.getBlockState(below).isValidSpawn(level, below, EntityType.ZOMBIE);
    return space
        && TorchRules.dangerous(
            new LightLevel(level.getBrightness(LightLayer.BLOCK, pos)),
            settings.spec(),
            level.canSeeSky(pos),
            new LightLevel(level.getMaxLocalRawBrightness(pos)),
            settings.lampOnBelow());
  }

  /** On the floor first, or on a wall, wherever the game lets a player put a torch (977). */
  private static Optional<BlockState> torchFor(Level level, BlockPos pos, BlockState state) {
    if (!state.canBeReplaced() || !state.getFluidState().isEmpty()) {
      return Optional.empty();
    }
    BlockState floor = Blocks.TORCH.defaultBlockState();
    if (floor.canSurvive(level, pos)) {
      return Optional.of(floor);
    }
    for (Direction facing : Direction.Plane.HORIZONTAL) {
      BlockState wall =
          Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, facing);
      if (wall.canSurvive(level, pos)) {
        return Optional.of(wall);
      }
    }
    return Optional.empty();
  }

  /** The numbers of one scan, from the config and the tier. */
  record Settings(int radius, int reach, TorchSpec spec, LightLevel lampOnBelow) {}

  /** Where a torch goes and in which state. */
  record Placement(BlockPos pos, BlockState state) {}
}
