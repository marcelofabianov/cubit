package io.github.marcelofabianov.cubit.block;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.dock.DockStock;
import io.github.marcelofabianov.cubit.domain.dock.DockStorageRules;
import io.github.marcelofabianov.cubit.domain.dock.DockStoredKind;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.item.PowerBankItem;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.slf4j.Logger;

/**
 * The storage of a dock: 27 slots for the Toolbox, the Power Banks, which the stock charges, and
 * the items of the robot (answers 947 to 968). The rules live in {@link DockStorageRules}.
 */
final class DockStorage {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String STORAGE_KEY = "Storage";
  private static final String SLOT_KEY = "Slot";
  private static final String LEGACY_CHARGE_SLOT_KEY = "ChargeSlot";
  private static final int UNSIGNED_BYTE = 0xFF;
  private static final String MOVED_MESSAGE = "The old charge slot of the dock at {} moved.";
  private static final String REFUSED_MESSAGE = "The dock at {} cannot store {}, so it drops.";

  private final SimpleContainer items;
  private final List<ItemStack> refused = new ArrayList<>();

  DockStorage(Runnable changed) {
    items =
        new SimpleContainer(DockStorageRules.SLOTS) {
          @Override
          public boolean canPlaceItem(int index, ItemStack stack) {
            return DockStorageRules.accepts(kindOf(stack));
          }

          @Override
          public void setChanged() {
            changed.run();
          }
        };
  }

  static DockStoredKind kindOf(ItemStack stack) {
    if (PowerBankItem.is(stack)) {
      return DockStoredKind.POWER_BANK;
    }
    if (stack.getItem() instanceof StorableRobotItem) {
      return DockStoredKind.ROBOT;
    }
    return stack.is(CubitItems.TOOLBOX.get()) ? DockStoredKind.TOOLBOX : DockStoredKind.OTHER;
  }

  Container container() {
    return items;
  }

  /** The automation puts both items in by any face, and takes what the rules release (953). */
  IItemHandlerModifiable handler() {
    return new InvWrapper(items) {
      @Override
      public ItemStack extractItem(int index, int amount, boolean simulate) {
        ItemStack stack = items.getItem(index);
        boolean full = PowerBankItem.is(stack) && PowerBankItem.batteryOf(stack).isFull();
        return DockStorageRules.releases(kindOf(stack), full)
            ? super.extractItem(index, amount, simulate)
            : ItemStack.EMPTY;
      }
    };
  }

  /** The item in the hand of the owner, joining the stacks and the free slots (answer 959). */
  boolean insert(ItemStack hand) {
    if (!DockStorageRules.accepts(kindOf(hand)) || !items.canAddItem(hand)) {
      return false;
    }
    ItemStack rest = items.addItem(hand.copy());
    hand.setCount(rest.getCount());
    return true;
  }

  /** One bank at a time, and only while no robot in the base still charges (949 and 952). */
  DockStock charge(DockStock stock, boolean chargesBanks, Runnable changed) {
    if (!chargesBanks) {
      return stock;
    }
    OptionalInt index = DockStorageRules.bankToCharge(banks());
    return index.isPresent()
        ? DockPowerBanks.charge(stock, items.getItem(index.getAsInt()), changed)
        : stock;
  }

  private List<Optional<Battery>> banks() {
    List<Optional<Battery>> banks = new ArrayList<>();
    for (int index = 0; index < items.getContainerSize(); index++) {
      ItemStack stack = items.getItem(index);
      banks.add(
          PowerBankItem.is(stack) ? Optional.of(PowerBankItem.batteryOf(stack)) : Optional.empty());
    }
    return banks;
  }

  void save(CompoundTag tag, HolderLookup.Provider registries) {
    ListTag list = new ListTag();
    for (int index = 0; index < items.getContainerSize(); index++) {
      ItemStack stack = items.getItem(index);
      if (!stack.isEmpty()) {
        CompoundTag entry = new CompoundTag();
        entry.putByte(SLOT_KEY, (byte) index);
        list.add(stack.save(registries, entry));
      }
    }
    tag.put(STORAGE_KEY, list);
  }

  /** The bank of the old charge slot moves to the first free slot, so nothing is lost (955). */
  void load(CompoundTag tag, HolderLookup.Provider registries, BlockPos pos) {
    items.clearContent();
    ListTag list = tag.getList(STORAGE_KEY, Tag.TAG_COMPOUND);
    for (int entry = 0; entry < list.size(); entry++) {
      CompoundTag saved = list.getCompound(entry);
      int index = saved.getByte(SLOT_KEY) & UNSIGNED_BYTE;
      ItemStack stack = ItemStack.parse(registries, saved).orElse(ItemStack.EMPTY);
      if (index < items.getContainerSize()) {
        keep(index, stack, pos);
      }
    }
    if (tag.contains(LEGACY_CHARGE_SLOT_KEY)) {
      ItemStack bank =
          ItemStack.parse(registries, tag.getCompound(LEGACY_CHARGE_SLOT_KEY))
              .orElse(ItemStack.EMPTY);
      moveLegacyBank(bank, pos);
    }
  }

  private void moveLegacyBank(ItemStack bank, BlockPos pos) {
    if (bank.isEmpty()) {
      return;
    }
    ItemStack rest = items.addItem(bank);
    if (!rest.isEmpty()) {
      refused.add(rest);
    }
    LOGGER.info(MOVED_MESSAGE, pos);
  }

  ItemContainerContents contents() {
    return ItemContainerContents.fromItems(items.getItems());
  }

  void apply(ItemContainerContents contents, BlockPos pos) {
    NonNullList<ItemStack> saved = NonNullList.withSize(items.getContainerSize(), ItemStack.EMPTY);
    contents.copyInto(saved);
    items.clearContent();
    for (int index = 0; index < saved.size(); index++) {
      keep(index, saved.get(index), pos);
    }
  }

  /** An item the storage does not keep, from an edited save, drops beside the dock (rule 17). */
  private void keep(int index, ItemStack stack, BlockPos pos) {
    if (stack.isEmpty()) {
      return;
    }
    if (DockStorageRules.accepts(kindOf(stack))) {
      items.setItem(index, stack);
      return;
    }
    LOGGER.warn(REFUSED_MESSAGE, pos, stack);
    refused.add(stack);
  }

  /** The level is not there while loading, so the refused items drop on the next tick. */
  void dropRefused(Level level, BlockPos pos) {
    if (refused.isEmpty()) {
      return;
    }
    refused.forEach(
        stack -> Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack));
    refused.clear();
  }
}
