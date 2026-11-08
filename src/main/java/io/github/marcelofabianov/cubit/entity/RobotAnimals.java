package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import java.util.Map;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;

/** What the robot has to say about each animal. Any other animal, of any mod, has its own topic. */
public final class RobotAnimals {
  private static final Map<EntityType<?>, ChatTopic> TOPICS =
      Map.of(
          EntityType.PIG, ChatTopic.ANIMAL_PIG,
          EntityType.COW, ChatTopic.ANIMAL_COW,
          EntityType.CHICKEN, ChatTopic.ANIMAL_CHICKEN,
          EntityType.SHEEP, ChatTopic.ANIMAL_SHEEP,
          EntityType.RABBIT, ChatTopic.ANIMAL_RABBIT);

  private RobotAnimals() {}

  public static ChatTopic topicOf(Animal animal) {
    return TOPICS.getOrDefault(animal.getType(), ChatTopic.ANIMAL_OTHER);
  }
}
