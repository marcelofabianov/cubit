package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.RobotTickReading;
import io.github.marcelofabianov.cubit.player.ChosenRobotLink;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import java.util.function.Supplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * Player and robot data. The IDs of the saved ones are part of the saves, so they cannot change
 * without a migration.
 */
public final class CubitAttachments {
  private static final String FOLLOWING_ROBOT_ID = "following_robot";
  private static final String ROBOT_TICK_READING_ID = "robot_tick_reading";
  private static final String CHOSEN_ROBOT_ID = "chosen_robot";
  private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
      DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, CubitMod.MOD_ID);

  // Copied on death because the robot keeps following the player after a respawn.
  public static final Supplier<AttachmentType<FollowingRobotLink>> FOLLOWING_ROBOT =
      ATTACHMENTS.register(
          FOLLOWING_ROBOT_ID,
          () ->
              AttachmentType.builder(() -> FollowingRobotLink.NOBODY)
                  .serialize(FollowingRobotLink.CODEC)
                  .copyOnDeath()
                  .build());

  // The Cubit chosen in the Tablet, in the player and not the item, so any Tablet uses it (1009).
  public static final Supplier<AttachmentType<ChosenRobotLink>> CHOSEN_ROBOT =
      ATTACHMENTS.register(
          CHOSEN_ROBOT_ID,
          () ->
              AttachmentType.builder(() -> ChosenRobotLink.NOBODY)
                  .serialize(ChosenRobotLink.CODEC)
                  .copyOnDeath()
                  .build());

  // Without a serializer: never saved nor sent, only read by the robot screen (answer 698).
  public static final Supplier<AttachmentType<RobotTickReading>> ROBOT_TICK_READING =
      ATTACHMENTS.register(
          ROBOT_TICK_READING_ID, () -> AttachmentType.builder(RobotTickReading::new).build());

  private CubitAttachments() {}

  public static void register(IEventBus modBus) {
    ATTACHMENTS.register(modBus);
  }
}
