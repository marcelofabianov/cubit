package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import org.junit.jupiter.api.Test;

class FreshFindsTest {
  private static final String ZOMBIE = "zombie";
  private static final String SKELETON = "skeleton";

  @Test
  void whatWasNeverSeenIsFresh() {
    assertEquals(Set.of(ZOMBIE), FreshFinds.<String>none().seen(Set.of(ZOMBIE)).fresh());
  }

  @Test
  void whatStaysInRangeIsNotFreshAgain() {
    FreshFinds<String> known = FreshFinds.<String>none().seen(Set.of(ZOMBIE)).known();

    assertEquals(Set.of(), known.seen(Set.of(ZOMBIE)).fresh());
  }

  @Test
  void onlyTheNewOneIsFresh() {
    FreshFinds<String> known = FreshFinds.<String>none().seen(Set.of(ZOMBIE)).known();

    assertEquals(Set.of(SKELETON), known.seen(Set.of(ZOMBIE, SKELETON)).fresh());
  }

  @Test
  void whatLeftAndCameBackIsFreshAgain() {
    FreshFinds<String> known = FreshFinds.<String>none().seen(Set.of(ZOMBIE)).known();
    FreshFinds<String> gone = known.seen(Set.of()).known();

    assertEquals(Set.of(ZOMBIE), gone.seen(Set.of(ZOMBIE)).fresh());
  }
}
