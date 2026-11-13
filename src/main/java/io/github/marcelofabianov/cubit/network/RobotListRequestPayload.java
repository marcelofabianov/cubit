package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Back on the screen of a Cubit: the client asks for the list of the selection again. */
public record RobotListRequestPayload() implements CustomPacketPayload {
  private static final String PATH = "robot_list_request";

  public static final RobotListRequestPayload INSTANCE = new RobotListRequestPayload();
  public static final Type<RobotListRequestPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, RobotListRequestPayload> STREAM_CODEC =
      StreamCodec.unit(INSTANCE);

  @Override
  public Type<RobotListRequestPayload> type() {
    return TYPE;
  }
}
