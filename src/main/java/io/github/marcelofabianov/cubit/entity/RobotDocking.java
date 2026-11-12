package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockFinder;
import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.dock.DockStay;
import io.github.marcelofabianov.cubit.domain.dock.DockTrip;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingOutcome;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/** A robot and its base: going there, waiting for its turn, hibernating and leaving. */
public final class RobotDocking {
  private static final String DOCK_POSITION_KEY = "DockPosition";
  private static final double ROUTE_SPEED_MODIFIER = 1;

  private final RobotEntity robot;
  private BlockPos docked;
  private BlockPos trip;
  private DockTrip progress = DockTrip.START;
  private int repathCountdown;
  private int searchCountdown;
  private int ticksFull;

  RobotDocking(RobotEntity robot) {
    this.robot = robot;
  }

  boolean hasTrip() {
    return trip != null;
  }

  void callTo(BlockPos dock) {
    trip = dock;
    progress = DockTrip.START;
    repathCountdown = 0;
  }

  void cancelTrip() {
    trip = null;
    robot.getNavigation().stop();
  }

  /** Called on every tick of the go-to-dock goal. */
  void walk() {
    Optional<InductionDockBlockEntity> target = ownedDock(trip);
    if (target.isEmpty()) {
      cancelTrip();
      return;
    }
    InductionDockBlockEntity dock = target.get();
    Vec3 spot = dock.robotSpot();
    double distance = robot.position().distanceTo(spot);
    DockTrip.Step step =
        progress.advance(distance, CubitMod.CONFIG.dockStuckTicks(), dock.isOccupied());
    progress = step.trip();
    switch (step.action()) {
      case WALK -> routeTo(trip, dock);
      case ARRIVE -> arrive(dock, spot);
      case TELEPORT -> teleportInto(dock, spot);
    }
  }

  /**
   * Heads for the block in front: the game cannot route to a spot inside a solid block. A failed
   * route needs no handling here, because a robot that does not advance ends up stuck.
   */
  private void routeTo(BlockPos position, InductionDockBlockEntity dock) {
    if (--repathCountdown > 0) {
      return;
    }
    repathCountdown = CubitMod.CONFIG.followRepathTicks();
    BlockPos front = position.relative(dock.getBlockState().getValue(InductionDockBlock.FACING));
    robot.getNavigation().moveTo(front.getX(), front.getY(), front.getZ(), ROUTE_SPEED_MODIFIER);
  }

  /** Without a free dock it waits by this one, in order of arrival, and looks for another. */
  private void arrive(InductionDockBlockEntity dock, Vec3 spot) {
    if (DockTrip.entersOnArrival(dock.isOccupied(), dock.isNextInLine(robot.getUUID()))) {
      enter(dock, spot);
      return;
    }
    robot.getNavigation().stop();
    dock.reportWaiting(robot.getUUID());
    if (--searchCountdown <= 0) {
      searchCountdown = CubitMod.CONFIG.followRepathTicks();
      goToAnotherFreeDock();
    }
  }

  private void goToAnotherFreeDock() {
    Optional<UUID> owner = robot.ownerId();
    if (owner.isPresent() && robot.level() instanceof ServerLevel level) {
      DockFinder.nearestFreeDock(level, robot.blockPosition(), robot.dockCallRadius(), owner.get())
          .ifPresent(free -> callTo(free.getBlockPos()));
    }
  }

  /** Without a safe place to appear, the robot gives up and goes back to what it was doing. */
  private void teleportInto(InductionDockBlockEntity dock, Vec3 spot) {
    Level level = robot.level();
    boolean free =
        !dock.isOccupied()
            && level.noCollision(
                robot, robot.getBoundingBox().move(spot.subtract(robot.position())));
    if (free) {
      enter(dock, spot);
    } else {
      cancelTrip();
    }
  }

  /** For a robot just placed on the dock: it has no way to walk there. */
  public void enterNow(InductionDockBlockEntity dock) {
    enter(dock, dock.robotSpot());
  }

