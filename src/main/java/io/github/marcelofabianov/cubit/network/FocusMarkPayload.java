package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The target of Focus, for the yellow arrow over it (answers 851 and 853). Only from the server to
 * the owner, and the client only draws. A mob id of {@link #NO_MOB} clears the arrow.
 */
public record FocusMarkPayload(int robotId, int mobId) implements CustomPacketPayload {
  public static final int NO_MOB = -1;
  private static final String PATH = "focus_mark";

  public static final Type<FocusMarkPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, FocusMarkPayload> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT,
          FocusMarkPayload::robotId,
          ByteBufCodecs.VAR_INT,
          FocusMarkPayload::mobId,
          FocusMarkPayload::new);

  @Override
  public Type<FocusMarkPayload> type() {
    return TYPE;
  }
}
