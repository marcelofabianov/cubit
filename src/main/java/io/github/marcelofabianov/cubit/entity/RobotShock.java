package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.tier.ShockSpec;
import io.github.marcelofabianov.cubit.registry.CubitDamageTypes;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/** What a shock does in the world: energy, damage, sparks and sound (answers 826, 827 and 831). */
final class RobotShock {
  private static final double SPARK_STEP = 0.25;
  private static final int ONE_SPARK = 1;
  private static final double NO_SPREAD = 0;
  private static final double NO_SPEED = 0;

  private RobotShock() {}

  /** One cost for the whole shock, even when an area shock hits many targets. */
  static void strike(RobotEntity robot, List<LivingEntity> targets, ShockSpec spec) {
    robot.setBattery(robot.battery().consume(spec.cost()).battery());
    DamageSource source = CubitDamageTypes.shock(robot);
    Player owner = robot.onlineOwner();
    for (LivingEntity target : targets) {
      // Like a tamed wolf, so the mob drops what only a player's kill gives.
      if (owner != null) {
        target.setLastHurtByPlayer(owner);
      }
      target.hurt(source, spec.damage());
      sparks(robot, target);
    }
    robot.playSound(CubitSounds.SHOCK_ATTACK.get());
  }

  private static void sparks(RobotEntity robot, LivingEntity target) {
    if (!(robot.level() instanceof ServerLevel level)) {
      return;
    }
    Vec3 from = robot.getEyePosition();
    Vec3 to = target.getBoundingBox().getCenter();
    double length = from.distanceTo(to);
    Vec3 step = to.subtract(from).normalize().scale(SPARK_STEP);
    Vec3 point = from;
    for (double done = 0; done <= length; done += SPARK_STEP) {
      level.sendParticles(
          ParticleTypes.ELECTRIC_SPARK,
          point.x,
          point.y,
          point.z,
          ONE_SPARK,
          NO_SPREAD,
          NO_SPREAD,
          NO_SPREAD,
          NO_SPEED);
      point = point.add(step);
    }
  }
}
