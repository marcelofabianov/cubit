package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.energy.RechargeSplashReception;
import io.github.zenndg.cubit.domain.energy.RechargeSplashSpec;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

/**
 * Where a Recharge Splash breaks: the splash for everyone, and the dose for the robots of the
 * thrower in the radius, checked on the server (answers 1402, 1408, 1409 and 1416).
 */
public final class RechargeSplashImpact {
  public static final String STILL_RECHARGING_KEY = "item.cubit.recharge_splash.still_recharging";
  private static final int SPLASH_COLOR = 0xD9262B;
  private static final DustParticleOptions RED_DROPS =
      new DustParticleOptions(new Vector3f(0.85f, 0.15f, 0.17f), 1f);
  private static final int DROPS = 24;
  private static final int SPARKS = 16;
  private static final double SPREAD = 0.6;
  private static final double SPEED = 0.15;

  private RechargeSplashImpact() {}

  static void at(ServerLevel level, Projectile splash) {
    showSplash(level, splash);
    Entity thrower = splash.getOwner();
    if (thrower == null) {
      return;
    }
    RechargeSplashSpec spec = CubitMod.CONFIG.rechargeSplash().spec();
    Vec3 impact = splash.position();
    int waitSeconds = 0;
    for (RobotEntity robot :
        level.getEntitiesOfClass(
            RobotEntity.class, splash.getBoundingBox().inflate(spec.radiusBlocks()))) {
      boolean inRadius = spec.reaches(robot.distanceToSqr(impact));
      RechargeSplashReception reception =
          RobotRechargeSplash.receive(robot, thrower.getUUID(), inRadius, spec);
      if (reception == RechargeSplashReception.STILL_RECHARGING) {
        waitSeconds = Math.max(waitSeconds, RobotRechargeSplash.secondsLeft(robot));
      }
    }
    if (waitSeconds > 0 && thrower instanceof Player player) {
      player.displayClientMessage(Component.translatable(STILL_RECHARGING_KEY, waitSeconds), true);
    }
  }

  /** The game's splash plays the glass and the drops, and the mod adds red dust and sparks. */
  private static void showSplash(ServerLevel level, Projectile splash) {
    level.levelEvent(
        LevelEvent.PARTICLES_SPELL_POTION_SPLASH, splash.blockPosition(), SPLASH_COLOR);
    level.sendParticles(
        RED_DROPS,
        splash.getX(),
        splash.getY(),
        splash.getZ(),
        DROPS,
        SPREAD,
        SPREAD,
        SPREAD,
        SPEED);
    level.sendParticles(
        ParticleTypes.ELECTRIC_SPARK,
        splash.getX(),
        splash.getY(),
        splash.getZ(),
        SPARKS,
        SPREAD,
        SPREAD,
        SPREAD,
        SPEED);
  }
}
