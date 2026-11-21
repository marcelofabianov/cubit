package io.github.zenndg.cubit.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;

/**
 * The navigation of a robot, where stopping really stops, and whose routes go through the doors its
 * owner marked (answer 1364).
 */
final class RobotNavigation extends GroundPathNavigation {
  RobotNavigation(Mob mob, Level level) {
    super(mob, level);
  }

  @Override
  protected PathFinder createPathFinder(int maxVisitedNodes) {
    nodeEvaluator = new RobotNodeEvaluator();
    nodeEvaluator.setCanPassDoors(true);
    return new PathFinder(nodeEvaluator, maxVisitedNodes);
  }

  /**
   * The game keeps a route update delayed by nearby block changes through a stop, and that update
   * would give a stopped robot a new route to its old target.
   */
  @Override
  public void stop() {
    super.stop();
    hasDelayedRecomputation = false;
  }
}
