package io.github.marcelofabianov.cubit.domain.dock;

/**
 * What an item is to the storage of the dock. The game layer tells which one (answers 947 and 965).
 * The item of the Cubit and the Damaged Cubit are the same kind: both are only kept.
 */
public enum DockStoredKind {
  TOOLBOX,
  POWER_BANK,
  ROBOT,
  OTHER
}
