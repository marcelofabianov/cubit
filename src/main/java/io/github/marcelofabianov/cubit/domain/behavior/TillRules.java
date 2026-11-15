package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * Whether the Farmer tills a block: one the hoe of the game turns into farmland, with air above and
 * water near enough to keep it wet, and never a protected block (answers 195 and 1232).
 */
public final class TillRules {
  private TillRules() {}

  public static boolean tills(
      boolean hoeMakesFarmland, boolean airAbove, boolean nearWater, boolean protectedBlock) {
    return hoeMakesFarmland && airAbove && nearWater && !protectedBlock;
  }
}
