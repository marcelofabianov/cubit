package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingRobotPolicy;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/** Frees the following link when a robot dies, now or as soon as the owner joins. */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class DeadRobotLinks {
  private DeadRobotLinks() {}

  /** Without this, the link would keep pointing to the dead robot and switch off the next one. */
  public static void onRobotDied(MinecraftServer server, UUID ownerId, UUID robotId) {
    Player owner = server.getPlayerList().getPlayer(ownerId);
    if (owner == null) {
      DeadRobots.of(server).add(ownerId, robotId);
      return;
    }
    release(owner, robotId);
  }

  public static void releaseFor(Player player, MinecraftServer server) {
    DeadRobots.of(server).takeRobotsOf(player.getUUID()).forEach(robot -> release(player, robot));
  }

  @SubscribeEvent
  public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    Player player = event.getEntity();
    MinecraftServer server = player.getServer();
    if (server != null) {
      releaseFor(player, server);
    }
  }

  private static void release(Player player, UUID robotId) {
    FollowingRobotPolicy policy = new FollowingRobotPolicy(CubitMod.CONFIG.powerRules());
    Optional<UUID> follower = player.getData(CubitAttachments.FOLLOWING_ROBOT).robot();
    player.setData(
        CubitAttachments.FOLLOWING_ROBOT,
        new FollowingRobotLink(policy.release(robotId, follower)));
  }
}
