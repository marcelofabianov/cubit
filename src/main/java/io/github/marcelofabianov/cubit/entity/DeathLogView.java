package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.DeathGuideCall;
import io.github.marcelofabianov.cubit.domain.behavior.DeathLog;
import io.github.marcelofabianov.cubit.domain.behavior.DeathRecord;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.network.DeathLogPayload;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Sends the Deaths tab its rows: the log of the owner and, for each death, what the guide button of
 * the chosen Cubit can do with it (answers 1111, 1117 and 1120).
 */
public final class DeathLogView {
  private static final String UNREADABLE_CAUSE = "?";

  private DeathLogView() {}

  public static void send(ServerPlayer owner) {
    DeathLog log = owner.getData(CubitAttachments.DEATH_LOG);
    Optional<RobotDeathGuide> guide = chosenGuide(owner);
    List<DeathLogPayload.Row> rows =
        log.deaths().stream().map(death -> row(owner, death, guide)).toList();
    long guiding =
        guide
            .map(each -> each.guiding().orElse(DeathLogPayload.NOT_GUIDING))
            .orElse(DeathLogPayload.NOT_GUIDING);
    int guideTier =
        guide
            .flatMap(RobotDeathGuide::guideTier)
            .map(RobotTier::ordinal)
            .orElse(DeathLogPayload.NO_GUIDE_TIER);
    CubitNetwork.sendDeathLog(owner, new DeathLogPayload(rows, guiding, guideTier));
  }

  /** The chosen Cubit, only while it is loaded, as every button of the Tablet reads it. */
  public static Optional<RobotDeathGuide> chosenGuide(ServerPlayer owner) {
    return owner
        .getData(CubitAttachments.CHOSEN_ROBOT)
        .robot()
        .flatMap(robot -> RobotTablet.find(owner, robot))
        .map(robot -> robot.docking().deathGuide());
  }

  private static DeathLogPayload.Row row(
      ServerPlayer owner, DeathRecord death, Optional<RobotDeathGuide> guide) {
    DeathGuideCall call =
        guide.map(each -> each.callFor(death)).orElse(DeathGuideCall.NOT_FOLLOWING);
    return new DeathLogPayload.Row(
        death.dayTime(),
        new BlockPos(death.x(), death.y(), death.z()),
        death.dimension(),
        cause(owner, death),
        call);
  }

  /** The cause as the game said it. A cause that cannot be read never breaks the tab (rule 17). */
  private static Component cause(ServerPlayer owner, DeathRecord death) {
    try {
      Component read = Component.Serializer.fromJson(death.cause(), owner.registryAccess());
      return read == null ? Component.literal(UNREADABLE_CAUSE) : read;
    } catch (RuntimeException unreadable) {
      return Component.literal(UNREADABLE_CAUSE);
    }
  }
}
