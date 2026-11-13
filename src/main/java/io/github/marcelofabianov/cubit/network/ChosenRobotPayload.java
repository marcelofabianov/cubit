package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** The Cubit chosen in the Tablet, for the arrow the client draws over it (answer 1010). */
public record ChosenRobotPayload(Optional<UUID> robot) implements CustomPacketPayload {
  private static final String PATH = "chosen_robot";

  public static final Type<ChosenRobotPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, ChosenRobotPayload> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC),
          ChosenRobotPayload::robot,
          ChosenRobotPayload::new);

  @Override
  public Type<ChosenRobotPayload> type() {
    return TYPE;
  }
}
