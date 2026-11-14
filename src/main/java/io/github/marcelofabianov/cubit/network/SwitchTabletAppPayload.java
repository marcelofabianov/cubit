package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Shift and the wheel with the Tablet in hand: the direction of the wheel. The server picks the
 * next app that makes sense for the chosen Cubit (answers 1006 and 1044).
 */
public record SwitchTabletAppPayload(int direction) implements CustomPacketPayload {
  private static final String PATH = "switch_tablet_app";

  public static final Type<SwitchTabletAppPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, SwitchTabletAppPayload> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.VAR_INT, SwitchTabletAppPayload::direction, SwitchTabletAppPayload::new);

  @Override
  public Type<SwitchTabletAppPayload> type() {
    return TYPE;
  }
}
