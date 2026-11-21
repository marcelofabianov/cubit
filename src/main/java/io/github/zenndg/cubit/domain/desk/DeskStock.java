package io.github.zenndg.cubit.domain.desk;

import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;

/**
 * The energy kept in an Engineering Desk. Every operation moves whole FE, so nothing is created or
 * lost (rule 13).
 */
public record DeskStock(Battery energy) {
  /** A saved amount above the capacity, after a config change, is limited to the capacity. */
  public static DeskStock of(Energy capacity, Energy saved) {
    return new DeskStock(new Battery(capacity, saved.min(capacity)));
  }

  public Energy charge() {
    return energy.charge();
  }

  public Energy capacity() {
    return energy.capacity();
  }

  /** The battery of a new tier keeps the charge. */
  public DeskStock withCapacity(Energy capacity) {
    return of(capacity, charge());
  }

  public Received receive(Energy offered, Energy inputLeft) {
    Battery.Charged charged = energy.recharge(offered.min(inputLeft));
    return new Received(new DeskStock(charged.battery()), charged.accepted());
  }

  /** A redstone item only becomes energy when it fits whole, so nothing is wasted. */
  public boolean canTake(Energy itemEnergy) {
    return !itemEnergy.isZero() && itemEnergy.amount() <= capacity().minus(charge()).amount();
  }

  public DeskStock take(Energy itemEnergy) {
    return new DeskStock(energy.recharge(itemEnergy).battery());
  }

  /** A craft is only paid whole, at its end (answer 1435). */
  public boolean canPay(Energy cost) {
    return charge().amount() >= cost.amount();
  }

  public DeskStock pay(Energy cost) {
    return new DeskStock(energy.consume(cost).battery());
  }

  /** What a desk kept from an offer, for the caller to count against the input. */
  public record Received(DeskStock stock, Energy accepted) {}
}