  private void enter(InductionDockBlockEntity dock, Vec3 spot) {
    Direction front = dock.getBlockState().getValue(InductionDockBlock.FACING);
    float yaw = front.toYRot();
    robot.moveTo(spot.x, spot.y, spot.z, yaw, 0);
    robot.setYHeadRot(yaw);
    robot.setYBodyRot(yaw);
    robot.setDeltaMovement(Vec3.ZERO);
    robot.getNavigation().stop();
    trip = null;
    docked = dock.getBlockPos();
    ticksFull = 0;
    robot.setPowerState(PowerState.HIBERNATING);
    Player owner = robot.onlineOwner();
    if (owner != null) {
      RobotFollowing.release(robot, owner);
    }
    dock.reportRobot(robot.getUUID(), false);
  }

  /** Called on every tick of the robot, on the server. */
  void tick() {
    if (robot.powerState() == PowerState.ACTIVE) {
      return;
    }
    trip = null;
    if (robot.powerState() != PowerState.HIBERNATING) {
      return;
    }
    Optional<InductionDockBlockEntity> dock = ownedDock(docked);
    if (dock.isEmpty()) {
      release();
      return;
    }
    hibernate(dock.get());
  }

  private void hibernate(InductionDockBlockEntity dock) {
    TierSpec spec = robot.spec();
    Battery battery = dock.charge(robot.battery(), spec.rechargePerTick());
    robot.setBattery(battery);
    robot.heal(DockStay.healthPerTick(spec.maxHealth(), CubitMod.CONFIG.dockFullHealTicks()));
    ticksFull = battery.isFull() ? ticksFull + 1 : 0;
    dock.reportRobot(robot.getUUID(), battery.isFull());
    boolean done =
        DockStay.shouldLeave(battery, ticksFull, CubitMod.CONFIG.dockCompleteHoldTicks());
    if (done && robot.onlineOwner() != null) {
      release();
    }
  }

  /** Leaving follows the rule of placing a robot, and an offline owner cannot be asked. */
  void release() {
    Player owner = robot.onlineOwner();
    if (owner == null) {
      robot.setPowerState(PowerState.INACTIVE_SWITCHED_OFF);
    } else {
      FollowingOutcome outcome =
          RobotFollowing.policy()
              .onArrival(robot.getUUID(), RobotFollowing.follower(owner), robot.battery());
      owner.setData(CubitAttachments.FOLLOWING_ROBOT, new FollowingRobotLink(outcome.follower()));
      robot.setPowerState(outcome.state());
    }
    stepOff();
    docked = null;
  }

  /** Clears the plate, so the next robot fits, even for a robot that stays switched off. */
  private void stepOff() {
    if (docked == null || !robot.level().hasChunkAt(docked)) {
      return;
    }
    BlockState state = robot.level().getBlockState(docked);
    if (!state.hasProperty(InductionDockBlock.FACING)) {
      return;
    }
    BlockPos front = docked.relative(state.getValue(InductionDockBlock.FACING));
    if (DamagingBlocks.safeToStand(robot, front)) {
      Vec3 spot = Vec3.atBottomCenterOf(front);
      robot.moveTo(spot.x, spot.y, spot.z, robot.getYRot(), 0);
    }
  }

  /** Checks the chunk first: reading a block must never load one (AGENTS.md, rule 22). */
  private Optional<InductionDockBlockEntity> ownedDock(BlockPos position) {
    Level level = robot.level();
    if (position == null
        || !level.hasChunkAt(position)
        || !(level.getBlockEntity(position) instanceof InductionDockBlockEntity dock)
        || !Ownership.sameOwner(dock.owner(), robot.ownerId())) {
      return Optional.empty();
    }
    return Optional.of(dock);
  }

  public boolean isDockedAt(BlockPos position) {
    return position.equals(docked);
  }

  void save(CompoundTag tag) {
    if (docked != null) {
      tag.putLong(DOCK_POSITION_KEY, docked.asLong());
    }
  }

  void load(CompoundTag tag) {
    docked = tag.contains(DOCK_POSITION_KEY) ? BlockPos.of(tag.getLong(DOCK_POSITION_KEY)) : null;
  }
}
