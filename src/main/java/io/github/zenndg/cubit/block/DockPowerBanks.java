package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.domain.dock.DockStock;
import io.github.zenndg.cubit.domain.energy.Battery;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.item.PowerBankItem;
import java.util.function.UnaryOperator;
import net.minecraft.world.item.ItemStack;

/** The Power Banks in the input slot and in the storage of the dock (answers 919 to 959). */
final class DockPowerBanks {
  private DockPowerBanks() {}

  /** The bank in the input slot empties into the stock like the cables, within the same input. */
  static void discharge(ItemStack stack, UnaryOperator<Energy> receive) {
    Battery bank = PowerBankItem.batteryOf(stack);
    Energy accepted = receive.apply(bank.charge());
    if (!accepted.isZero()) {
      PowerBankItem.store(stack, bank.consume(accepted).battery());
    }
  }

  /**
   * At the rate of the tier of the dock (answer 1376). Entities tick before block entities, so a
   * docked robot already took its share this tick, and the bank only charges once it is full
   * (answer 952).
   */
  static DockStock charge(DockStock stock, ItemStack stack, Energy rate, Runnable changed) {
    if (!PowerBankItem.is(stack)) {
      return stock;
    }
    Battery before = PowerBankItem.batteryOf(stack);
    DockStock.Transfer transfer = stock.charge(before, rate);
    if (transfer.robot().equals(before)) {
      return stock;
    }
    PowerBankItem.store(stack, transfer.robot());
    changed.run();
    return transfer.stock();
  }
}
