package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.SlotRecharge;
import io.github.marcelofabianov.cubit.item.EnergySlotContents;
import io.github.marcelofabianov.cubit.item.EnergySlotItems;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/** The energy slot of a robot. It turns one item at a time into FE for the battery. */
final class RobotEnergySlot {
  private static final int SLOT = 0;
  private static final int SIZE = 1;
  private static final int ONE_ITEM = 1;
  private static final String ITEM_KEY = "Item";
  private static final String RESERVE_KEY = "Reserve";

  private final SimpleContainer container = new SimpleContainer(SIZE);
  private SlotRecharge recharge = SlotRecharge.EMPTY;

  Container container() {
    return container;
  }

  Battery charge(Battery battery, Energy rate) {
    ItemStack stack = container.getItem(SLOT);
    Energy itemEnergy = EnergySlotItems.energyOf(stack);
    if (!itemEnergy.isZero() && recharge.canTake(itemEnergy, battery)) {
      stack.shrink(ONE_ITEM);
      recharge = recharge.take(itemEnergy);
    }
    SlotRecharge.Step step = recharge.charge(battery, rate);
    recharge = step.slot();
    return step.battery();
  }

  /** Puts a whole stack in the slot, as far as it fits, and returns whether anything moved. */
  boolean insert(ItemStack stack) {
    if (!EnergySlotItems.accepts(stack) || !container.canAddItem(stack)) {
      return false;
    }
    ItemStack rest = container.addItem(stack.copy());
    stack.setCount(rest.getCount());
    return true;
  }

  EnergySlotContents contents() {
    return new EnergySlotContents(
        ItemContainerContents.fromItems(List.of(container.getItem(SLOT))),
        recharge.reserve().amount());
  }

  void fill(EnergySlotContents contents) {
    container.setItem(
        SLOT, contents.item().stream().findFirst().map(ItemStack::copy).orElse(ItemStack.EMPTY));
    recharge = new SlotRecharge(new Energy(Math.max(contents.reserve(), 0)));
  }

  void clear() {
    container.clearContent();
    recharge = SlotRecharge.EMPTY;
  }

  CompoundTag save(HolderLookup.Provider registries) {
    CompoundTag tag = new CompoundTag();
    tag.putLong(RESERVE_KEY, recharge.reserve().amount());
    ItemStack stack = container.getItem(SLOT);
    if (!stack.isEmpty()) {
      tag.put(ITEM_KEY, stack.save(registries));
    }
    return tag;
  }

  void load(CompoundTag tag, HolderLookup.Provider registries) {
    recharge = new SlotRecharge(new Energy(Math.max(tag.getLong(RESERVE_KEY), 0)));
    Optional<ItemStack> stack =
        tag.contains(ITEM_KEY)
            ? ItemStack.parse(registries, tag.getCompound(ITEM_KEY))
            : Optional.empty();
    container.setItem(SLOT, stack.orElse(ItemStack.EMPTY));
  }
}
