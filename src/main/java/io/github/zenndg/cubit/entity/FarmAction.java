package io.github.zenndg.cubit.entity;

/**
 * What the Farmer does at a block: harvest a ripe plant and plant it again, cut a tall plant above
 * its base, pick a fruit off its stem, till by the water, or plant on empty farmland (answers 1219,
 * 1220 and 1232).
 */
enum FarmAction {
  HARVEST,
  TALL,
  FRUIT,
  TILL,
  PLANT
}
