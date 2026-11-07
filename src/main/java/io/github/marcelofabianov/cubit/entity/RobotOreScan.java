package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.FreshFinds;
import io.github.marcelofabianov.cubit.domain.behavior.ScanArea;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import io.github.marcelofabianov.cubit.domain.robot.OreSignals;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

/**
 * Reads the blocks around a robot in slices, a limited number per tick, and only in loaded chunks
 * (AGENTS.md, rules 21 and 22). Nothing here is saved, because it is recalculated.
 */
public final class RobotOreScan {
  private final RobotEntity robot;
  private final BlockPos.MutableBlockPos reading = new BlockPos.MutableBlockPos();
  private final Map<Long, OreKind> found = new HashMap<>();
  private FreshFinds<Long> known = FreshFinds.none();
  private ScanArea area;
  private BlockPos center;
  private int cursor;

  RobotOreScan(RobotEntity robot) {
    this.robot = robot;
  }

  /** What an ore block is. A block in an unloaded chunk is never read, so nothing loads it. */
  public static List<OreKind> kindsAt(Level level, BlockPos pos) {
    if (!level.hasChunkAt(pos)) {
      return List.of();
    }
    BlockState state = level.getBlockState(pos);
    if (!state.is(Tags.Blocks.ORES)) {
      return List.of();
    }
    return OreKind.kindsOf(state.getTags().map(tag -> tag.location().toString()).toList());
  }

  boolean isSweeping() {
    return area != null;
  }

  void start(int radius) {
    area = new ScanArea(radius);
    center = robot.blockPosition();
    cursor = 0;
    found.clear();
  }

  /** The kinds of the blocks that became new, once a whole sweep is finished. */
  Optional<List<OreKind>> continueSweep(int blocksPerTick, OreSignals signals, OreFilter filter) {
    int end = Math.min(area.blocks(), cursor + blocksPerTick);
    for (; cursor < end; cursor++) {
      read(signals, filter);
    }
    return cursor < area.blocks() ? Optional.empty() : Optional.of(finish());
  }

  void reset() {
    area = null;
    found.clear();
    known = FreshFinds.none();
  }

  private void read(OreSignals signals, OreFilter filter) {
    reading.set(
        center.getX() + area.xOf(cursor),
        center.getY() + area.yOf(cursor),
        center.getZ() + area.zOf(cursor));
    signals
        .lead(kindsAt(robot.level(), reading), filter)
        .ifPresent(kind -> found.put(reading.asLong(), kind));
  }

  private List<OreKind> finish() {
    area = null;
    FreshFinds.Update<Long> update = known.seen(Set.copyOf(found.keySet()));
    known = update.known();
    return update.fresh().stream().map(found::get).toList();
  }
}
