package io.github.marcelofabianov.cubit.entity;

import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * The torch slot of a robot: one stack of plain torches, only on a tier that places them (answers
 * 973 and 983). Torches kept from a higher tier stay, and nothing is deleted.
 */
final class RobotTorchSlot {
  private static final int SLOT = 0;
  private static final int SIZE = 1;
  private static final int ONE_TORCH = 1;
  private static final String ITEM_KEY = "Item";

  private final SimpleContainer container;

  RobotTorchSlot(BooleanSupplier unlocked) {
    container =
        new SimpleContainer(SIZE) {
          @Override
          public boolean canPlaceItem(int index, ItemStack stack) {
            return unlocked.getAsBoolean() && stack.is(Items.TORCH);
          }
        };
  }

  Container container() {
    return container;
  }

  boolean hasTorch() {
    return container.getItem(SLOT).is(Items.TORCH);
  }

  void useOne() {
    container.getItem(SLOT).shrink(ONE_TORCH);
    container.setChanged();
  }

  ItemContainerContents contents() {
    return ItemContainerContents.fromItems(List.of(container.getItem(SLOT)));
  }

  void fill(ItemContainerContents contents) {
    container.setItem(
        SLOT, contents.stream().findFirst().map(ItemStack::copy).orElse(ItemStack.EMPTY));
  }

  void clear() {
    container.clearContent();
  }

  CompoundTag save(HolderLookup.Provider registries) {
    CompoundTag tag = new CompoundTag();
    ItemStack stack = container.getItem(SLOT);
    if (!stack.isEmpty()) {
      tag.put(ITEM_KEY, stack.save(registries));
    }
    return tag;
  }

  void load(CompoundTag tag, HolderLookup.Provider registries) {
    container.setItem(
        SLOT,
        tag.contains(ITEM_KEY)
            ? ItemStack.parse(registries, tag.getCompound(ITEM_KEY)).orElse(ItemStack.EMPTY)
            : ItemStack.EMPTY);
  }
}
