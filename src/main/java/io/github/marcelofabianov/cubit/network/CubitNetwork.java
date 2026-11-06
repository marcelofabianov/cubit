package io.github.marcelofabianov.cubit.network;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * The packets of the mod. The client registers what shows a notice, so this common class never
 * references the client package, which does not exist on a dedicated server.
 */
public final class CubitNetwork {
  private static final String PROTOCOL_VERSION = "1";

  private static Consumer<Component> noticeDisplay = text -> {};

  private CubitNetwork() {}

  public static void register(RegisterPayloadHandlersEvent event) {
    event
        .registrar(PROTOCOL_VERSION)
        .playToClient(
            NoticePayload.TYPE,
            NoticePayload.STREAM_CODEC,
            (payload, context) -> noticeDisplay.accept(payload.text()));
  }

  public static void setNoticeDisplay(Consumer<Component> display) {
    noticeDisplay = display;
  }

  /** The game refuses a payload the connection did not agree on, instead of ignoring it. */
  public static void sendNotice(ServerPlayer player, Component text) {
    if (player.connection.hasChannel(NoticePayload.TYPE)) {
      PacketDistributor.sendToPlayer(player, new NoticePayload(text));
    }
  }
}
