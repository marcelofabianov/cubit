package io.github.marcelofabianov.cubit.domain.tier;

import io.github.marcelofabianov.cubit.domain.energy.Energy;

/**
 * The pickup numbers of one tier. A radius of zero locks the magnet, as on Base (answers 879 and
 * 889), while pickup by touch works on every tier. The fetch radius is around the owner, and zero
 * locks it too (answer 936).
 */
public record PickupSpec(
    int magnetRadiusBlocks,
    Energy magnetCostPerItem,
    int filterSlots,
    boolean filterTags,
    int fetchRadiusBlocks) {
  /** No magnet, no fetch and no filter slots, for a tier built without pickup numbers. */
  public static final PickupSpec NONE = new PickupSpec(0, Energy.ZERO, 0, false, 0);

  private static final String NEGATIVE_MESSAGE = "A pickup number cannot be negative: ";

  public PickupSpec {
    requireNotNegative(magnetRadiusBlocks);
    requireNotNegative(filterSlots);
    requireNotNegative(fetchRadiusBlocks);
  }

  public boolean magnetUnlocked() {
    return magnetRadiusBlocks > 0;
  }

  public boolean fetchUnlocked() {
    return fetchRadiusBlocks > 0;
  }

  private static void requireNotNegative(int value) {
    if (value < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + value);
    }
  }
}
