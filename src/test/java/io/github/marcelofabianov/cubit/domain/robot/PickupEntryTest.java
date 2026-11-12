package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class PickupEntryTest {
  private static final String DIRT_ID = "minecraft:dirt";
  private static final String LOGS_TAG = "minecraft:logs";
  private static final String LOGS_KEY = "#minecraft:logs";
  private static final String BLANK = " ";
  private static final String ONLY_MARK = "#";

  @Test
  void anItemKeepsItsIdAsTheKey() {
    assertEquals(DIRT_ID, PickupEntry.item(DIRT_ID).key());
    assertEquals(Optional.of(PickupEntry.item(DIRT_ID)), PickupEntry.fromKey(DIRT_ID));
  }

  @Test
  void aTagIsSavedWithTheMark() {
    assertEquals(LOGS_KEY, PickupEntry.tag(LOGS_TAG).key());
    assertEquals(Optional.of(PickupEntry.tag(LOGS_TAG)), PickupEntry.fromKey(LOGS_KEY));
  }

  @Test
  void anEmptyKeyIsNotAnEntry() {
    assertTrue(PickupEntry.fromKey(BLANK).isEmpty());
    assertTrue(PickupEntry.fromKey(ONLY_MARK).isEmpty());
    assertThrows(IllegalArgumentException.class, () -> PickupEntry.item(BLANK));
  }
}
