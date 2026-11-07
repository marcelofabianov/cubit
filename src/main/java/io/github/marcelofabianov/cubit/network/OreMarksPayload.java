package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The new ores a scan found, for the owner to see a box around each. It only goes from the server
 * to the client, which only draws (answer 613).
 */
public record OreMarksPayload(List<BlockPos> positions, int holdTicks)
    implements CustomPacketPayload {
  private static final String PATH = "ore_marks";

  public static final Type<OreMarksPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, OreMarksPayload> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()),
          OreMarksPayload::positions,
          ByteBufCodecs.VAR_INT,
          OreMarksPayload::holdTicks,
          OreMarksPayload::new);

  public OreMarksPayload {
    positions = List.copyOf(positions);
  }

  @Override
  public Type<OreMarksPayload> type() {
    return TYPE;
  }
}
