package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.BurningAction;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

/** Carries out what {@link BurningAction} decides. The rule itself lives in the domain. */
final class BurningReactionGoal extends Goal {
  /** The same precision as the moves of the game: arriving next to the target counts. */
  private static final int ROUTE_PRECISION_BLOCKS = 1;

  /** How far a route may end from the water for the robot to step into it. */
  private static final int STEP_INTO_WATER_BLOCKS = 2;

  /** How many route updates the robot has to step into the water before giving up on it. */
  private static final int STEP_INTO_WATER_ROUTE_UPDATES = 2;

  private static final int WANDER_HEIGHT = 1;
  private static final int WANDER_ATTEMPTS = 8;

  private final RobotEntity robot;
  private Path path;
  private BlockPos water;
  private final Set<BlockPos> rejectedWater = new HashSet<>();
  private int searchCountdown;
  private int stepCountdown;

  BurningReactionGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE));
  }

  @Override
  public boolean canUse() {
    // The game gives no route to a mob in the air, and that must not read as water without a route.
    boolean canPlanRoute = robot.onGround() || robot.isInLiquid();
    if (!isReacting()) {
      rejectedWater.clear();
      return false;
    }
    if (!canPlanRoute) {
      return false;
    }
    water = null;
    path = null;
    // Only the water search waits: the robot must have somewhere to go right away.
    if (searchCountdown-- <= 0) {
      searchCountdown = reducedTickDelay(CubitMod.CONFIG.followRepathTicks());
      findWater().ifPresent(this::planRouteToWater);
    }
    if (water == null) {
      path = findWanderRoute();
    }
    return path != null;
  }

  @Override
  public boolean canContinueToUse() {
    boolean steppingIntoWater = water != null && stepCountdown > 0;
    return isReacting() && (steppingIntoWater || !robot.getNavigation().isDone());
  }

  @Override
  public void start() {
    robot.getNavigation().moveTo(path, CubitMod.CONFIG.burningSpeedMultiplier());
    stepCountdown =
        STEP_INTO_WATER_ROUTE_UPDATES * reducedTickDelay(CubitMod.CONFIG.followRepathTicks());
  }

  /** A route stops next to the water, so the last step into it is taken directly. */
  @Override
  public void tick() {
    if (water != null && robot.getNavigation().isDone()) {
      Vec3 center = Vec3.atBottomCenterOf(water);
      robot
          .getMoveControl()
          .setWantedPosition(
              center.x, center.y, center.z, CubitMod.CONFIG.burningSpeedMultiplier());
      stepCountdown--;
    }
  }

  @Override
  public void stop() {
    robot.getNavigation().stop();
    // Still burning here means the step failed, so the next search looks for other water.
    if (water != null && isReacting()) {
      rejectedWater.add(water);
    }
    water = null;
  }

  private boolean isReacting() {
    return BurningAction.decide(robot.powerState(), robot.isOnFire(), false) != BurningAction.NONE;
  }

  /**
   * Water without a route, like behind a wall, must not leave the robot burning in place. The game
   * never marks a water block as reached, so what counts is where the route ends.
   */
  private void planRouteToWater(BlockPos position) {
    Path route = routeTo(position);
    Node end = route == null ? null : route.getEndNode();
    if (end != null && end.asBlockPos().distManhattan(position) <= STEP_INTO_WATER_BLOCKS) {
      path = route;
      water = position;
    } else {
      rejectedWater.add(position);
    }
  }

  /**
   * Searches at any height inside the radius, because water is often up or down some steps. Only
   * water open to the sky side counts, because the ground hides water the robot cannot enter.
   */
  private Optional<BlockPos> findWater() {
    Level level = robot.level();
    int radius = CubitMod.CONFIG.waterSearchRadiusBlocks();
    return BlockPos.findClosestMatch(
        robot.blockPosition(),
        radius,
        radius,
        position -> !rejectedWater.contains(position) && isOpenWater(level, position));
  }

  /** Checks the chunk first: reading a block must never load one (AGENTS.md, rule 22). */
  private static boolean isOpenWater(Level level, BlockPos position) {
    BlockPos above = position.above();
    return level.hasChunkAt(position)
        && level.getFluidState(position).is(FluidTags.WATER)
        && level.getBlockState(above).getCollisionShape(level, above).isEmpty();
  }

  /**
   * Picks random spots and asks for a route, which already avoids lava. The random positions of the
   * game refuse any block with a route penalty, like those next to water, and would leave the robot
   * standing still.
   */
  private Path findWanderRoute() {
    int radius = CubitMod.CONFIG.burningWanderRadiusBlocks();
    BlockPos current = robot.blockPosition();
    for (int attempt = 0; attempt < WANDER_ATTEMPTS; attempt++) {
      BlockPos target =
          current.offset(randomOffset(radius), randomOffset(WANDER_HEIGHT), randomOffset(radius));
      Path route = routeTo(target);
      if (route != null && route.getNodeCount() > 1) {
        return route;
      }
    }
    return null;
  }

  /** Checks the chunk first: planning a route must never load one (AGENTS.md, rule 22). */
  private Path routeTo(BlockPos target) {
    if (!robot.level().hasChunkAt(target)) {
      return null;
    }
    return robot.getNavigation().createPath(target, ROUTE_PRECISION_BLOCKS);
  }

  private int randomOffset(int range) {
    return robot.getRandom().nextIntBetweenInclusive(-range, range);
  }
}
