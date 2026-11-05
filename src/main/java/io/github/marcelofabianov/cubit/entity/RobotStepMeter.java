package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.energy.RobotActivity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.phys.Vec3;

/** Reads from the world what a robot did since the last tick, for the domain to price it. */
final class RobotStepMeter {
  private static final double MIN_STEP_SQUARED = 1.0E-4;

  private Vec3 last;

  RobotActivity measure(RobotEntity robot, boolean fullInventory) {
    Vec3 position = robot.position();
    boolean moved = last != null && last.distanceToSqr(position) > MIN_STEP_SQUARED;
    double rose = last == null ? 0 : position.y - last.y;
    last = position;
    boolean ownRoute = !robot.getNavigation().isDone() || robot.getMoveControl().hasWanted();
    boolean walking = ownRoute && moved;
    double climbed = walking && robot.onGround() ? Math.max(rose, 0) : 0;
    return new RobotActivity(
        walking, fullInventory, robot.isInWater(), hasNegativeCondition(robot), climbed);
  }

  /** Goes by the category of the effect, so the effects of other mods count without a list. */
  private static boolean hasNegativeCondition(RobotEntity robot) {
    if (robot.isOnFire() || robot.isInLava()) {
      return true;
    }
    for (MobEffectInstance effect : robot.getActiveEffects()) {
      if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
        return true;
      }
    }
    return false;
  }
}
