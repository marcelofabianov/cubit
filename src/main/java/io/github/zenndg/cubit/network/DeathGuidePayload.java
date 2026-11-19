package io.github.zenndg.cubit.network;

import io.github.zenndg.cubit.CubitMod;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The guide button of a row of the Deaths tab: guide to that death, or cancel. The row goes with
 * the time of its death, so a stale screen never sends the Cubit to another one. The server checks
 * the owner, the chosen Cubit and the tier (answers 1117 and 1120 and rule 20).
 */
public record DeathGuidePayload(UUID robot, int row, long dayTime, boolean start)
    implements CustomPacketPayload {
  private static final String PATH = "death_guide";

  public static final Type<DeathGuidePayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, DeathGuidePayload> STREAM_CODEC =
      StreamCodec.composite(
          UUIDUtil.STREAM_CODEC,
          DeathGuidePayload::robot,
          ByteBufCodecs.VAR_INT,
          DeathGuidePayload::row,
          ByteBufCodecs.VAR_LONG,
          DeathGuidePayload::dayTime,
          ByteBufCodecs.BOOL,
          DeathGuidePayload::start,
          DeathGuidePayload::new);

  @Override
  public Type<DeathGuidePayload> type() {
    return TYPE;
  }
}
