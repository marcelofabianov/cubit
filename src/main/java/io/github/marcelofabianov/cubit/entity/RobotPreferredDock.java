package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockFinder;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.dock.DockGoal;
import io.github.marcelofabianov.cubit.domain.dock.PreferredDock;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.menu.DockGoalControl;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The Dock the owner marked with the Dock app of the Tablet, and where `Go to Dock` sends the
 * Cubit: the marked Dock in reach, or the nearest free one (answers 1041 and 1042). Only the loaded
 * chunks are read (rule 22), and the screen asks again only once per interval (rule 21).
 */
public final class RobotPreferredDock implements DockGoalControl {
  private static final String KEY = "PreferredDock";
  private static final String BLOCK_KEY = "Block";
  private static final String DIMENSION_KEY = "Dimension";

  private final RobotEntity robot;
  private Optional<PreferredDock> marked = Optional.empty();
  private DockGoal goal = DockGoal.NO_DOCK;
  private long nextCheck = Long.MIN_VALUE;

  RobotPreferredDock(RobotEntity robot) {
    this.robot = robot;
  }

  public Optional<PreferredDock> marked() {
    return marked;
  }

  /** A click of the owner on one of their Docks marks it, and on the marked one removes it. */
  public boolean click(Player player, InductionDockBlockEntity dock) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || !dock.isOwner(player)) {
      return false;
    }
    String dimension = dock.getLevel().dimension().location().toString();
    marked =
        PreferredDock.clicked(marked, new PreferredDock(dock.getBlockPos().asLong(), dimension));
    nextCheck = Long.MIN_VALUE;
    return true;
  }

  /** For the screen of the Tablet, read again once per interval of the dock search. */
  @Override
  public DockGoal goal() {
    long now = robot.level().getGameTime();
    if (now >= nextCheck) {
      nextCheck = now + CubitMod.CONFIG.followRepathTicks();
      goal = DockGoal.of(robot.powerState(), dockToGo().isPresent());
    }
    return goal;
  }

  /** Sends the Cubit, checked again now, only for the owner. */
  @Override
  public boolean go(Player player) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID())) {
      return false;
    }
    Optional<BlockPos> dock = dockToGo();
    nextCheck = Long.MIN_VALUE;
    if (!DockGoal.of(robot.powerState(), dock.isPresent()).ready()) {
      return false;
    }
    robot.callToDock(dock.get());
    return true;
  }

  /** The Dock the Cubit would go to now, also for the auto return (answer 1067). */
  Optional<BlockPos> dockToGo() {
    Optional<UUID> owner = robot.ownerId();
    if (owner.isEmpty() || !(robot.level() instanceof ServerLevel level)) {
      return Optional.empty();
    }
    Optional<BlockPos> preferred = preferredInReach(level);
    if (preferred.isPresent()) {
      return preferred;
    }
    return DockFinder.nearestFreeDock(level, robot.blockPosition(), radius(), owner.get())
        .map(InductionDockBlockEntity::getBlockPos);
  }

  /** A marked Dock that is gone is forgotten, once its loaded chunk shows it (answer 1042). */
  private Optional<BlockPos> preferredInReach(Level level) {
    if (marked.isEmpty() || !marked.get().isIn(level.dimension().location().toString())) {
      return Optional.empty();
    }
    BlockPos block = BlockPos.of(marked.get().block());
    double reach = (double) radius() * radius();
    if (!level.hasChunkAt(block) || block.distSqr(robot.blockPosition()) > reach) {
      return Optional.empty();
    }
    if (level.getBlockEntity(block) instanceof InductionDockBlockEntity dock
        && Ownership.sameOwner(dock.owner(), robot.ownerId())) {
      return Optional.of(block);
    }
    marked = Optional.empty();
    return Optional.empty();
  }

  private int radius() {
    return robot.dockCallRadius();
  }

  void save(CompoundTag tag) {
    marked.ifPresent(dock -> {
      CompoundTag saved = new CompoundTag();
      saved.putLong(BLOCK_KEY, dock.block());
      saved.putString(DIMENSION_KEY, dock.dimension());
      tag.put(KEY, saved);
    });
  }

  /** A save without the key, or with a broken one, has no marked Dock (rule 17). */
  void load(CompoundTag tag) {
    CompoundTag saved = tag.getCompound(KEY);
    String dimension = saved.getString(DIMENSION_KEY);
    marked =
        saved.contains(BLOCK_KEY) && !dimension.isBlank()
            ? Optional.of(new PreferredDock(saved.getLong(BLOCK_KEY), dimension))
            : Optional.empty();
  }
}
