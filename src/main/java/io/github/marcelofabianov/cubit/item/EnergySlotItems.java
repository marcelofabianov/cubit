package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

/** Which items the energy slots accept: redstone, by tag so other mods count, and Power Banks. */
public final class EnergySlotItems {
  private EnergySlotItems() {}

  /** Redstone turns into FE, and a Power Bank stays in the slot, giving or taking charge. */
  public static boolean accepts(ItemStack stack) {
    return PowerBankItem.is(stack) || !energyOf(stack).isZero();
  }

  public static Energy energyOf(ItemStack stack) {
    if (stack.is(Tags.Items.DUSTS_REDSTONE)) {
      return new Energy(CubitMod.CONFIG.redstoneDustEnergy());
    }
    if (stack.is(Tags.Items.STORAGE_BLOCKS_REDSTONE)) {
      return new Energy(CubitMod.CONFIG.redstoneBlockEnergy());
    }
    return Energy.ZERO;
  }

  /** Puts a whole stack in the slot, as far as it fits, and returns whether anything moved. */
  public static boolean insert(SimpleContainer slot, ItemStack stack) {
    if (!accepts(stack) || !slot.canAddItem(stack)) {
      return false;
    }
    ItemStack rest = slot.addItem(stack.copy());
    stack.setCount(rest.getCount());
    return true;
  }
}
