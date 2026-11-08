package io.github.marcelofabianov.cubit.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.component.ItemContainerContents;

/** What the energy slot holds as an item: the stack and the FE already taken from it. */
public record EnergySlotContents(ItemContainerContents item, long reserve) {
  private static final String ITEM_FIELD = "item";
  private static final String RESERVE_FIELD = "reserve";
  private static final long NO_RESERVE = 0;

  public static final EnergySlotContents EMPTY =
      new EnergySlotContents(ItemContainerContents.EMPTY, NO_RESERVE);

  public static final Codec<EnergySlotContents> CODEC =
      RecordCodecBuilder.create(instance ->
          instance
              .group(
                  ItemContainerContents.CODEC
                      .fieldOf(ITEM_FIELD)
                      .forGetter(EnergySlotContents::item),
                  Codec.LONG
                      .xmap(reserve -> SavedEnergy.of(reserve).amount(), reserve -> reserve)
                      .fieldOf(RESERVE_FIELD)
                      .forGetter(EnergySlotContents::reserve))
              .apply(instance, EnergySlotContents::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, EnergySlotContents> STREAM_CODEC =
      StreamCodec.composite(
          ItemContainerContents.STREAM_CODEC,
          EnergySlotContents::item,
          ByteBufCodecs.VAR_LONG,
          EnergySlotContents::reserve,
          EnergySlotContents::new);
}
