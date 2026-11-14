package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.AreaCheck;
import io.github.marcelofabianov.cubit.domain.robot.RobotName;
import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import io.github.marcelofabianov.cubit.domain.tier.AreaSpec;
import io.github.marcelofabianov.cubit.menu.RobotScreenOpening;
import io.github.marcelofabianov.cubit.menu.TabletOpening;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.payload.AdvancedOpenScreenPayload;

/**
 * What the Tablet does to a Cubit of the player on the server: open its screen and rename it, at
 * any distance, only for the owner and only while the Cubit is loaded (answers 1015 and 1018).
 */
public final class RobotTablet {
  private RobotTablet() {}

  /** The loaded Cubit of the player with this id, in any dimension. */
  public static Optional<RobotEntity> find(ServerPlayer player, UUID robot) {
    for (ServerLevel level : player.server.getAllLevels()) {
      if (level.getEntity(robot) instanceof RobotEntity found && canUse(found, player)) {
        return Optional.of(found);
      }
    }
    return Optional.empty();
  }

  /** The screen stays open while the Cubit lives and stays loaded, not by the distance. */
  static boolean canUse(RobotEntity robot, Player player) {
    return robot.isAlive() && Ownership.owns(robot.ownerId(), player.getUUID());
  }

  /**
   * A connection without the channel of the screens with data, as the player of the tests, would
   * throw, so the choice stays without the screen.
   */
  public static void open(RobotEntity robot, ServerPlayer player) {
    if (!player.connection.hasChannel(AdvancedOpenScreenPayload.TYPE)) {
      return;
    }
    RobotScreenOpening opening = RobotScreenReadout.opening(robot);
    TabletOpening tablet = opening(robot);
    player.openMenu(
        new SimpleMenuProvider(
            (containerId, inventory, opener) ->
                RobotScreenReadout.tabletMenu(containerId, inventory, robot),
            robot.getDisplayName()),
        buffer -> {
          opening.write(buffer);
          tablet.write(buffer);
        });
  }

  static TabletOpening opening(RobotEntity robot) {
    return new TabletOpening(robot.getUUID(), robot.level().dimension().location().toString());
  }

  /** The largest area of the tier of the Cubit, from the config (answer 1050). */
  public static AreaSpec areaOf(RobotEntity robot) {
    return robot.spec().area();
  }

  /** Whether the Cubit can take the area: its tier, its dimension and its reach (answer 1058). */
  public static AreaCheck checkArea(RobotEntity robot, WorkArea area) {
    AreaCheck.Place place =
        new AreaCheck.Place(
            robot.level().dimension().location().toString(),
            robot.getX(),
            robot.getZ(),
            robot.dockCallRadius());
    return AreaCheck.of(area, areaOf(robot), place);
  }

  /**
   * Names the Cubit as the name tag does, without spending the tag. An empty name removes it, and
   * an active Cubit reacts with the happy face (answers 1015 to 1017).
   */
  public static boolean rename(ServerPlayer player, UUID robot, String typed) {
    Optional<RobotEntity> found = find(player, robot);
    Optional<RobotName> name = RobotName.typed(typed);
    if (found.isEmpty() || name.isEmpty()) {
      return false;
    }
    RobotEntity named = found.get();
    named.setCustomName(name.get().removes() ? null : Component.literal(name.get().value()));
    RobotPaint.react(named);
    return true;
  }
}
