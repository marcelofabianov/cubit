package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.RobotListing;
import io.github.marcelofabianov.cubit.domain.robot.RobotListings;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotSnapshotReader;
import io.github.marcelofabianov.cubit.entity.RobotTablet;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.network.ChosenRobotPayload;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.network.RobotListPayload;
import io.github.marcelofabianov.cubit.network.TabletRequests;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * The selection screen of the Tablet on the server: which Cubits it lists and which one the player
 * chooses (answers 1003, 1004 and 1009). Every choice from the client is checked here (rule 20).
 */
public final class TabletSelection {

  private TabletSelection() {}

  /** What the server does with each request of the Tablet, every one checked again here. */
  public static TabletRequests requests() {
    return new TabletRequests(
        TabletSelection::chooseAndOpen,
        TabletSelection::open,
        (player, payload) -> RobotTablet.rename(player, payload.robot(), payload.name()));
  }

  public static void open(ServerPlayer player) {
    CubitNetwork.sendRobotList(player, new RobotListPayload(entriesFor(player)));
  }

  /** The loaded Cubits are read live, and the others come from their last snapshot. */
  public static List<RobotListPayload.Entry> entriesFor(ServerPlayer player) {
    Map<UUID, Listed> listed = new HashMap<>();
    for (ServerLevel level : player.server.getAllLevels()) {
      for (RobotEntity robot :
          level.getEntities(CubitEntities.GENERAL_ROBOT.get(), robot -> owns(player, robot))) {
        RobotSnapshotReader.of(robot)
            .ifPresent(snapshot ->
                listed.put(
                    snapshot.robot(),
                    new Listed(snapshot, true, RobotSnapshotReader.following(robot))));
      }
    }
    for (RobotSnapshot snapshot : RobotSnapshots.of(player.server).of(player.getUUID())) {
      listed.putIfAbsent(snapshot.robot(), new Listed(snapshot, false, false));
    }
    List<RobotListing> order = new ArrayList<>();
    listed.values().forEach(each -> order.add(each.listing(player)));
    return RobotListings.ordered(order).stream()
        .map(listing -> listed.get(listing.id()).entry(player))
        .toList();
  }

  /** Only a Cubit of the player, loaded or in a snapshot, can be chosen. */
  public static void choose(ServerPlayer player, UUID robot) {
    entriesFor(player).stream()
        .filter(entry -> entry.robot().equals(robot))
        .findFirst()
        .ifPresent(entry -> {
          player.setData(CubitAttachments.CHOSEN_ROBOT, new ChosenRobotLink(Optional.of(robot)));
          sendChosen(player);
          Component name =
              entry.name().isEmpty()
                  ? Component.translatable(CubitEntities.GENERAL_ROBOT.get().getDescriptionId())
                  : Component.literal(entry.name());
          player.displayClientMessage(Component.translatable(TabletKeys.CHOSEN, name), true);
        });
  }

  /**
   * The choice opens the screen of the Cubit when it is loaded. An unloaded one shows its snapshot,
   * which the client builds from the list (answer 1034).
   */
  public static void chooseAndOpen(ServerPlayer player, UUID robot) {
    choose(player, robot);
    RobotTablet.find(player, robot).ifPresent(found -> RobotTablet.open(found, player));
  }

  public static void sendChosen(ServerPlayer player) {
    CubitNetwork.sendChosenRobot(
        player, new ChosenRobotPayload(player.getData(CubitAttachments.CHOSEN_ROBOT).robot()));
  }

  /** The choice goes away with the Cubit, when it is picked up or destroyed (answer 1009). */
  static void forget(ServerPlayer player, UUID robot) {
    if (player.getData(CubitAttachments.CHOSEN_ROBOT).robot().filter(robot::equals).isPresent()) {
      player.setData(CubitAttachments.CHOSEN_ROBOT, ChosenRobotLink.NOBODY);
      sendChosen(player);
    }
  }

  private static boolean owns(ServerPlayer player, RobotEntity robot) {
    return Ownership.owns(robot.ownerId(), player.getUUID());
  }

  /** A Cubit to list, with what only a loaded one can tell. */
  private record Listed(RobotSnapshot snapshot, boolean loaded, boolean following) {
    boolean sameDimension(ServerPlayer player) {
      return snapshot.dimension().equals(player.level().dimension());
    }

    double distance(ServerPlayer player) {
      return sameDimension(player) ? Math.sqrt(player.blockPosition().distSqr(snapshot.pos())) : 0;
    }

    RobotListing listing(ServerPlayer player) {
      return new RobotListing(
          snapshot.robot(),
          snapshot.name(),
          following,
          sameDimension(player),
          loaded,
          distance(player));
    }

    RobotListPayload.Entry entry(ServerPlayer player) {
      long age = loaded ? 0 : Math.max(player.level().getGameTime() - snapshot.gameTime(), 0);
      return new RobotListPayload.Entry(
          snapshot.robot(),
          snapshot.name(),
          new RobotListPayload.Look(
              snapshot.look().tier().ordinal(),
              snapshot.look().color().ordinal(),
              snapshot.look().expression().ordinal()),
          snapshot.status().ordinal(),
          snapshot.batteryPercent(),
          snapshot.pos(),
          snapshot.dimension().location().toString(),
          new RobotListPayload.Where(loaded, following, distance(player), age));
    }
  }
}
