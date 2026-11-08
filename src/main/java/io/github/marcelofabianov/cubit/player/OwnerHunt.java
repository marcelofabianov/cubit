package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.RobotChatter;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

/** The robot comments when its owner kills an animal, and only then. */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class OwnerHunt {
  private OwnerHunt() {}

  @SubscribeEvent
  public static void onDeath(LivingDeathEvent event) {
    if (event.getEntity() instanceof Animal animal
        && event.getSource().getEntity() instanceof ServerPlayer killer) {
      RobotChatter.onOwnerKilled(killer, animal);
    }
  }
}
