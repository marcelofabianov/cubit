package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.ShockCandidate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

/**
 * Tells the domain what a living thing near the robot is. A neutral mob is checked before an enemy,
 * because some, like the zombified piglin, are both and stay calm until provoked.
 */
final class ShockCandidates {
  private ShockCandidates() {}

  static ShockCandidate of(RobotEntity robot, LivingEntity entity) {
    if (robot.ownerId().filter(entity.getUUID()::equals).isPresent()) {
      return ShockCandidate.OWNER;
    }
    if (entity instanceof Player) {
      return ShockCandidate.PLAYER;
    }
    if (entity instanceof RobotEntity) {
      return ShockCandidate.ROBOT;
    }
    if (entity instanceof NeutralMob) {
      return ShockCandidate.NEUTRAL_MOB;
    }
    return entity instanceof Enemy ? ShockCandidate.HOSTILE_MOB : ShockCandidate.PEACEFUL_MOB;
  }

  /** Turned against the owner: chasing the owner, or angry at the owner. */
  static boolean angryAtOwner(Player owner, LivingEntity entity) {
    if (owner == null) {
      return false;
    }
    boolean chasing = entity instanceof Mob mob && mob.getTarget() == owner;
    return chasing || entity instanceof NeutralMob neutral && neutral.isAngryAt(owner);
  }
}
