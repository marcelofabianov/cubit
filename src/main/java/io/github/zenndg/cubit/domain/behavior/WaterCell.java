package io.github.zenndg.cubit.domain.behavior;

/**
 * What one block is for the open water test of the game, in FishingHook: still water with no
 * collision is inside, air or a lily pad is above, and anything else breaks the water (answer
 * 1183).
 */
public enum WaterCell {
  INSIDE_WATER,
  ABOVE_WATER,
  INVALID
}
