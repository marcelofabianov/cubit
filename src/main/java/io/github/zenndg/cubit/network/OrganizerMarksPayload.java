package io.github.zenndg.cubit.network;

import io.github.zenndg.cubit.CubitMod;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * The machines and destinations the Organizers of the player marked, for the arrows of the Mark
 * app: the block, the name of the Cubit and the number of the mark, in its order (answer 1297). An
 * empty name is a Cubit without a name, which the client names in its own language.
 */
public record OrganizerMarksPayload(List<Mark> marks) implements CustomPacketPayload {
  private static final String PATH = "organizer_marks";

  /** One mark. */
  public record Mark(BlockPos pos, String cubit, int number) {
    static final StreamCodec<RegistryFriendlyByteBuf, Mark> CODEC =
        StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            Mark::pos,
            ByteBufCodecs.STRING_UTF8,
            Mark::cubit,
            ByteBufCodecs.VAR_INT,
            Mark::number,
            Mark::new);
  }

  public static final OrganizerMarksPayload NONE = new OrganizerMarksPayload(List.of());

  public static final Type<OrganizerMarksPayload> TYPE =
      new Type<>(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, PATH));
  public static final StreamCodec<RegistryFriendlyByteBuf, OrganizerMarksPayload> STREAM_CODEC =
      StreamCodec.composite(
          Mark.CODEC.apply(ByteBufCodecs.list()),
          OrganizerMarksPayload::marks,
          OrganizerMarksPayload::new);

  public OrganizerMarksPayload {
    marks = List.copyOf(marks);
  }

  @Override
  public Type<OrganizerMarksPayload> type() {
    return TYPE;
  }
}
