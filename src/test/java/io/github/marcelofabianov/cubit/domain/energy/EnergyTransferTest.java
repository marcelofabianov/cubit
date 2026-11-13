package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EnergyTransferTest {
  private static final Energy BANK_CAPACITY = new Energy(210_000);
  private static final Energy ROBOT_CAPACITY = new Energy(300_000);
  private static final Energy RATE = new Energy(500);
  private static final Energy LITTLE = new Energy(200);
  private static final Energy ALMOST_FULL = new Energy(299_900);
  private static final Energy PLENTY = new Energy(100_000);

  @Test
  void movesUpToTheRate() {
    EnergyTransfer moved =
        EnergyTransfer.move(
            new Battery(BANK_CAPACITY, PLENTY), new Battery(ROBOT_CAPACITY, Energy.ZERO), RATE);

    assertEquals(RATE, moved.moved());
    assertEquals(PLENTY.minus(RATE), moved.from().charge());
    assertEquals(RATE, moved.to().charge());
  }

  @Test
  void movesOnlyWhatTheSourceHas() {
    EnergyTransfer moved =
        EnergyTransfer.move(
            new Battery(BANK_CAPACITY, LITTLE), new Battery(ROBOT_CAPACITY, Energy.ZERO), RATE);

    assertEquals(LITTLE, moved.moved());
    assertEquals(Energy.ZERO, moved.from().charge());
  }

  @Test
  void movesOnlyWhatFitsAndNeverLosesEnergy() {
    Battery from = new Battery(BANK_CAPACITY, PLENTY);
    Battery to = new Battery(ROBOT_CAPACITY, ALMOST_FULL);
    EnergyTransfer moved = EnergyTransfer.move(from, to, RATE);

    assertEquals(ROBOT_CAPACITY.minus(ALMOST_FULL), moved.moved());
    assertEquals(from.charge().plus(to.charge()), moved.from().charge().plus(moved.to().charge()));
  }
}
