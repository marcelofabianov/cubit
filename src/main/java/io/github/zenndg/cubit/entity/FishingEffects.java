package io.github.zenndg.cubit.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

/** The splash and the sound of the game at the point of the water, with each fish (1198). */
final class FishingEffects {
  private static final int SPLASH_COUNT = 12;
  private static final double SPREAD = 0.3;
  private static final double SURFACE = 0.5;
  private static final double SPEED = 0.05;
  private static final float VOLUME = 1;
  private static final float PITCH = 1;

  private FishingEffects() {}

  static void splash(ServerLevel level, Vec3 water) {
    Vec3 surface = water.add(0, SURFACE, 0);
    particles(level, ParticleTypes.SPLASH, surface);
    particles(level, ParticleTypes.FISHING, surface);
    level.playSound(
        null,
        surface.x,
        surface.y,
        surface.z,
        SoundEvents.FISHING_BOBBER_RETRIEVE,
        SoundSource.NEUTRAL,
        VOLUME,
        PITCH);
  }

  private static void particles(ServerLevel level, SimpleParticleType type, Vec3 at) {
    level.sendParticles(type, at.x, at.y, at.z, SPLASH_COUNT, SPREAD, 0, SPREAD, SPEED);
  }
}
