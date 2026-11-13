package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PickupFilterTest {
  private static final String DIRT_ID = "minecraft:dirt";
  private static final String STONE_ID = "minecraft:stone";
  private static final String OAK_ID = "minecraft:oak_log";
  private static final String BIRCH_ID = "minecraft:birch_log";
  private static final String LOGS_TAG = "minecraft:logs";
  private static final String BURNABLE_TAG = "minecraft:logs_that_burn";
  private static final PickupEntry DIRT = PickupEntry.item(DIRT_ID);
  private static final PickupEntry STONE = PickupEntry.item(STONE_ID);
  private static final PickupEntry OAK = PickupEntry.item(OAK_ID);
  private static final PickupEntry LOGS = PickupEntry.tag(LOGS_TAG);
  private static final PickupEntry BURNABLE = PickupEntry.tag(BURNABLE_TAG);
  private static final PickupItem DIRT_ITEM = new PickupItem(DIRT_ID, Set.of());
  private static final PickupItem STONE_ITEM = new PickupItem(STONE_ID, Set.of());
  private static final PickupItem BIRCH_ITEM =
      new PickupItem(BIRCH_ID, Set.of(LOGS_TAG, BURNABLE_TAG));
  private static final List<String> OAK_TAGS = List.of(LOGS_TAG, BURNABLE_TAG);
  private static final int ONE_SLOT = 1;
  private static final int THREE_SLOTS = 3;
  private static final int FIRST = 0;
  private static final int SECOND = 1;
  private static final int FAR_SLOT = 5;
  private static final PickupSpec BASE = new PickupSpec(0, Energy.ZERO, THREE_SLOTS, false, 0);
  private static final PickupSpec ELITE = new PickupSpec(5, new Energy(30), THREE_SLOTS, true, 0);
  private static final PickupSpec SINGLE = new PickupSpec(0, Energy.ZERO, ONE_SLOT, false, 0);

  @Test
  void anEmptyBlacklistTakesAnything() {
    assertEquals(PickupMode.BLACKLIST, PickupFilter.EMPTY.mode());
    assertTrue(PickupFilter.EMPTY.allows(DIRT_ITEM, BASE));
  }

  @Test
  void anEmptyWhitelistTakesNothing() {
    assertFalse(PickupFilter.EMPTY.toggledMode().allows(DIRT_ITEM, BASE));
  }

  @Test
  void theBlacklistLeavesTheListedItems() {
    PickupFilter filter = PickupFilter.EMPTY.placed(FIRST, DIRT, BASE);

    assertFalse(filter.allows(DIRT_ITEM, BASE));
    assertTrue(filter.allows(STONE_ITEM, BASE));
  }

  @Test
  void theWhitelistTakesOnlyTheListedItems() {
    PickupFilter filter = PickupFilter.EMPTY.toggledMode().placed(FIRST, DIRT, BASE);

    assertTrue(filter.allows(DIRT_ITEM, BASE));
    assertFalse(filter.allows(STONE_ITEM, BASE));
  }

  @Test
  void togglingTheModeTwiceGoesBack() {
    assertEquals(PickupFilter.EMPTY, PickupFilter.EMPTY.toggledMode().toggledMode());
  }

  @Test
  void placingOnAFilledSlotReplacesIt() {
    PickupFilter filter = PickupFilter.EMPTY.placed(FIRST, DIRT, BASE).placed(FIRST, STONE, BASE);

    assertEquals(List.of(STONE), filter.entries());
  }

  @Test
  void placingAfterTheLastEntryAppends() {
    PickupFilter filter =
        PickupFilter.EMPTY.placed(FIRST, DIRT, BASE).placed(FAR_SLOT, STONE, BASE);

    assertEquals(List.of(DIRT, STONE), filter.entries());
  }

  @Test
  void aFullFilterIgnoresAnotherEntry() {
    PickupFilter full = PickupFilter.EMPTY.placed(FIRST, DIRT, SINGLE);

    assertEquals(full, full.placed(SECOND, STONE, SINGLE));
  }

  @Test
  void theSameEntryCountsOnce() {
    PickupFilter filter = PickupFilter.EMPTY.placed(FIRST, DIRT, BASE).placed(SECOND, DIRT, BASE);

    assertEquals(List.of(DIRT), filter.entries());
  }

  @Test
  void clearingRemovesTheEntryAndShiftsTheRest() {
    PickupFilter filter =
        PickupFilter.EMPTY.placed(FIRST, DIRT, BASE).placed(SECOND, STONE, BASE).cleared(FIRST);

    assertEquals(List.of(STONE), filter.entries());
    assertEquals(filter, filter.cleared(FAR_SLOT));
  }

  @Test
  void aTagMatchesEveryItemInIt() {
    PickupFilter filter = PickupFilter.EMPTY.toggledMode().placed(FIRST, LOGS, ELITE);

    assertTrue(filter.allows(BIRCH_ITEM, ELITE));
  }

  @Test
  void aTierWithoutTagsRefusesAndIgnoresThem() {
    PickupFilter refused = PickupFilter.EMPTY.placed(FIRST, LOGS, BASE);
    PickupFilter saved = new PickupFilter(PickupMode.WHITELIST, List.of(LOGS));

    assertEquals(PickupFilter.EMPTY, refused);
    assertFalse(saved.allows(BIRCH_ITEM, BASE));
    assertEquals(List.of(), saved.effective(BASE));
  }

  @Test
  void entriesAboveTheLimitAreKeptButIgnored() {
    PickupFilter saved = new PickupFilter(PickupMode.BLACKLIST, List.of(DIRT, STONE));

    assertEquals(List.of(DIRT), saved.effective(SINGLE));
    assertTrue(saved.allows(STONE_ITEM, SINGLE));
    assertEquals(List.of(DIRT, STONE), saved.entries());
  }

  @Test
  void cyclingGoesFromTheItemThroughItsTagsAndBack() {
    PickupFilter item = PickupFilter.EMPTY.placed(FIRST, OAK, ELITE);
    PickupFilter firstTag = item.cycled(FIRST, OAK_ID, OAK_TAGS, ELITE);
    PickupFilter secondTag = firstTag.cycled(FIRST, OAK_ID, OAK_TAGS, ELITE);

    assertEquals(List.of(LOGS), firstTag.entries());
    assertEquals(List.of(BURNABLE), secondTag.entries());
    assertEquals(item, secondTag.cycled(FIRST, OAK_ID, OAK_TAGS, ELITE));
  }

  @Test
  void cyclingDoesNothingWithoutTagsOrOnAnEmptySlot() {
    PickupFilter item = PickupFilter.EMPTY.placed(FIRST, OAK, BASE);

    assertEquals(item, item.cycled(FIRST, OAK_ID, OAK_TAGS, BASE));
    assertEquals(item, item.cycled(SECOND, OAK_ID, OAK_TAGS, ELITE));
    assertEquals(item, item.cycled(FIRST, OAK_ID, List.of(), ELITE));
  }
}
