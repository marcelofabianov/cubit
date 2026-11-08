package io.github.marcelofabianov.cubit.entity;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

/** The navigation of a robot, where stopping really stops. */
final class RobotNavigation extends GroundPathNavigation {
  RobotNavigation(Mob mob, Level level) {
    super(mob, level);
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
