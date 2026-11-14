package io.github.marcelofabianov.cubit.domain.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import org.junit.jupiter.api.Test;

class UnloadCallTest {
  private static final boolean EMPTY = true;
  private static final boolean ITEMS = false;
  private static final boolean ROOM = true;
  private static final boolean NO_ROOM = false;

  @Test
  void anActiveCubitWithItemsAndAChestWithRoomCanUnload() {
    assertTrue(UnloadCall.of(PowerState.ACTIVE, ITEMS, ROOM).ready());
  }

  @Test
  void theRefusalSaysWhy() {
    assertEquals(UnloadCall.NO_CHEST, UnloadCall.of(PowerState.ACTIVE, ITEMS, NO_ROOM));
    assertEquals(UnloadCall.EMPTY, UnloadCall.of(PowerState.ACTIVE, EMPTY, ROOM));
    assertEquals(UnloadCall.HIBERNATING, UnloadCall.of(PowerState.HIBERNATING, ITEMS, ROOM));
    assertEquals(
        UnloadCall.SWITCHED_OFF, UnloadCall.of(PowerState.INACTIVE_SWITCHED_OFF, ITEMS, ROOM));
  }

  /** Only a full Cubit working in an area, with the switch on (answers 1087 and 1093). */
  @Test
  void byItselfOnlyAFullCubitInAnAreaUnloads() {
    assertTrue(UnloadCall.unloadsByItself(true, true, true, false));
    assertFalse(UnloadCall.unloadsByItself(false, true, true, false));
    assertFalse(UnloadCall.unloadsByItself(true, false, true, false));
    assertFalse(UnloadCall.unloadsByItself(true, true, false, false));
    assertFalse(UnloadCall.unloadsByItself(true, true, true, true));
  }
}
