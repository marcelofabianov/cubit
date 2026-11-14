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
 * The marked chests of the Cubits of the player, for the arrows over them: the block, the name of
 * the Cubit and the used and total slots of the chest (answers 1086 and 1090 to 1092). An empty
 * name is a Cubit without a name, which the client names in its own language.
 */
public record UnloadChestsPayload(List<Chest> chests) implements CustomPacketPayload {
  private static final String PATH = "unload_chests";

  /** One marked chest. */
  public record Chest(BlockPos pos, String cubit, int used, int total) {
    static final StreamCodec<RegistryFriendlyByteBuf, Chest> CODEC =
        StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            Chest::pos,
            ByteBufCodecs.STRING_UTF8,
            Chest::cubit,
            ByteBufCodecs.VAR_INT,
            Chest::used,
            ByteBufCodecs.VAR_INT,
            Chest::total,
            Chest::new);
  }

  public static final UnloadChestsPayload NONE = new UnloadChestsPayload(List.of());

  public static final Type<UnloadChestsPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, UnloadChestsPayload> STREAM_CODEC =
      StreamCodec.composite(
          Chest.CODEC.apply(ByteBufCodecs.list()),
          UnloadChestsPayload::chests,
          UnloadChestsPayload::new);

  public UnloadChestsPayload {
    chests = List.copyOf(chests);
  }

  @Override
  public Type<UnloadChestsPayload> type() {
    return TYPE;
  }
}
