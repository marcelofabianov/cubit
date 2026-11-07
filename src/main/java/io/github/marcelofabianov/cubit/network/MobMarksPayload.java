package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The hostile mobs in range of a robot at a scan. Each one replaces the last, so an empty list
 * clears the boxes. It only goes from the server to the client, which only draws (answer 613).
 */
public record MobMarksPayload(int robotId, int radius, List<Integer> mobIds, int holdTicks)
    implements CustomPacketPayload {
  private static final String PATH = "mob_marks";

  public static final Type<MobMarksPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, MobMarksPayload> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT,
          MobMarksPayload::robotId,
          ByteBufCodecs.VAR_INT,
          MobMarksPayload::radius,
          ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list()),
          MobMarksPayload::mobIds,
          ByteBufCodecs.VAR_INT,
          MobMarksPayload::holdTicks,
          MobMarksPayload::new);

  public MobMarksPayload {
    mobIds = List.copyOf(mobIds);
  }

  @Override
  public Type<MobMarksPayload> type() {
    return TYPE;
  }
}
