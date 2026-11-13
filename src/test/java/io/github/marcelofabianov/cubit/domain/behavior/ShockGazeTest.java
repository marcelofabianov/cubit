package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ShockGazeTest {
  private static final UUID MOB = new UUID(0, 1);
  private static final UUID OTHER = new UUID(0, 2);
  private static final long NOW = 1_000;
  private static final int AFTER = 60;
  private static final int FORGET = 32;
  private static final double NEAR = 10;
  private static final double FAR = 33;

  @Test
  void aStrikeFixesTheGazeOnTheTargetWithoutTime() {
    ShockGaze gaze = ShockGaze.EMPTY.struck(MOB);

    assertEquals(Optional.of(MOB), gaze.target());
    assertFalse(gaze.lingering(NOW));
  }

  @Test
  void aStrikeOnAnotherMobChangesTheTarget() {
    assertEquals(Optional.of(OTHER), ShockGaze.EMPTY.struck(MOB).struck(OTHER).target());
  }

  @Test
  void theGazeLingersOnTheLastSpotForTheTimeAfterTheTarget() {
    ShockGaze lost = ShockGaze.EMPTY.struck(MOB).lost(NOW, AFTER);

    assertEquals(Optional.empty(), lost.target());
    assertTrue(lost.lingering(NOW + AFTER - 1));
    assertFalse(lost.lingering(NOW + AFTER));
    assertFalse(ShockGaze.EMPTY.lingering(0));
  }

  @Test
  void theTargetIsKeptWhileItLivesNearTheOwnerInTheSameLevel() {
    assertTrue(ShockGaze.keeps(true, true, NEAR, FORGET));
    assertFalse(ShockGaze.keeps(false, true, NEAR, FORGET));
    assertFalse(ShockGaze.keeps(true, false, NEAR, FORGET));
    assertFalse(ShockGaze.keeps(true, true, FAR, FORGET));
  }

  @Test
  void theTimeAfterMustBePositive() {
    assertThrows(IllegalArgumentException.class, () -> ShockGaze.EMPTY.lost(NOW, 0));
  }
}
