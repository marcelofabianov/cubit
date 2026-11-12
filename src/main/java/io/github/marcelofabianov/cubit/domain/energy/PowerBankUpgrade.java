package io.github.marcelofabianov.cubit.domain.energy;

import java.util.Optional;

/**
 * The chained Power Bank recipe keeps the charge of the bank in the center (answer 902). A charge
 * that does not fit refuses the recipe, so no FE is lost (answer 907 and rule 13).
 */
public final class PowerBankUpgrade {
  private PowerBankUpgrade() {}

  public static Optional<Battery> carry(Battery previous, Energy nextCapacity) {
    if (previous.charge().amount() > nextCapacity.amount()) {
      return Optional.empty();
    }
    return Optional.of(new Battery(nextCapacity, previous.charge()));
  }
}
