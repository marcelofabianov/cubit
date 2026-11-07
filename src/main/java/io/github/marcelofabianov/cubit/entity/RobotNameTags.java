package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * The game applies a name tag before asking the robot, and to anyone who clicks. This event comes
 * first, so it is the only place that can keep the robot of someone else from being renamed.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class RobotNameTags {
  private RobotNameTags() {}

  @SubscribeEvent
  public static void onInteract(PlayerInteractEvent.EntityInteract event) {
    if (event.getLevel().isClientSide()
        || !(event.getTarget() instanceof RobotEntity robot)
        || !event.getItemStack().is(Items.NAME_TAG)
        || !event.getItemStack().has(DataComponents.CUSTOM_NAME)) {
      return;
    }
    if (Ownership.owns(robot.ownerId(), event.getEntity().getUUID())) {
      RobotPaint.react(robot);
    } else {
      event.setCancellationResult(InteractionResult.FAIL);
      event.setCanceled(true);
    }
  }
}
