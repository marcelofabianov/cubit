package io.github.zenndg.cubit.player;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.DeathLog;
import io.github.zenndg.cubit.domain.behavior.DeathRecord;
import io.github.zenndg.cubit.registry.CubitAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/**
 * Records every death of the player in the death log, with or without a Cubit (answers 1114 and
 * 1124). It listens last, so a death another mod cancels is never recorded.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class OwnerDeaths {
  private OwnerDeaths() {}

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onDeath(LivingDeathEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      record(player, player.getCombatTracker().getDeathMessage());
    }
  }

  /** The cause is the death message of the game, saved as text that keeps its translation. */
  public static void record(ServerPlayer player, Component cause) {
    BlockPos block = player.blockPosition();
    DeathRecord death =
        new DeathRecord(
            player.level().getDayTime(),
            block.getX(),
            block.getY(),
            block.getZ(),
            player.level().dimension().location().toString(),
            Component.Serializer.toJson(cause, player.registryAccess()));
    DeathLog log = player.getData(CubitAttachments.DEATH_LOG);
    player.setData(CubitAttachments.DEATH_LOG, log.record(death, CubitMod.CONFIG.deathLogSize()));
  }
}
