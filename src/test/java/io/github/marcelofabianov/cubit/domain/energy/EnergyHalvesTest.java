package io.github.marcelofabianov.cubit.domain.energy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EnergyHalvesTest {
  private static final long BATTERY = 300_000;
  private static final long LOW_HALF_WITH_SIGN_BIT = 0x1_8000L;
  private static final long ULTIMATE_BATTERY = 8_000_000;
  private static final long LARGEST = 0xFFFF_FFFFL;
  private static final long TOO_LARGE = LARGEST + 1;

  /** The game sends each half as a signed short, so the client sees it sign-extended. */
  private static long roundTrip(long amount) {
    Energy energy = new Energy(amount);
    short low = (short) EnergyHalves.low(energy);
    short high = (short) EnergyHalves.high(energy);
    return EnergyHalves.join(low, high).amount();
  }

  @Test
  void anAmountSurvivesTheTrip() {
    assertEquals(0, roundTrip(0));
    assertEquals(BATTERY, roundTrip(BATTERY));
    assertEquals(ULTIMATE_BATTERY, roundTrip(ULTIMATE_BATTERY));
    assertEquals(LARGEST, roundTrip(LARGEST));
  }

  @Test
  void aHalfWithTheSignBitIsNotReadAsNegative() {
    assertEquals(LOW_HALF_WITH_SIGN_BIT, roundTrip(LOW_HALF_WITH_SIGN_BIT));
  }

  @Test
  void anAmountTooLargeForTwoHalvesShowsTheLargestOne() {
    assertEquals(LARGEST, roundTrip(TOO_LARGE));
    assertEquals(LARGEST, roundTrip(Long.MAX_VALUE));
  }
}
