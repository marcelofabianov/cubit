package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotSnapshotReader;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * What the Tablet does with entities and what keeps its list current: the click on a Cubit or on
 * any other entity, the snapshot of an unloaded Cubit and the arrow after joining (answers 1004,
 * 1007 and 1009).
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class TabletEvents {
  private TabletEvents() {}

  /**
   * The Tablet comes before the entity, and its app decides: with Open, a click on an own Cubit
   * chooses it and opens its screen, and a click on any other entity, such as a villager, opens the
   * selection (answer 1007).
   */
  @SubscribeEvent
  public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
    if (!event.getItemStack().is(CubitItems.TABLET.get())) {
      return;
    }
    event.setCanceled(true);
    event.setCancellationResult(InteractionResult.SUCCESS);
    if (!(event.getEntity() instanceof ServerPlayer player)) {
      return;
    }
    TabletUse.click(player, event.getItemStack(), new TabletUse.Living(event.getTarget()));
  }

  /**
   * An armor stand and a few others answer here, before the event above, so the entity is kept from
   * acting. The result is a pass: a success would end the click on the client, which would then
   * never send the click on the entity, and the Tablet would never see it.
   */
  @SubscribeEvent
  public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
    if (event.getItemStack().is(CubitItems.TABLET.get())) {
      event.setCanceled(true);
      event.setCancellationResult(InteractionResult.PASS);
    }
  }

  /**
   * An unloaded Cubit leaves its snapshot. A destroyed one, picked up or dead, leaves the list and
   * the choice of its owner. A change of dimension keeps both.
   */
  @SubscribeEvent
  public static void onLeave(EntityLeaveLevelEvent event) {
    if (!(event.getEntity() instanceof RobotEntity robot)
        || !(event.getLevel() instanceof ServerLevel level)
        || robot.getRemovalReason() == null) {
      return;
    }
    RobotSnapshots snapshots = RobotSnapshots.of(level.getServer());
    if (!robot.getRemovalReason().shouldDestroy()) {
      if (robot.getRemovalReason() != Entity.RemovalReason.CHANGED_DIMENSION) {
        RobotSnapshotReader.of(robot).ifPresent(snapshots::keep);
      }
      return;
    }
    snapshots.forget(robot.getUUID());
    robot
        .ownerId()
        .map(owner -> level.getServer().getPlayerList().getPlayer(owner))
        .ifPresent(owner -> TabletSelection.forget(owner, robot.getUUID()));
  }

  /** A Cubit that loads again is read live, so its old snapshot goes away. */
  @SubscribeEvent
  public static void onJoin(EntityJoinLevelEvent event) {
    if (event.getEntity() instanceof RobotEntity robot
        && event.getLevel() instanceof ServerLevel level) {
      RobotSnapshots.of(level.getServer()).forget(robot.getUUID());
    }
  }

  @SubscribeEvent
  public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      TabletSelection.sendChosen(player);
      TabletAreaView.sync(player);
    }
  }

  /** A marking halfway lives only while the player plays (answer 1060). */
  @SubscribeEvent
  public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
    if (event.getEntity() instanceof ServerPlayer player) {
      TabletAreaApp.forget(player);
    }
  }
}
