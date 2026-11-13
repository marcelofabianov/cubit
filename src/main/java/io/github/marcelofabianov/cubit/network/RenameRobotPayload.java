package io.github.marcelofabianov.cubit.network;

import io.github.marcelofabianov.cubit.CubitMod;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The name the player typed for a Cubit on the Tablet. The server checks the owner and the size of
 * the name before naming (answers 1015 and 1017 and rule 20).
 */
public record RenameRobotPayload(UUID robot, String name) implements CustomPacketPayload {
  private static final String PATH = "rename_robot";

  /** Room for spaces around the 50 characters, which the server cuts, and nothing more. */
  private static final int MAX_TYPED = 256;

  public static final Type<RenameRobotPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, RenameRobotPayload> STREAM_CODEC =
      StreamCodec.composite(
          UUIDUtil.STREAM_CODEC,
          RenameRobotPayload::robot,
          ByteBufCodecs.stringUtf8(MAX_TYPED),
          RenameRobotPayload::name,
          RenameRobotPayload::new);

  @Override
  public Type<RenameRobotPayload> type() {
    return TYPE;
  }
}
