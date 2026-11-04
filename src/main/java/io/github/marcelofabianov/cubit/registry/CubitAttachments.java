package io.github.marcelofabianov.cubit.registry;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import java.util.function.Supplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Player data. The IDs are part of the saves, so they cannot change without a migration. */
public final class CubitAttachments {
  private static final String FOLLOWING_ROBOT_ID = "following_robot";
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

  private CubitAttachments() {}

  public static void register(IEventBus modBus) {
    ATTACHMENTS.register(modBus);
  }
}
