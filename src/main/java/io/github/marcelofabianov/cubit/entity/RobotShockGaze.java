package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ShockGaze;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * Keeps the eyes of the robot on the target of its shock until it dies, flees or is gone, and then
 * on its last spot for a while (answers 991 to 995). The rules live in {@link ShockGaze}.
 */
final class RobotShockGaze {
  private final RobotEntity robot;
  private ShockGaze gaze = ShockGaze.EMPTY;
  private Vec3 lastSpot = Vec3.ZERO;

  RobotShockGaze(RobotEntity robot) {
    this.robot = robot;
  }

  /** The nearest target of the strike, which also replaces an older one. */
  void struck(LivingEntity target) {
    gaze = gaze.struck(target.getUUID());
    lastSpot = target.getEyePosition();
  }

  void tick() {
    gaze.target().ifPresent(this::follow);
  }

  /** The eyes of the target, or its last spot while the gaze lingers. */
  Optional<Vec3> spot() {
    boolean looking = gaze.target().isPresent() || gaze.lingering(robot.level().getGameTime());
    return looking ? Optional.of(lastSpot) : Optional.empty();
  }

  private void follow(UUID id) {
    Entity found = robot.level() instanceof ServerLevel level ? level.getEntity(id) : null;
    if (found instanceof LivingEntity target && keeps(target)) {
      lastSpot = target.getEyePosition();
      return;
    }
    gaze = gaze.lost(robot.level().getGameTime(), CubitMod.CONFIG.shockLookAfterTicks());
  }

  /** The distance counts from the owner, or from the robot while the owner is away (992). */
  private boolean keeps(LivingEntity target) {
    Player owner = robot.onlineOwner();
    Entity from = owner != null && owner.level() == robot.level() ? owner : robot;
    return ShockGaze.keeps(
        target.isAlive(),
        target.level() == robot.level(),
        from.distanceTo(target),
        CubitMod.CONFIG.focusForgetDistanceBlocks());
  }
}
