package io.github.marcelofabianov.cubit.domain.behavior;

/** What the robot comments on, and how many phrases it has to choose from. */
public enum ChatTopic {
  DAWN(8),
  NICE_DAY(8),
  DUSK(8),
  RAIN(8),
  STORM(8),
  COLD(8),
  LEFT_BEHIND(16),
  BIOME_FOREST(4),
  BIOME_JUNGLE(4),
  BIOME_DESERT(4),
  BIOME_SWAMP(4),
  BIOME_OCEAN(4),
  BIOME_MOUNTAIN(4),
  BIOME_PLAINS(4),
  BIOME_TAIGA(4),
  BIOME_BADLANDS(4),
  BIOME_MUSHROOM(4),
  BIOME_NETHER(4),
  BIOME_END(4),
  ANIMAL_PIG(5),
  ANIMAL_COW(5),
  ANIMAL_CHICKEN(4),
  ANIMAL_SHEEP(4),
  ANIMAL_RABBIT(4),
  ANIMAL_OTHER(4),
  ACHIEVEMENT(8),
  CHALLENGE(6);

  private final int phrases;

  ChatTopic(int phrases) {
    this.phrases = phrases;
  }

  public int phrases() {
    return phrases;
  }

  public static ChatTopic ofAchievement(boolean challenge) {
    return challenge ? CHALLENGE : ACHIEVEMENT;
  }
}
