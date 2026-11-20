package io.github.zenndg.cubit.player;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.ownership.Ownership;
import io.github.zenndg.cubit.domain.robot.UnloadChest;
import io.github.zenndg.cubit.entity.OrganizerTarget;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.entity.RobotUnload;
import io.github.zenndg.cubit.network.CubitNetwork;
import io.github.zenndg.cubit.network.OrganizerMarksPayload;
import io.github.zenndg.cubit.network.UnloadChestsPayload;
import io.github.zenndg.cubit.registry.CubitEntities;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Sends the marked chests of the Cubits of the player, with their slots, and the marks of its
 * Organizers, once a second while the player holds the Tablet, and clears them once when it leaves
 * the hand (answers 1090 to 1092 and 1297). Only loaded Cubits in the dimension of the player and
 * loaded chests count (rule 22).
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID)
public final class TabletChestView {
  private static final int SEND_TICKS = 20;
  private static final String NO_NAME = "";
  private static final Set<UUID> SHOWING = ConcurrentHashMap.newKeySet();

  private TabletChestView() {}

  @SubscribeEvent
  public static void onTick(PlayerTickEvent.Post event) {
    if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % SEND_TICKS != 0) {
      return;
    }
    if (holdsTablet(player)) {
      SHOWING.add(player.getUUID());
      send(player);
    } else if (SHOWING.remove(player.getUUID())) {
      CubitNetwork.sendUnloadChests(player, UnloadChestsPayload.NONE);
      CubitNetwork.sendOrganizerMarks(player, OrganizerMarksPayload.NONE);
    }
  }

  /** Also right after a click of the Unload app, so the arrow shows at once. */
  static void send(ServerPlayer player) {
    ServerLevel level = player.serverLevel();
    String dimension = level.dimension().location().toString();
    List<UnloadChestsPayload.Chest> chests = new ArrayList<>();
    List<? extends RobotEntity> robots =
        level.getEntities(
            CubitEntities.GENERAL_ROBOT.get(),
            robot -> Ownership.owns(robot.ownerId(), player.getUUID()));
    for (RobotEntity robot : robots) {
      String name = robot.hasCustomName() ? robot.getCustomName().getString() : NO_NAME;
      RobotUnload unload = robot.docking().unload();
      for (UnloadChest chest : unload.chests().chests()) {
        BlockPos pos = BlockPos.of(chest.block());
        Optional<RobotUnload.Slots> slots =
            chest.isIn(dimension) ? unload.slotsAt(pos) : Optional.empty();
        slots.ifPresent(found ->
            chests.add(new UnloadChestsPayload.Chest(pos, name, found.used(), found.total())));
      }
    }
    CubitNetwork.sendUnloadChests(player, new UnloadChestsPayload(chests));
    CubitNetwork.sendOrganizerMarks(player, new OrganizerMarksPayload(marks(robots, dimension)));
  }

  /** The marks of the Organizers in the dimension of the player, numbered in their order. */
  private static List<OrganizerMarksPayload.Mark> marks(
      List<? extends RobotEntity> robots, String dimension) {
    List<OrganizerMarksPayload.Mark> marks = new ArrayList<>();
    for (RobotEntity robot : robots) {
      String name = robot.hasCustomName() ? robot.getCustomName().getString() : NO_NAME;
      int number = 0;
      for (OrganizerTarget target : robot.workArea().work().marks().targets()) {
        number++;
        if (target.mark().isIn(dimension)) {
          marks.add(
              new OrganizerMarksPayload.Mark(BlockPos.of(target.mark().block()), name, number));
        }
      }
    }
    return marks;
  }

  private static boolean holdsTablet(ServerPlayer player) {
    return player.getMainHandItem().is(CubitItems.TABLET.get())
        || player.getOffhandItem().is(CubitItems.TABLET.get());
  }
}
