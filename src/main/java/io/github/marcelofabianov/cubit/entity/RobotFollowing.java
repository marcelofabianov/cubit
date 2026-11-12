package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingRobotPolicy;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.world.entity.player.Player;

/** Which robot follows a player, read from the player, and the rule of the place to follow. */
final class RobotFollowing {
  private RobotFollowing() {}

  static FollowingRobotPolicy policy() {
    return new FollowingRobotPolicy(CubitMod.CONFIG.powerRules());
  }

  static Optional<UUID> follower(Player player) {
    return player.getData(CubitAttachments.FOLLOWING_ROBOT).robot();
  }

  /** The robot gives up the place to follow, when it is the one following (answer 858). */
  static void release(RobotEntity robot, Player owner) {
    owner.setData(
        CubitAttachments.FOLLOWING_ROBOT,
        new FollowingRobotLink(policy().release(robot.getUUID(), follower(owner))));
  }
}
