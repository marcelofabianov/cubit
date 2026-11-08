package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.energy.Energy;

/** The numbers of the scanners at one tier. */
public record ScannerSpec(
    int oreRadiusBlocks,
    int mobRadiusBlocks,
    int oreIntervalTicks,
    int mobIntervalTicks,
    Energy oreCost,
    Energy mobCost,
    int oreFilterSlots) {
  private static final String INVALID_MESSAGE = "A scanner number must be positive: ";

  public ScannerSpec {
    requirePositive(oreRadiusBlocks);
    requirePositive(mobRadiusBlocks);
    requirePositive(oreIntervalTicks);
    requirePositive(mobIntervalTicks);
    requirePositive(oreFilterSlots);
  }

  private static void requirePositive(int value) {
    if (value < 1) {
      throw new IllegalArgumentException(INVALID_MESSAGE + value);
    }
  }
}
