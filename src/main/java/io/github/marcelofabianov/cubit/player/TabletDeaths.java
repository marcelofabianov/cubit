package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.DeathLog;
import io.github.marcelofabianov.cubit.domain.behavior.DeathRecord;
import io.github.marcelofabianov.cubit.entity.DeathLogView;
import io.github.marcelofabianov.cubit.entity.RobotDeathGuide;
import io.github.marcelofabianov.cubit.network.DeathGuidePayload;
import io.github.marcelofabianov.cubit.network.DeleteDeathPayload;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;

/**
 * The buttons of the Deaths tab on the server. Each request is checked again here: only the log of
 * the player who sent it, only the chosen Cubit of that player and only a row that still holds the
 * same death (answers 1111, 1117 and 1120 and rule 20).
 */
public final class TabletDeaths {
  private TabletDeaths() {}

  public static void guide(ServerPlayer player, DeathGuidePayload payload) {
    Optional<RobotDeathGuide> guide = chosenGuide(player, payload);
    if (guide.isEmpty()) {
      return;
    }
    if (!payload.start()) {
      guide.get().cancel();
      DeathLogView.send(player);
      return;
    }
    Optional<DeathRecord> death = logOf(player).at(payload.row(), payload.dayTime());
    if (death.isPresent() && guide.get().start(death.get())) {
      DeathLogView.send(player);
    }
  }

  public static void delete(ServerPlayer player, DeleteDeathPayload payload) {
    DeathLog log = logOf(player);
    DeathLog next = log.remove(payload.row(), payload.dayTime());
    if (next != log) {
      player.setData(CubitAttachments.DEATH_LOG, next.fittedTo(CubitMod.CONFIG.deathLogSize()));
      DeathLogView.send(player);
    }
  }

  /** Only the Cubit chosen in the Tablet, loaded and of this player, which the request names. */
  private static Optional<RobotDeathGuide> chosenGuide(
      ServerPlayer player, DeathGuidePayload payload) {
    boolean named =
        player
            .getData(CubitAttachments.CHOSEN_ROBOT)
            .robot()
            .filter(payload.robot()::equals)
            .isPresent();
    return named ? DeathLogView.chosenGuide(player) : Optional.empty();
  }

  private static DeathLog logOf(ServerPlayer player) {
    return player.getData(CubitAttachments.DEATH_LOG);
  }
}
