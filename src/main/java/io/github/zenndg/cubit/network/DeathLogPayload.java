package io.github.zenndg.cubit.network;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.DeathGuideCall;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The deaths of the owner for the Deaths tab, the newest first, each with whether the chosen Cubit
 * can guide there, the death it is guiding to now, if any, and the first tier that guides, by its
 * order (answers 1111, 1117 and 1119). It only goes from the server to the client.
 */
public record DeathLogPayload(List<Row> rows, long guiding, int guideTier)
    implements CustomPacketPayload {
  /** The time the guide sends while the Cubit guides to no death. */
  public static final long NOT_GUIDING = -1L;

  /** The tier sent when no tier guides, so the button says nothing about when it unlocks. */
  public static final int NO_GUIDE_TIER = -1;

  private static final String PATH = "death_log";

  /** The largest log the config allows. */
  private static final int MAX_ROWS = 50;

  private static final int MAX_DIMENSION = 256;

  /** One death and what the guide button can do with it. */
  public record Row(
      long dayTime, BlockPos block, String dimension, Component cause, DeathGuideCall call) {
    private static final StreamCodec<RegistryFriendlyByteBuf, DeathGuideCall> CALL_CODEC =
        ByteBufCodecs.VAR_INT.map(Row::callOf, DeathGuideCall::ordinal).cast();

    static final StreamCodec<RegistryFriendlyByteBuf, Row> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            Row::dayTime,
            BlockPos.STREAM_CODEC,
            Row::block,
            ByteBufCodecs.stringUtf8(MAX_DIMENSION),
            Row::dimension,
            ComponentSerialization.TRUSTED_STREAM_CODEC,
            Row::cause,
            CALL_CODEC,
            Row::call,
            Row::new);

    /** An unknown value can only be locked, which never sends the Cubit anywhere. */
    private static DeathGuideCall callOf(int ordinal) {
      DeathGuideCall[] calls = DeathGuideCall.values();
      return ordinal >= 0 && ordinal < calls.length ? calls[ordinal] : DeathGuideCall.LOCKED;
    }
  }

  public static final Type<DeathLogPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, DeathLogPayload> STREAM_CODEC =
      StreamCodec.composite(
          Row.STREAM_CODEC.apply(ByteBufCodecs.list(MAX_ROWS)),
          DeathLogPayload::rows,
          ByteBufCodecs.VAR_LONG,
          DeathLogPayload::guiding,
          ByteBufCodecs.VAR_INT,
          DeathLogPayload::guideTier,
          DeathLogPayload::new);

  @Override
  public Type<DeathLogPayload> type() {
    return TYPE;
  }
}
