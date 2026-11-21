package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.RechargeSplashDose;
import io.github.zenndg.cubit.domain.energy.RechargeSplashReception;
import io.github.zenndg.cubit.domain.energy.RechargeSplashSpec;
import io.github.zenndg.cubit.domain.tier.TierSpec;
import io.github.zenndg.cubit.registry.CubitAttachments;
import io.github.zenndg.cubit.registry.CubitSounds;
import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

/**
 * The Recharge Splash a robot is receiving, kept in an attachment without a serializer, so what is
 * left is lost when the robot is stored, dies or unloads (answer 1417).
 */
final class RobotRechargeSplash {
  private static final float VOLUME = 1f;
  private static final float PITCH = 1f;
  private static final int SPARKS = 12;
  private static final double SPARK_SPREAD = 0.3;
  private static final double SPARK_SPEED = 0.1;

  private RobotRechargeSplash() {}

  /** Gives this tick's part. The battery never goes past full, and what does not fit is lost. */
  static Battery tick(RobotEntity robot, Battery battery) {
    RechargeSplashDose dose = robot.getData(CubitAttachments.RECHARGE_SPLASH);
    if (!dose.isActive()) {
      return battery;
    }
    RechargeSplashDose.Step step = dose.next();
    robot.setData(CubitAttachments.RECHARGE_SPLASH, step.rest());
    robot.heal((float) step.health());
    return battery.recharge(step.energy()).battery();
  }

  static RechargeSplashReception receive(
      RobotEntity robot, UUID thrower, boolean inRadius, RechargeSplashSpec spec) {
    RechargeSplashDose current = robot.getData(CubitAttachments.RECHARGE_SPLASH);
    RechargeSplashReception reception =
        RechargeSplashReception.of(robot.ownerId(), thrower, inRadius && robot.isAlive(), current);
    if (reception == RechargeSplashReception.STARTS) {
      TierSpec tier = robot.spec();
      robot.setData(
          CubitAttachments.RECHARGE_SPLASH, spec.doseFor(tier.batteryCapacity(), tier.maxHealth()));
      showStart(robot);
    }
    return reception;
  }

  static int secondsLeft(RobotEntity robot) {
    return robot.getData(CubitAttachments.RECHARGE_SPLASH).secondsLeft();
  }

  /** The default happy face, as after an interaction (answer 1421). */
  private static void showStart(RobotEntity robot) {
    robot.face().onInteraction();
    robot.playSound(CubitSounds.RECHARGING.get(), VOLUME, PITCH);
    if (robot.level() instanceof ServerLevel level) {
      level.sendParticles(
          ParticleTypes.ELECTRIC_SPARK,
          robot.getX(),
          robot.getY(1),
          robot.getZ(),
          SPARKS,
          SPARK_SPREAD,
          SPARK_SPREAD,
          SPARK_SPREAD,
          SPARK_SPEED);
    }
  }
}
