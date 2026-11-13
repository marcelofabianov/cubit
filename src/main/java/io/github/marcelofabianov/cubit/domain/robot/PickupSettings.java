package io.github.marcelofabianov.cubit.domain.robot;

/** The pickup switches and the filter, which the item keeps (answers 343, 884 and 938). */
public record PickupSettings(
    boolean pickupOn, boolean magnetOn, boolean fetchOn, PickupFilter filter) {
  /** Every switch on with an empty blacklist, also for what was saved before they existed. */
  public static final PickupSettings INITIAL =
      new PickupSettings(true, true, true, PickupFilter.EMPTY);

  public PickupSettings toggledPickup() {
    return new PickupSettings(!pickupOn, magnetOn, fetchOn, filter);
  }

  public PickupSettings toggledMagnet() {
    return new PickupSettings(pickupOn, !magnetOn, fetchOn, filter);
  }

  public PickupSettings toggledFetch() {
    return new PickupSettings(pickupOn, magnetOn, !fetchOn, filter);
  }

  public PickupSettings withFilter(PickupFilter next) {
    return new PickupSettings(pickupOn, magnetOn, fetchOn, next);
  }
}
