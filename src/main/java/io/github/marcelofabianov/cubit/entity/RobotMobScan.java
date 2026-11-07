package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.FreshFinds;
import io.github.marcelofabianov.cubit.domain.behavior.ScanArea;
import io.github.marcelofabianov.cubit.domain.behavior.ScanRules;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

/** Finds the hostile mobs around a robot. Nothing here is saved, because it is recalculated. */
final class RobotMobScan {
  private final RobotEntity robot;
  private FreshFinds<UUID> known = FreshFinds.none();
  private List<Integer> inRange = List.of();

  RobotMobScan(RobotEntity robot) {
    this.robot = robot;
  }

  boolean enemyInRange() {
    return !inRange.isEmpty();
  }

  /** The network IDs of the hostile mobs in range at the last scan, to mark each one. */
  List<Integer> inRange() {
    return inRange;
  }

  /** The closest mob that was not in range before, if any. */
  Optional<Mob> scan(Player owner, int radius) {
    ScanArea area = new ScanArea(radius);
    List<Mob> mobs =
        robot
            .level()
            .getEntitiesOfClass(
                Mob.class,
                robot.getBoundingBox().inflate(radius),
                mob ->
                    mob.isAlive()
                        && area.holdsDistance(robot.distanceToSqr(mob))
                        && isHostile(mob, owner));
    inRange = mobs.stream().map(Entity::getId).toList();
    FreshFinds.Update<UUID> update =
        known.seen(mobs.stream().map(Entity::getUUID).collect(Collectors.toSet()));
    known = update.known();
    Set<UUID> fresh = update.fresh();
    return mobs.stream()
        .filter(mob -> fresh.contains(mob.getUUID()))
        .min(Comparator.comparingDouble(robot::distanceToSqr));
  }

  void reset() {
    inRange = List.of();
    known = FreshFinds.none();
  }

  private static boolean isHostile(Mob mob, Player owner) {
    return ScanRules.isHostile(
        mob instanceof Enemy, mob instanceof NeutralMob && mob.getTarget() == owner);
  }
}
