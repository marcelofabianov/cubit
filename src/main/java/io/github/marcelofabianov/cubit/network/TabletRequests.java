package io.github.marcelofabianov.cubit.network;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;

/**
 * Who handles on the server what the Tablet asks: the choice of a Cubit, the list again and the
 * name. The network never depends on the player code, which checks every request (rule 20).
 */
public record TabletRequests(
    BiConsumer<ServerPlayer, UUID> choose,
    Consumer<ServerPlayer> list,
    BiConsumer<ServerPlayer, RenameRobotPayload> rename) {
  static final TabletRequests NONE =
      new TabletRequests((player, robot) -> {}, player -> {}, (player, name) -> {});
}
