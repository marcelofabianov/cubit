package io.github.zenndg.cubit.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.zenndg.cubit.domain.desk.DeskOrder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

/**
 * The recipe and the order of a desk, kept in its item as `cubit:engineering_desk_order` (answer
 * 1492). A recipe a pack removed is dropped when the desk reads it (rule 17).
 */
public record SavedDeskOrder(ResourceLocation recipe, int total, int done) {
  private static final String RECIPE_FIELD = "recipe";
  private static final String TOTAL_FIELD = "total";
  private static final String DONE_FIELD = "done";

  public static final Codec<SavedDeskOrder> CODEC =
      RecordCodecBuilder.create(instance ->
          instance
              .group(
                  ResourceLocation.CODEC.fieldOf(RECIPE_FIELD).forGetter(SavedDeskOrder::recipe),
                  Codec.INT.fieldOf(TOTAL_FIELD).forGetter(SavedDeskOrder::total),
                  Codec.INT.fieldOf(DONE_FIELD).forGetter(SavedDeskOrder::done))
              .apply(instance, SavedDeskOrder::new));

  public static final StreamCodec<ByteBuf, SavedDeskOrder> STREAM_CODEC =
      StreamCodec.composite(
          ResourceLocation.STREAM_CODEC,
          SavedDeskOrder::recipe,
          ByteBufCodecs.VAR_INT,
          SavedDeskOrder::total,
          ByteBufCodecs.VAR_INT,
          SavedDeskOrder::done,
          SavedDeskOrder::new);

  /** An invalid saved order, from an edited save, becomes no order. */
  public DeskOrder order() {
    boolean valid = total >= 0 && done >= 0 && done <= total;
    return valid ? new DeskOrder(total, done) : DeskOrder.NONE;
  }
}
