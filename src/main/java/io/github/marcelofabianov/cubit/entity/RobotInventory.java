package io.github.marcelofabianov.cubit.entity;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.robot.InventoryFit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.slf4j.Logger;

/**
 * The items a robot carries. The surplus of a smaller config is kept and saved until it can be
 * dropped, because dropping it while the entity loads is not safe and nothing can be deleted.
 */
final class RobotInventory {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String SLOT_KEY = "Slot";
  private static final String SURPLUS_MESSAGE = "A robot holds {} item stacks above its {} slots.";
  private static final String INVALID_ITEM_MESSAGE = "Skipping an invalid robot item: {}";

  private final List<ItemStack> surplus = new ArrayList<>();
  private SimpleContainer container;

  RobotInventory(int slots) {
    container = new SimpleContainer(slots);
  }

  Container container() {
    return container;
  }

  void fill(List<ItemStack> saved, int slots) {
    container = new SimpleContainer(slots);
    surplus.clear();
    if (saved.size() <= slots
        || saved.subList(slots, saved.size()).stream().allMatch(ItemStack::isEmpty)) {
      for (int slot = 0; slot < Math.min(slots, saved.size()); slot++) {
        container.setItem(slot, saved.get(slot).copy());
      }
      return;
    }
    fillCompacted(saved, slots);
  }

  private void fillCompacted(List<ItemStack> saved, int slots) {
    List<ItemStack> items = saved.stream().filter(stack -> !stack.isEmpty()).toList();
    InventoryFit<ItemStack> fit = InventoryFit.of(items, slots);
    for (int slot = 0; slot < fit.kept().size(); slot++) {
      container.setItem(slot, fit.kept().get(slot).copy());
    }
    fit.surplus().forEach(stack -> surplus.add(stack.copy()));
    if (!surplus.isEmpty()) {
      LOGGER.warn(SURPLUS_MESSAGE, surplus.size(), slots);
    }
  }

  void dropSurplus(Consumer<ItemStack> drop) {
    surplus.forEach(drop);
    surplus.clear();
  }

  ItemContainerContents contents() {
    return ItemContainerContents.fromItems(allStacks());
  }

  /** The screen may still be open, so emptying this is what prevents a duplicate. */
  void clear() {
    container.clearContent();
    surplus.clear();
  }

  ListTag save(HolderLookup.Provider registries) {
    ListTag items = new ListTag();
    List<ItemStack> stacks = allStacks();
    for (int slot = 0; slot < stacks.size(); slot++) {
      if (!stacks.get(slot).isEmpty()) {
        CompoundTag entry = new CompoundTag();
        entry.putInt(SLOT_KEY, slot);
        items.add(stacks.get(slot).save(registries, entry));
      }
    }
    return items;
  }

  void load(ListTag items, HolderLookup.Provider registries, int slots) {
    List<ItemStack> saved = new ArrayList<>();
    for (int index = 0; index < items.size(); index++) {
      CompoundTag entry = items.getCompound(index);
      int slot = Math.max(entry.getInt(SLOT_KEY), 0);
      ItemStack stack = ItemStack.parse(registries, entry).orElse(ItemStack.EMPTY);
      if (stack.isEmpty()) {
        LOGGER.warn(INVALID_ITEM_MESSAGE, entry);
        continue;
      }
      while (saved.size() <= slot) {
        saved.add(ItemStack.EMPTY);
      }
      saved.set(slot, stack);
    }
    fill(saved, slots);
  }

  private List<ItemStack> allStacks() {
    List<ItemStack> stacks = new ArrayList<>(container.getItems());
    stacks.addAll(surplus);
    return stacks;
  }
}
