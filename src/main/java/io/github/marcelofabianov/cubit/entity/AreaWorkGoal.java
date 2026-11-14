package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.AreaCorner;
import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * The Cubit in its area, a volume between two corners: it walks there, or appears inside when
 * stuck, and then goes round the edges, from corner to corner, at the height it walks (answers 342,
 * 1058, 1059 and 1062). Fetching an item comes first, by its priority.
 */
final class AreaWorkGoal extends Goal {
  /** The robot speed is already in its movement attribute, so the route does not change it. */
  private static final double ROUTE_SPEED_MODIFIER = 1;

  private static final double ARRIVED_BLOCKS = 1.5;
  private static final double PROGRESS_BLOCKS = 0.5;
  private static final double HALF = 0.5;
  private static final int TELEPORT_ATTEMPTS = 16;

  private final RobotEntity robot;
  private int corner;
  private int repathCountdown;
  private double nearest;
  private int stuckTicks;

  AreaWorkGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  @Override
  public boolean canUse() {
    return robot.workArea().working();
  }

  @Override
  public boolean canContinueToUse() {
    return canUse();
  }

  /** The stuck time counts ticks, so this goal runs on all of them. */
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
  }

  @Override
  public void tick() {
    WorkArea area = robot.workArea().here().orElse(null);
    if (area == null) {
      return;
    }
    boolean inside = area.contains(robot.getX(), robot.getY(), robot.getZ());
    Vec3 target = inside ? spotOf(area, area.corner(corner)) : centerOf(area);
    double distance = robot.position().subtract(target).horizontalDistance();
    if (inside && distance < ARRIVED_BLOCKS) {
      nextCorner();
      return;
    }
    if (stuck(distance)) {
      unstick(area, inside);
      return;
    }
    if (--repathCountdown <= 0) {
      repathCountdown = CubitMod.CONFIG.followRepathTicks();
      robot.getNavigation().moveTo(target.x, target.y, target.z, ROUTE_SPEED_MODIFIER);
    }
  }

  /** A corner it cannot reach is skipped, and outside the area it appears inside (answer 1058). */
  private void unstick(WorkArea area, boolean inside) {
    if (inside) {
      nextCorner();
    } else {
      teleportInto(area);
      resetProgress();
    }
  }

  private void nextCorner() {
    corner++;
    repathCountdown = 0;
    resetProgress();
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

  /** Without a safe spot, the robot stays and tries again after the next stuck time. */
  private void teleportInto(WorkArea area) {
    for (int attempt = 0; attempt < TELEPORT_ATTEMPTS; attempt++) {
      BlockPos target =
          new BlockPos(
              between(area.min().x(), area.max().x()),
              between(area.min().y() + 1, area.max().y() + 1),
              between(area.min().z(), area.max().z()));
      if (FollowOwnerGoal.canStandAt(robot, target)) {
        Vec3 spot = Vec3.atBottomCenterOf(target);
        robot.moveTo(spot.x, spot.y, spot.z, robot.getYRot(), robot.getXRot());
        robot.getNavigation().stop();
        return;
      }
    }
  }

  private int between(int low, int high) {
    return robot.getRandom().nextIntBetweenInclusive(low, high);
  }

  /**
   * The corner of the base at the height the Cubit is, within the area: the area is a volume, so
   * the round follows its edges on the level the Cubit walks, not under the ground.
   */
  private Vec3 spotOf(WorkArea area, AreaCorner corner) {
    return new Vec3(corner.x() + HALF, levelIn(area), corner.z() + HALF);
  }

  private Vec3 centerOf(WorkArea area) {
    return new Vec3(area.centerX(), levelIn(area), area.centerZ());
  }

  /** From standing on the lowest marked block to standing on the highest. */
  private double levelIn(WorkArea area) {
    return Math.clamp(robot.getY(), area.min().y() + 1, area.max().y() + 1);
  }
}
