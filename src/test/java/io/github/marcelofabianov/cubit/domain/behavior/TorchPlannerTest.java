package io.github.marcelofabianov.cubit.domain.behavior;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class TorchPlannerTest {
  private static final int LONG = 30;
  private static final int SHORT = 3;
  private static final LightLevel TORCH = new LightLevel(14);
  private static final LightLevel REQUIRED = new LightLevel(1);
  private static final int MIDDLE = 1;
  private static final GridCell START = new GridCell(0, MIDDLE, MIDDLE);
  private static final GridCell NEAR_START = new GridCell(2, MIDDLE, MIDDLE);
  private static final GridCell REACHED = new GridCell(13, MIDDLE, MIDDLE);
  private static final GridCell TOO_FAR = new GridCell(14, MIDDLE, MIDDLE);
  private static final GridCell ONLY_FROM_THE_MIDDLE = new GridCell(27, MIDDLE, MIDDLE);
  private static final GridCell MIDDLE_OF_HALL = new GridCell(15, MIDDLE, MIDDLE);
  private static final int WALL_X = 5;

  private static TorchGrid hall() {
    return new TorchGrid(LONG, SHORT, SHORT);
  }

  @Test
  void withoutADangerousSpotNoTorchIsPlaced() {
    TorchGrid grid = hall();
    grid.addCandidate(START);

    assertEquals(Optional.empty(), TorchPlanner.best(grid, TORCH, REQUIRED));
  }

  @Test
  void theLightFallsOneLevelPerBlock() {
    TorchGrid reached = hall();
    reached.addCandidate(START);
    reached.markDangerous(REACHED);
    TorchGrid tooFar = hall();
    tooFar.addCandidate(START);
    tooFar.markDangerous(TOO_FAR);

    assertEquals(Optional.of(START), TorchPlanner.best(reached, TORCH, REQUIRED));
    assertEquals(Optional.empty(), TorchPlanner.best(tooFar, TORCH, REQUIRED));
  }

  @Test
  void theSpotThatMakesMoreBlocksSafeWins() {
    TorchGrid grid = hall();
    grid.addCandidate(START);
    grid.addCandidate(MIDDLE_OF_HALL);
    grid.markDangerous(NEAR_START);
    grid.markDangerous(REACHED);
    grid.markDangerous(ONLY_FROM_THE_MIDDLE);

    assertEquals(Optional.of(MIDDLE_OF_HALL), TorchPlanner.best(grid, TORCH, REQUIRED));
  }

  @Test
  void theLightDoesNotCrossAWall() {
    TorchGrid grid = hall();
    for (int y = 0; y < SHORT; y++) {
      for (int z = 0; z < SHORT; z++) {
        grid.markBlocking(new GridCell(WALL_X, y, z));
      }
    }
    grid.addCandidate(START);
    grid.markDangerous(REACHED);

    assertEquals(Optional.empty(), TorchPlanner.best(grid, TORCH, REQUIRED));
  }

  @Test
  void theFirstOfEqualSpotsWins() {
    TorchGrid grid = hall();
    grid.addCandidate(START);
    grid.addCandidate(NEAR_START);
    grid.markDangerous(new GridCell(1, MIDDLE, MIDDLE));

    assertEquals(Optional.of(START), TorchPlanner.best(grid, TORCH, REQUIRED));
  }

  @Test
  void aCellOutsideTheGridIsRejected() {
    TorchGrid grid = hall();

    assertThrows(
        IllegalArgumentException.class, () -> grid.markDangerous(new GridCell(LONG, 0, 0)));
    assertThrows(IllegalArgumentException.class, () -> new TorchGrid(0, SHORT, SHORT));
  }
}
