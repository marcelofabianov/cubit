package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The player picked a Cubit on the selection screen. The client only asks, and the server checks
 * that the player owns it before choosing (rules 19 and 20).
 */
public record ChooseRobotPayload(UUID robot) implements CustomPacketPayload {
  private static final String PATH = "choose_robot";

  public static final Type<ChooseRobotPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, ChooseRobotPayload> STREAM_CODEC =
      StreamCodec.composite(
          UUIDUtil.STREAM_CODEC, ChooseRobotPayload::robot, ChooseRobotPayload::new);

  @Override
  public Type<ChooseRobotPayload> type() {
    return TYPE;
  }
}
