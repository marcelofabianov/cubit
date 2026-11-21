package io.github.zenndg.cubit.registry;

import com.mojang.serialization.Codec;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.DeathLog;
import io.github.zenndg.cubit.domain.energy.RechargeSplashDose;
import io.github.zenndg.cubit.entity.RobotTickReading;
import io.github.zenndg.cubit.player.ChosenRobotLink;
import io.github.zenndg.cubit.player.FollowingRobotLink;
import io.github.zenndg.cubit.player.SavedDeathLog;
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
  private static final String DEATH_LOG_ID = "death_log";
  private static final String RECHARGE_SPLASH_ID = "recharge_splash";
  private static final String DESK_SEEN_ID = "engineering_desk_seen";
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

  // The deaths of the player, kept through death, since a death is recorded as it happens (1114).
  public static final Supplier<AttachmentType<DeathLog>> DEATH_LOG =
      ATTACHMENTS.register(
          DEATH_LOG_ID,
          () ->
              AttachmentType.builder(() -> DeathLog.EMPTY)
                  .serialize(SavedDeathLog.CODEC)
                  .copyOnDeath()
                  .build());

  // Without a serializer: never saved nor sent, only read by the robot screen (answer 698).
  public static final Supplier<AttachmentType<RobotTickReading>> ROBOT_TICK_READING =
      ATTACHMENTS.register(
          ROBOT_TICK_READING_ID, () -> AttachmentType.builder(RobotTickReading::new).build());

  // Without a serializer: what a Recharge Splash still has to give is lost on unload (1417).
  public static final Supplier<AttachmentType<RechargeSplashDose>> RECHARGE_SPLASH =
      ATTACHMENTS.register(
          RECHARGE_SPLASH_ID, () -> AttachmentType.builder(() -> RechargeSplashDose.NONE).build());

  // Whether the player already placed a desk, so its first text shows once (answer 1492).
  public static final Supplier<AttachmentType<Boolean>> DESK_SEEN =
      ATTACHMENTS.register(
          DESK_SEEN_ID,
          () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL).copyOnDeath().build());

  private CubitAttachments() {}

  public static void register(IEventBus modBus) {
    ATTACHMENTS.register(modBus);
  }
}
