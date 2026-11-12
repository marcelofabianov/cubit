package io.github.marcelofabianov.cubit.domain.robot;

/** The pickup switches and the filter, which the item keeps (answers 343 and 884). */
public record PickupSettings(boolean pickupOn, boolean magnetOn, PickupFilter filter) {
  /** Both switches on with an empty blacklist, also for what was saved before pickup existed. */
  public static final PickupSettings INITIAL = new PickupSettings(true, true, PickupFilter.EMPTY);

  public PickupSettings toggledPickup() {
    return new PickupSettings(!pickupOn, magnetOn, filter);
  }

  public PickupSettings toggledMagnet() {
    return new PickupSettings(pickupOn, !magnetOn, filter);
  }

  public PickupSettings withFilter(PickupFilter next) {
    return new PickupSettings(pickupOn, magnetOn, next);
  }
}
