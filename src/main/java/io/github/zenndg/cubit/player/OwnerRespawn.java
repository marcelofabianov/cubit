package io.github.zenndg.cubit.player;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.entity.RobotRespawn;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/** Calls the robot back when its owner respawns, and it tells where the owner fell (1111). */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class OwnerRespawn {
  private OwnerRespawn() {}

  /** Leaving the End is a portal, not a death, and a portal never takes the robot along. */
  @SubscribeEvent
  public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
    if (!event.isEndConquered() && event.getEntity() instanceof ServerPlayer owner) {
      RobotRespawn.bringTo(owner);
      RobotRespawn.tellDeathPoint(owner);
    }
  }
}
