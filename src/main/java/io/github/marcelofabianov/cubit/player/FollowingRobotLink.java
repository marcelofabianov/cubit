package io.github.marcelofabianov.cubit.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;

/** Which robot follows the player. It is stored in the player data, so it survives a restart. */
public record FollowingRobotLink(Optional<UUID> robot) {
  public static final FollowingRobotLink NOBODY = new FollowingRobotLink(Optional.empty());

  private static final String ROBOT_FIELD = "robot";

  public static final Codec<FollowingRobotLink> CODEC =
      RecordCodecBuilder.create(instance ->
          instance
              .group(
                  UUIDUtil.CODEC.optionalFieldOf(ROBOT_FIELD).forGetter(FollowingRobotLink::robot))
              .apply(instance, FollowingRobotLink::new));
}
