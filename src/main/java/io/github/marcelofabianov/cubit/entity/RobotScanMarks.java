package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.tier.ScannerSpec;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.network.MobMarksPayload;
import io.github.marcelofabianov.cubit.network.OreMarksPayload;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Tells the owner where the scanners found something, so only the client of the owner draws a box
 * around it (answers 557, 608 and 613). Nothing here is saved, because it is recalculated.
 */
public final class RobotScanMarks {
  /**
   * A mob box waits a little past the next scan, so it does not blink while the scan refreshes it.
   * Only visual, so it stays here and not in the config (question 398).
   */
  private static final int MOB_GRACE_TICKS = 20;

  private final RobotEntity robot;
  private List<BlockPos> lastOres = List.of();
  private List<Integer> lastMobs = List.of();

  RobotScanMarks(RobotEntity robot) {
    this.robot = robot;
  }

  /** The new ores the last sweep marked. */
  public List<BlockPos> lastOres() {
    return lastOres;
  }

  /** The network IDs of the mobs the last scan marked. */
  public List<Integer> lastMobs() {
    return lastMobs;
  }

  /** Every new ore of the sweep, even when its text still waits the interval (answer 597). */
  void markOres(Player owner, Collection<Long> positions) {
    lastOres = positions.stream().map(BlockPos::of).toList();
    if (!lastOres.isEmpty() && owner instanceof ServerPlayer player) {
      CubitNetwork.sendOreMarks(
          player, new OreMarksPayload(lastOres, CubitMod.CONFIG.scanMarkTicks()));
    }
  }

  /** Each scan replaces the boxes, so a scan without mobs clears what the last one marked. */
  void markMobs(Player owner, List<Integer> mobIds, ScannerSpec spec) {
    boolean changed = !mobIds.isEmpty() || !lastMobs.isEmpty();
    lastMobs = mobIds;
    if (changed && owner instanceof ServerPlayer player) {
      int holdTicks = spec.mobIntervalTicks() + MOB_GRACE_TICKS;
      CubitNetwork.sendMobMarks(
          player, new MobMarksPayload(robot.getId(), spec.mobRadiusBlocks(), mobIds, holdTicks));
    }
  }
}
