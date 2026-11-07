package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class OreKindTest {
  private static final String DIAMOND_TAG = "c:ores/diamond";
  private static final String SCRAP_TAG = "c:ores/netherite_scrap";
  private static final String PARENT_TAG = "c:ores";
  private static final String GROUND_TAG = "c:ores_in_ground/stone";
  private static final String EMPTY_SUBTAG = "c:ores/";
  private static final String DIAMOND_NAME = "DIAMOND";
  private static final String SCRAP_NAME = "NETHERITE SCRAP";

  @Test
  void aSubtagIsNamedInCapitals() {
    assertEquals(Optional.of(DIAMOND_NAME), new OreKind(DIAMOND_TAG).name());
    assertEquals(Optional.of(SCRAP_NAME), new OreKind(SCRAP_TAG).name());
  }

  @Test
  void theParentTagHasNoName() {
    assertEquals(Optional.empty(), OreKind.ANY.name());
    assertEquals(PARENT_TAG, OreKind.ANY.tag());
  }

  @Test
  void rejectsWhatIsNotAnOreTag() {
    assertThrows(IllegalArgumentException.class, () -> new OreKind(GROUND_TAG));
    assertThrows(IllegalArgumentException.class, () -> new OreKind(EMPTY_SUBTAG));
  }

  @Test
  void parsingIgnoresWhatIsNotAnOreTag() {
    assertEquals(Optional.of(new OreKind(DIAMOND_TAG)), OreKind.parse(DIAMOND_TAG));
    assertEquals(Optional.empty(), OreKind.parse(GROUND_TAG));
  }

  @Test
  void aBlockCountsAsItsSubtagsAndNotAsTheParent() {
    assertEquals(
        List.of(new OreKind(DIAMOND_TAG)),
        OreKind.kindsOf(List.of(PARENT_TAG, DIAMOND_TAG, GROUND_TAG)));
  }

  @Test
  void aBlockWithOnlyTheParentCountsAsAnyOre() {
    assertEquals(List.of(OreKind.ANY), OreKind.kindsOf(List.of(GROUND_TAG, PARENT_TAG)));
  }

  @Test
  void aBlockWithoutOreTagsHasNoKind() {
    assertTrue(OreKind.kindsOf(List.of(GROUND_TAG)).isEmpty());
  }

  @Test
  void subtagsComeInAStableOrder() {
    assertEquals(
        List.of(new OreKind(DIAMOND_TAG), new OreKind(SCRAP_TAG)),
        OreKind.kindsOf(List.of(SCRAP_TAG, DIAMOND_TAG)));
  }
}
