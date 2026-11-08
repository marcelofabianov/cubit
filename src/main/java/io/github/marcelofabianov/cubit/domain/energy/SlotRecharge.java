package io.github.marcelofabianov.cubit.domain.energy;

/** The FE taken from an item in the energy slot, still on its way to the battery. */
public record SlotRecharge(Energy reserve) {
  public static final SlotRecharge EMPTY = new SlotRecharge(Energy.ZERO);

  /** A whole item only leaves the slot when its energy fits, so a full battery wastes nothing. */
  public boolean canTake(Energy itemEnergy, Battery battery) {
    return reserve.isZero()
        && itemEnergy.amount() <= battery.capacity().amount() - battery.charge().amount();
  }

  public SlotRecharge take(Energy itemEnergy) {
    return new SlotRecharge(reserve.plus(itemEnergy));
  }

  public Step charge(Battery battery, Energy rate) {
    Battery.Charged recharge = battery.recharge(rate.min(reserve));
    return new Step(recharge.battery(), new SlotRecharge(reserve.minus(recharge.accepted())));
  }

  /** The result of one tick. */
  public record Step(Battery battery, SlotRecharge slot) {}
}
