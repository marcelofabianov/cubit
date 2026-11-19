package io.github.zenndg.cubit.network;

import io.github.zenndg.cubit.CubitMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The delete button of a row of the Deaths tab. The row goes with the time of its death, so a stale
 * screen never removes another one. The server only changes the log of the player who sent it
 * (answer 1111 and rule 20).
 */
public record DeleteDeathPayload(int row, long dayTime) implements CustomPacketPayload {
  private static final String PATH = "delete_death";

  public static final Type<DeleteDeathPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, DeleteDeathPayload> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT,
          DeleteDeathPayload::row,
          ByteBufCodecs.VAR_LONG,
          DeleteDeathPayload::dayTime,
          DeleteDeathPayload::new);

  @Override
  public Type<DeleteDeathPayload> type() {
    return TYPE;
  }
}
