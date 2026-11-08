package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

/**
 * The group of a biome, by the tags of NeoForge, so the biomes of other mods count. The order
 * matters, because a biome can have several tags. A snowy biome has the comments on the cold.
 */
public final class RobotBiomes {
  private static final List<Group> GROUPS =
      List.of(
          new Group(Tags.Biomes.IS_NETHER, ChatTopic.BIOME_NETHER),
          new Group(Tags.Biomes.IS_END, ChatTopic.BIOME_END),
          new Group(Tags.Biomes.IS_MUSHROOM, ChatTopic.BIOME_MUSHROOM),
          new Group(Tags.Biomes.IS_SWAMP, ChatTopic.BIOME_SWAMP),
          new Group(Tags.Biomes.IS_JUNGLE, ChatTopic.BIOME_JUNGLE),
          new Group(Tags.Biomes.IS_DESERT, ChatTopic.BIOME_DESERT),
          new Group(Tags.Biomes.IS_BADLANDS, ChatTopic.BIOME_BADLANDS),
          new Group(Tags.Biomes.IS_OCEAN, ChatTopic.BIOME_OCEAN),
          new Group(Tags.Biomes.IS_BEACH, ChatTopic.BIOME_OCEAN),
          new Group(Tags.Biomes.IS_MOUNTAIN, ChatTopic.BIOME_MOUNTAIN),
          new Group(Tags.Biomes.IS_TAIGA, ChatTopic.BIOME_TAIGA),
          new Group(Tags.Biomes.IS_FOREST, ChatTopic.BIOME_FOREST),
          new Group(Tags.Biomes.IS_SAVANNA, ChatTopic.BIOME_PLAINS),
          new Group(Tags.Biomes.IS_PLAINS, ChatTopic.BIOME_PLAINS));

  private RobotBiomes() {}

  public static Optional<ChatTopic> groupOf(Holder<Biome> biome) {
    if (biome.is(Tags.Biomes.IS_SNOWY)) {
      return Optional.empty();
    }
    return GROUPS.stream().filter(group -> biome.is(group.tag())).map(Group::topic).findFirst();
  }

  private record Group(TagKey<Biome> tag, ChatTopic topic) {}
}
