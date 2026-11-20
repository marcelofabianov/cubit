package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.FishingSpot;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import java.util.EnumSet;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * The Fisher in its area, in place of the round: it scans for a spot, walks to the stand and fishes
 * facing the water. A stand it cannot reach is dropped, and it looks again (answers 1188 and 1204).
 * Unloading comes first, by its priority.
 */
final class FishingGoal extends Goal {
  /** The robot speed is already in its movement attribute, so the route does not change it. */
  private static final double ROUTE_SPEED_MODIFIER = 1;

  private static final double ARRIVED_BLOCKS = 0.75;
  private static final double PROGRESS_BLOCKS = 0.5;

  private final RobotEntity robot;
  private int repathCountdown;
  private double nearest;
  private int stuckTicks;

  FishingGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return fishing().active() && fishing().reason().isEmpty();
  }

  @Override
  public boolean canContinueToUse() {
    return canUse();
  }

  /** The wait of the catch counts ticks, so this goal runs on all of them. */
  @Override
  public boolean requiresUpdateEveryTick() {
    return true;
  }

  @Override
  public void start() {
    repathCountdown = 0;
    resetProgress();
  }

  @Override
  public void stop() {
    robot.getNavigation().stop();
    fishing().setFishing(false);
  }

  @Override
  public void tick() {
    Optional<WorkArea> area = robot.workArea().here();
    if (area.isEmpty()) {
      return;
    }
    Optional<FishingSpot> spot = fishing().spot();
    if (spot.isEmpty()) {
      fishing().search(area.get());
      return;
    }
    Vec3 stand = Vec3.atBottomCenterOf(BlockPos.of(spot.get().stand()));
    double distance = robot.position().subtract(stand).horizontalDistance();
    if (distance < ARRIVED_BLOCKS) {
      fishAt(spot.get());
      return;
    }
    fishing().setFishing(false);
    walkTo(stand, distance);
  }

  private void fishAt(FishingSpot spot) {
    robot.getNavigation().stop();
    Vec3 water = Vec3.atCenterOf(BlockPos.of(spot.water()));
    robot.getLookControl().setLookAt(water.x, water.y, water.z);
    fishing().fish();
  }

  private void walkTo(Vec3 stand, double distance) {
    if (stuck(distance)) {
      fishing().giveUp();
      resetProgress();
      return;
    }
    if (--repathCountdown <= 0) {
      repathCountdown = CubitMod.CONFIG.followRepathTicks();
      robot.getNavigation().moveTo(stand.x, stand.y, stand.z, ROUTE_SPEED_MODIFIER);
    }
  }

  private boolean stuck(double distance) {
    if (distance < nearest - PROGRESS_BLOCKS) {
      nearest = distance;
      stuckTicks = 0;
      return false;
    }
    return ++stuckTicks >= CubitMod.CONFIG.dockStuckTicks();
  }

  private void resetProgress() {
    nearest = Double.MAX_VALUE;
    stuckTicks = 0;
  }

  private RobotFishing fishing() {
    return robot.workArea().work().fishing();
  }
}
