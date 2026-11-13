package io.github.marcelofabianov.cubit.network;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

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
  private static Consumer<FocusMarkPayload> focusMarkDisplay = mark -> {};
  private static Consumer<RobotListPayload> robotListDisplay = list -> {};
  private static Consumer<ChosenRobotPayload> chosenRobotDisplay = chosen -> {};
  private static BiConsumer<ServerPlayer, UUID> robotChooser = (player, robot) -> {};

  private CubitNetwork() {}

  public static void register(RegisterPayloadHandlersEvent event) {
    PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
    registrar
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
            (payload, context) -> mobMarksDisplay.accept(payload))
        .playToClient(
            FocusMarkPayload.TYPE,
            FocusMarkPayload.STREAM_CODEC,
            (payload, context) -> focusMarkDisplay.accept(payload));
    registerTablet(registrar);
  }

  /** The list and the chosen Cubit go to the client, and only the choice comes back. */
  private static void registerTablet(PayloadRegistrar registrar) {
    registrar
        .playToClient(
            RobotListPayload.TYPE,
            RobotListPayload.STREAM_CODEC,
            (payload, context) -> robotListDisplay.accept(payload))
        .playToClient(
            ChosenRobotPayload.TYPE,
            ChosenRobotPayload.STREAM_CODEC,
            (payload, context) -> chosenRobotDisplay.accept(payload))
        .playToServer(
            ChooseRobotPayload.TYPE,
            ChooseRobotPayload.STREAM_CODEC,
            (payload, context) -> {
              if (context.player() instanceof ServerPlayer player) {
                robotChooser.accept(player, payload.robot());
              }
            });
  }

  /** On the client only: how the list and the chosen Cubit of the Tablet show. */
  public static void setTabletDisplays(
      Consumer<RobotListPayload> list, Consumer<ChosenRobotPayload> chosen) {
    robotListDisplay = list;
    chosenRobotDisplay = chosen;
  }

  /** On the server: who handles the choice, so the network never depends on the player code. */
  public static void setRobotChooser(BiConsumer<ServerPlayer, UUID> chooser) {
    robotChooser = chooser;
  }

  public static void sendRobotList(ServerPlayer player, RobotListPayload list) {
    send(player, list);
  }

  public static void sendChosenRobot(ServerPlayer player, ChosenRobotPayload chosen) {
    send(player, chosen);
  }

  /** On the client: asks the server to choose a Cubit. */
  public static void chooseRobot(UUID robot) {
    PacketDistributor.sendToServer(new ChooseRobotPayload(robot));
  }

  public static void setNoticeDisplay(Consumer<Component> display) {
    noticeDisplay = display;
  }

  public static void setMarksDisplay(
      Consumer<OreMarksPayload> ores, Consumer<MobMarksPayload> mobs) {
    oreMarksDisplay = ores;
    mobMarksDisplay = mobs;
  }

  public static void setFocusMarkDisplay(Consumer<FocusMarkPayload> display) {
    focusMarkDisplay = display;
  }

  public static void sendFocusMark(ServerPlayer player, FocusMarkPayload mark) {
    send(player, mark);
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
