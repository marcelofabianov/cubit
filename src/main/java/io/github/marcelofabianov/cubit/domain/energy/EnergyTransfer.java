package io.github.marcelofabianov.cubit.domain.energy;

/**
 * Energy moved from one battery to another in one step: as much as the rate, the source and the
 * room allow, so nothing is created or lost (rule 13).
 */
public record EnergyTransfer(Battery from, Battery to, Energy moved) {
  public static EnergyTransfer move(Battery from, Battery to, Energy rate) {
    Battery.Charged received = to.recharge(rate.min(from.charge()));
    Battery.Consumed given = from.consume(received.accepted());
    return new EnergyTransfer(given.battery(), received.battery(), received.accepted());
  }
}
