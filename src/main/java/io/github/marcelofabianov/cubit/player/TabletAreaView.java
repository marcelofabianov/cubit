package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.domain.robot.AreaCorner;
import io.github.marcelofabianov.cubit.domain.robot.AreaMarking;
import io.github.marcelofabianov.cubit.domain.tier.AreaSpec;
import io.github.marcelofabianov.cubit.entity.RobotTablet;
import io.github.marcelofabianov.cubit.network.AreaMarkPayload;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

/**
 * Sends the owner what the Area app draws: the first corner with the limits of the Cubit being
 * marked, and the area of the chosen Cubit in the dimension of the player (answer 1056).
 */
public final class TabletAreaView {
  private TabletAreaView() {}

  public static void sync(ServerPlayer player) {
    Optional<AreaMarking> marking = TabletAreaApp.marking(player);
    Optional<BlockPos> first = marking.flatMap(AreaMarking::first).map(TabletAreaView::pos);
    AreaMarkPayload.Limits limits =
        marking
            .flatMap(found -> RobotTablet.find(player, found.robot()))
            .map(RobotTablet::areaOf)
            .map(TabletAreaView::limits)
            .orElse(AreaMarkPayload.Limits.NONE);
    Optional<AreaMarkPayload.Box> area =
        TabletUse.chosen(player)
            .flatMap(robot -> robot.workArea().area())
            .filter(found -> found.isIn(TabletAreaApp.dimensionOf(player)))
            .map(found -> new AreaMarkPayload.Box(pos(found.min()), pos(found.max())));
    CubitNetwork.sendAreaMark(player, new AreaMarkPayload(first, limits, area));
  }

  private static AreaMarkPayload.Limits limits(AreaSpec spec) {
    return new AreaMarkPayload.Limits(spec.sizeBlocks(), spec.heightBlocks());
  }

  private static BlockPos pos(AreaCorner corner) {
    return new BlockPos(corner.x(), corner.y(), corner.z());
  }
}
