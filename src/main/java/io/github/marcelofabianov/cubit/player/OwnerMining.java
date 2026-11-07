package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.RobotChatter;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.BlockEvent;

/** What the owner mines that the robot cares about. By the tag, so ores of other mods count too. */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class OwnerMining {
  private OwnerMining() {}

  @SubscribeEvent
  public static void onBreak(BlockEvent.BreakEvent event) {
    if (event.getPlayer() instanceof ServerPlayer owner
        && event.getState().is(Tags.Blocks.ORES_REDSTONE)) {
      RobotChatter.onOwnerMinedRedstone(owner);
    }
  }
}
