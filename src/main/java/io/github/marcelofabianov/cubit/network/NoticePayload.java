package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** A short text from the robot to its owner. It only goes from the server to the client. */
public record NoticePayload(Component text) implements CustomPacketPayload {
  private static final String PATH = "notice";

  public static final Type<NoticePayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, NoticePayload> STREAM_CODEC =
      ComponentSerialization.TRUSTED_STREAM_CODEC.map(NoticePayload::new, NoticePayload::text);

  @Override
  public Type<NoticePayload> type() {
    return TYPE;
  }
}
