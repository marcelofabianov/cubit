package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.RobotChatter;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;

/** The robot congratulates its owner. Recipes are advancements too, but the player sees nothing. */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class OwnerAchievement {
  private OwnerAchievement() {}

  @SubscribeEvent
  public static void onAchievement(AdvancementEvent.AdvancementEarnEvent event) {
    if (event.getEntity() instanceof ServerPlayer owner) {
      event
          .getAdvancement()
          .value()
          .display()
          .filter(DisplayInfo::shouldShowToast)
          .ifPresent(display ->
              RobotChatter.congratulate(owner, display.getType() == AdvancementType.CHALLENGE));
    }
  }
}
