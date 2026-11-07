package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StateRecordsTest {
  private static final int NEGATIVE = -1;
  private static final float FULL_TILT = 8f;
  private static final float BEYOND_FULL_TILT = 8.5f;

  @Test
  void aNegativeTimeIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> new ExpressionTimer(NEGATIVE));
    assertThrows(IllegalArgumentException.class, () -> new NoticeRepeat(NEGATIVE));
    assertThrows(IllegalArgumentException.class, () -> new GazeTracker(GazeDirection.UP, NEGATIVE));
    assertThrows(IllegalArgumentException.class, () -> new OwnerGaze(GazeDirection.UP, NEGATIVE));
  }

  @Test
  void noTimeLeftIsAccepted() {
    assertDoesNotThrow(() -> new ExpressionTimer(0));
    assertDoesNotThrow(() -> new NoticeRepeat(0));
    assertDoesNotThrow(() -> new GazeTracker(GazeDirection.UP, 0));
    assertDoesNotThrow(() -> new OwnerGaze(GazeDirection.UP, 0));
  }

  @Test
  void aTiltBeyondTheFullLeanIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> new BodyTilt(0f, BEYOND_FULL_TILT));
    assertThrows(IllegalArgumentException.class, () -> new BodyTilt(0f, -BEYOND_FULL_TILT));
    assertThrows(IllegalArgumentException.class, () -> new BodyTilt(BEYOND_FULL_TILT, 0f));
    assertThrows(IllegalArgumentException.class, () -> new BodyTilt(-BEYOND_FULL_TILT, 0f));
  }

  @Test
  void aTiltThatIsNotANumberIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> new BodyTilt(0f, Float.NaN));
    assertThrows(IllegalArgumentException.class, () -> new BodyTilt(Float.NaN, 0f));
  }

  @Test
  void theFullLeanIsAccepted() {
    assertDoesNotThrow(() -> new BodyTilt(FULL_TILT, -FULL_TILT));
  }
}
