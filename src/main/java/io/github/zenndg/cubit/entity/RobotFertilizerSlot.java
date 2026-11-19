package io.github.zenndg.cubit.entity;

import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.common.Tags;

/**
 * The fertilizer slot of the Farmer: any item of the common tag of fertilizers, bone meal first,
 * also from other mods, on a tier that allows it (answers 1243 and 1244). A fertilizer kept from a
 * higher tier stays, and nothing is deleted, as in the torch slot.
 */
public final class RobotFertilizerSlot {
  private static final int SLOT = 0;
  private static final int SIZE = 1;
  private static final String ITEM_KEY = "Item";

  private final SimpleContainer container;

  RobotFertilizerSlot(BooleanSupplier unlocked) {
    container =
        new SimpleContainer(SIZE) {
          @Override
          public boolean canPlaceItem(int index, ItemStack stack) {
            return unlocked.getAsBoolean() && accepts(stack);
          }
        };
  }

  public static boolean accepts(ItemStack stack) {
    return stack.is(Tags.Items.FERTILIZERS);
  }

  Container container() {
    return container;
  }

  ItemStack stack() {
    return container.getItem(SLOT);
  }

  boolean isEmpty() {
    return stack().isEmpty();
  }

  /** Brings fertilizer into the slot, and gives back what does not fit (rule 13). */
  ItemStack fill(ItemStack stack) {
    return container.addItem(stack);
  }

  ItemContainerContents contents() {
    return ItemContainerContents.fromItems(List.of(stack()));
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
    if (!stack().isEmpty()) {
      tag.put(ITEM_KEY, stack().save(registries));
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
