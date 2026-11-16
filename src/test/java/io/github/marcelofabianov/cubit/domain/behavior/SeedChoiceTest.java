package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class SeedChoiceTest {
  private static final String WHEAT = "minecraft:wheat_seeds";
  private static final String CARROT = "minecraft:carrot";
  private static final String BEETROOT = "minecraft:beetroot_seeds";
  private static final int FEW = 3;
  private static final int MANY = 20;
  private static final Map<String, Integer> BOTH = Map.of(WHEAT, FEW, CARROT, MANY);

  /** The seed of a neighbor keeps the bed together (answer 1236). */
  @Test
  void theSeedOfANeighborComesFirst() {
    assertEquals(Optional.of(WHEAT), SeedChoice.pick(BOTH, List.of(WHEAT)));
  }

  @Test
  void withoutANeighborItIsTheSeedItHasTheMostOf() {
    assertEquals(Optional.of(CARROT), SeedChoice.pick(BOTH, List.of()));
  }

  @Test
  void withoutTheSeedOfTheNeighborItTakesAnother() {
    assertEquals(Optional.of(CARROT), SeedChoice.pick(BOTH, List.of(BEETROOT)));
  }

  @Test
  void withoutSeedsThereIsNone() {
    assertEquals(Optional.empty(), SeedChoice.pick(Map.of(WHEAT, 0), List.of(WHEAT)));
  }
}
