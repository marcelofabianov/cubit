package io.github.zenndg.cubit.domain.dock;

/**
 * What an item is to the storage of the dock. The game layer tells which one (answers 947 and 965).
 * The item of the Cubit and the Damaged Cubit are the same kind: both are only kept. Any other item
 * of the mod is kept too (answer 1361), and only the items of other mods stay out.
 */
public enum DockStoredKind {
  TOOLBOX,
  POWER_BANK,
  ROBOT,
  MOD_ITEM,
  OTHER
}
