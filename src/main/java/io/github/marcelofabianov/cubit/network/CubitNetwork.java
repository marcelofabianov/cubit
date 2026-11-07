package io.github.marcelofabianov.cubit.network;

import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * The packets of the mod. The client registers what shows a notice and the boxes of the scanners,
 * so this common class never references the client package, which does not exist on a dedicated
 * server.
 */
public final class CubitNetwork {
  private static final String PROTOCOL_VERSION = "1";

  private static Consumer<Component> noticeDisplay = text -> {};
  private static Consumer<OreMarksPayload> oreMarksDisplay = marks -> {};
  private static Consumer<MobMarksPayload> mobMarksDisplay = marks -> {};

  private CubitNetwork() {}

  public static void register(RegisterPayloadHandlersEvent event) {
    event
        .registrar(PROTOCOL_VERSION)
        .playToClient(
            NoticePayload.TYPE,
            NoticePayload.STREAM_CODEC,
            (payload, context) -> noticeDisplay.accept(payload.text()))
        .playToClient(
            OreMarksPayload.TYPE,
            OreMarksPayload.STREAM_CODEC,
            (payload, context) -> oreMarksDisplay.accept(payload))
        .playToClient(
            MobMarksPayload.TYPE,
            MobMarksPayload.STREAM_CODEC,
            (payload, context) -> mobMarksDisplay.accept(payload));
  }

  public static void setNoticeDisplay(Consumer<Component> display) {
    noticeDisplay = display;
  }

  public static void setMarksDisplay(
      Consumer<OreMarksPayload> ores, Consumer<MobMarksPayload> mobs) {
    oreMarksDisplay = ores;
    mobMarksDisplay = mobs;
  }

  public static void sendNotice(ServerPlayer player, Component text) {
    send(player, new NoticePayload(text));
  }

  public static void sendOreMarks(ServerPlayer player, OreMarksPayload marks) {
    send(player, marks);
  }

  public static void sendMobMarks(ServerPlayer player, MobMarksPayload marks) {
    send(player, marks);
  }

  /** The game refuses a payload the connection did not agree on, instead of ignoring it. */
  private static void send(ServerPlayer player, CustomPacketPayload payload) {
    if (player.connection.hasChannel(payload.type())) {
      PacketDistributor.sendToPlayer(player, payload);
    }
  }
}
