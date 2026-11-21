package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.network.CubitNetwork;
import io.github.zenndg.cubit.registry.CubitAttachments;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * The short text of the first desk a player places: choose a recipe in the catalog and feed the
 * desk with redstone or FE (answers 1479 and 1492). It shows only once per player.
 */
public final class DeskFirstNotice {
  public static final String KEY = "notice.cubit.engineering_desk_first";

  private DeskFirstNotice() {}

  static void show(Player player) {
    if (!(player instanceof ServerPlayer server) || server.getData(CubitAttachments.DESK_SEEN)) {
      return;
    }
    server.setData(CubitAttachments.DESK_SEEN, true);
    CubitNetwork.sendNotice(server, Component.translatable(KEY));
  }
}
