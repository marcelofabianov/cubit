package io.github.zenndg.cubit.network;

import io.github.zenndg.cubit.CubitMod;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** The doors the player marked in its dimension, for the arrows of the Doors app (answer 1371). */
public record DoorMarksPayload(List<BlockPos> doors) implements CustomPacketPayload {
  private static final String PATH = "door_marks";

  public static final DoorMarksPayload NONE = new DoorMarksPayload(List.of());

  public static final Type<DoorMarksPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, DoorMarksPayload> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()),
          DoorMarksPayload::doors,
          DoorMarksPayload::new);

  public DoorMarksPayload {
    doors = List.copyOf(doors);
  }

  @Override
  public Type<DoorMarksPayload> type() {
    return TYPE;
  }
}
