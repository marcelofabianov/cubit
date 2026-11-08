package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RespawnReturn;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;

/**
 * Brings the robot that follows to an owner who respawned. The chunk where the owner died is still
 * loaded at that moment, and a robot already unloaded stays there, because nothing is ever loaded
 * by force (AGENTS.md, rule 22). The robot goes to the spot the game chose for the owner.
 */
public final class RobotRespawn {
  private RobotRespawn() {}

  public static void bringTo(ServerPlayer owner) {
    MinecraftServer server = owner.getServer();
    if (server != null) {
      RobotEntity.follower(owner).ifPresent(robot -> bringTo(owner, server, robot));
    }
  }

  private static void bringTo(ServerPlayer owner, MinecraftServer server, UUID robotId) {
    for (ServerLevel level : server.getAllLevels()) {
      if (level.getEntity(robotId) instanceof RobotEntity robot) {
        bring(robot, owner);
        return;
      }
    }
  }

  private static void bring(RobotEntity robot, ServerPlayer owner) {
    boolean sameDimension = robot.level() == owner.level();
    boolean bringsBack =
        RespawnReturn.bringsBack(
            robot.powerState(),
            robot.docking().hasTrip(),
            sameDimension,
            sameDimension ? robot.distanceTo(owner) : 0,
            CubitMod.CONFIG.followDecision().teleportDistanceBlocks());
    if (!bringsBack) {
      return;
    }
    robot.getNavigation().stop();
    if (sameDimension) {
      robot.teleportTo(owner.getX(), owner.getY(), owner.getZ());
    } else {
      robot.changeDimension(transitionTo(robot, owner));
    }
  }

  private static DimensionTransition transitionTo(RobotEntity robot, ServerPlayer owner) {
    return new DimensionTransition(
        owner.serverLevel(),
        owner.position(),
        Vec3.ZERO,
        robot.getYRot(),
        robot.getXRot(),
        DimensionTransition.DO_NOTHING);
  }
}
