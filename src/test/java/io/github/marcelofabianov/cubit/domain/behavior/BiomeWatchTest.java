package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class BiomeWatchTest {
  private static final Optional<ChatTopic> FOREST = Optional.of(ChatTopic.BIOME_FOREST);
  private static final Optional<ChatTopic> DESERT = Optional.of(ChatTopic.BIOME_DESERT);
  private static final Optional<ChatTopic> NO_GROUP = Optional.empty();

  @Test
  void theFirstReadingSaysNothing() {
    assertEquals(NO_GROUP, BiomeWatch.UNKNOWN.next(FOREST).topic());
  }

  @Test
  void anotherGroupIsATopic() {
    BiomeWatch forest = BiomeWatch.UNKNOWN.next(FOREST).watch();

    assertEquals(DESERT, forest.next(DESERT).topic());
  }

  @Test
  void theSameGroupSaysNothing() {
    BiomeWatch forest = BiomeWatch.UNKNOWN.next(FOREST).watch();

    assertEquals(NO_GROUP, forest.next(FOREST).topic());
  }

  @Test
  void aBiomeWithoutAGroupIsNotAChange() {
    BiomeWatch forest = BiomeWatch.UNKNOWN.next(FOREST).watch();
    BiomeWatch river = forest.next(NO_GROUP).watch();

    assertEquals(NO_GROUP, forest.next(NO_GROUP).topic());
    assertEquals(NO_GROUP, river.next(FOREST).topic());
    assertEquals(DESERT, river.next(DESERT).topic());
  }

  @Test
  void theFirstGroupAfterAnUnknownBiomeSaysNothing() {
    BiomeWatch unknown = BiomeWatch.UNKNOWN.next(NO_GROUP).watch();

    assertEquals(NO_GROUP, unknown.next(FOREST).topic());
  }
}
