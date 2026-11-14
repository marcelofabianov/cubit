package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * What the client of the owner draws with the Area app: the first corner being marked, with the
 * limits of the Cubit, and the area of the chosen Cubit (answer 1056). The server sends it on each
 * change, never on every tick.
 */
public record AreaMarkPayload(Optional<BlockPos> first, Limits limits, Optional<Box> area)
    implements CustomPacketPayload {
  private static final String PATH = "area_mark";

  /** The base and the height the tier of the Cubit being marked allows. */
  public record Limits(int size, int height) {
    public static final Limits NONE = new Limits(0, 0);
    static final StreamCodec<RegistryFriendlyByteBuf, Limits> CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            Limits::size,
            ByteBufCodecs.VAR_INT,
            Limits::height,
            Limits::new);
  }

  /** The blocks of an area, from its lower corner to its upper one. */
  public record Box(BlockPos min, BlockPos max) {
    static final StreamCodec<RegistryFriendlyByteBuf, Box> CODEC =
        StreamCodec.composite(
            BlockPos.STREAM_CODEC, Box::min, BlockPos.STREAM_CODEC, Box::max, Box::new);
  }

  public static final AreaMarkPayload NOTHING =
      new AreaMarkPayload(Optional.empty(), Limits.NONE, Optional.empty());

  public static final Type<AreaMarkPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, AreaMarkPayload> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.optional(BlockPos.STREAM_CODEC),
          AreaMarkPayload::first,
          Limits.CODEC,
          AreaMarkPayload::limits,
          ByteBufCodecs.optional(Box.CODEC),
          AreaMarkPayload::area,
          AreaMarkPayload::new);

  @Override
  public Type<AreaMarkPayload> type() {
    return TYPE;
  }
}
