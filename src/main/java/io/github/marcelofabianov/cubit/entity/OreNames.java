package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

/**
 * The name of an ore kind in the language of the player: the name of the first block of its tag, as
 * the game translates it (answer 734). A tag without blocks keeps the name of the tag.
 */
public final class OreNames {
  private OreNames() {}

  public static Component of(OreKind kind) {
    ResourceLocation id = ResourceLocation.tryParse(kind.tag());
    if (id == null) {
      return Component.literal(kind.shownName());
    }
    return BuiltInRegistries.BLOCK
        .getTag(TagKey.create(Registries.BLOCK, id))
        .flatMap(blocks -> blocks.stream().findFirst())
        .<Component>map(block -> Component.translatable(block.value().getDescriptionId()))
        .orElseGet(() -> Component.literal(kind.shownName()));
  }
}
