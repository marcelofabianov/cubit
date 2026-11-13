package io.github.marcelofabianov.cubit.player;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;

/** The Cubit chosen in the Tablet, kept in the player data (answers 1009 and 1026). */
public record ChosenRobotLink(Optional<UUID> robot) {
  public static final ChosenRobotLink NOBODY = new ChosenRobotLink(Optional.empty());

  private static final String ROBOT_FIELD = "robot";

  public static final Codec<ChosenRobotLink> CODEC =
      RecordCodecBuilder.create(instance ->
          instance
              .group(UUIDUtil.CODEC.optionalFieldOf(ROBOT_FIELD).forGetter(ChosenRobotLink::robot))
              .apply(instance, ChosenRobotLink::new));
}
