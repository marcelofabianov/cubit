package io.github.marcelofabianov.cubit.block;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.dock.DockStock;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.PowerBankItem;
import java.util.function.UnaryOperator;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

/** The Power Banks in the two slots of the dock (answers 919 to 923). */
final class DockPowerBanks {
  private static final int ONE_SLOT = 1;

  private DockPowerBanks() {}

  /** The slot where the stock charges a bank. It takes nothing but a Power Bank. */
  static SimpleContainer chargeSlot(Runnable changed) {
    return new SimpleContainer(ONE_SLOT) {
      @Override
      public boolean canPlaceItem(int index, ItemStack stack) {
        return PowerBankItem.is(stack);
      }

      @Override
      public void setChanged() {
        changed.run();
      }
    };
  }

  /** The bank in the input slot empties into the stock like the cables, within the same input. */
  static void discharge(ItemStack stack, UnaryOperator<Energy> receive) {
    Battery bank = PowerBankItem.batteryOf(stack);
    Energy accepted = receive.apply(bank.charge());
    if (!accepted.isZero()) {
      PowerBankItem.store(stack, bank.consume(accepted).battery());
    }
  }

  /**
   * After the robot: entities tick before block entities, so a docked robot already took its share
   * this tick, and the bank gets what is left at the rate of the Base (answer 906).
   */
  static DockStock charge(DockStock stock, ItemStack stack, Runnable changed) {
    if (!PowerBankItem.is(stack)) {
      return stock;
    }
    Energy rate =
        CubitMod.CONFIG
            .tierCatalog()
            .spec(CubitConfig.GENERAL_ROBOT, RobotTier.BASE)
            .rechargePerTick();
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
