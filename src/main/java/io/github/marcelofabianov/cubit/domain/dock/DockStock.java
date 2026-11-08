package io.github.marcelofabianov.cubit.domain.dock;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;

/** The energy kept in a dock. Every operation moves whole FE, so nothing is created or lost. */
public record DockStock(Battery energy) {
  /** A saved amount above the capacity, after a config change, is limited to the capacity. */
  public static DockStock of(Energy capacity, Energy saved) {
    return new DockStock(new Battery(capacity, saved.min(capacity)));
  }

  public Received receive(Energy offered, Energy inputLeft) {
    Battery.Charged charged = energy.recharge(offered.min(inputLeft));
    return new Received(new DockStock(charged.battery()), charged.accepted());
  }

  /** A whole item only leaves the slot when its energy fits, so nothing is wasted. */
  public boolean canTake(Energy itemEnergy) {
    return !itemEnergy.isZero()
        && itemEnergy.amount() <= energy.capacity().minus(energy.charge()).amount();
  }

  public DockStock take(Energy itemEnergy) {
    return new DockStock(energy.recharge(itemEnergy).battery());
  }

  /** The robot takes what it can at its rate, and the stock loses exactly that amount. */
  public Transfer charge(Battery robot, Energy rate) {
    Battery.Charged charged = robot.recharge(rate.min(energy.charge()));
    return new Transfer(
        new DockStock(energy.consume(charged.accepted()).battery()), charged.battery());
  }

  /** What a dock kept from an offer, for the caller to report and to count against the input. */
  public record Received(DockStock stock, Energy accepted) {}

  /** The result of one tick of charging. */
  public record Transfer(DockStock stock, Battery robot) {}
}
