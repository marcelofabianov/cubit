package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.item.JobCardItem;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/**
 * The card slot of a robot: one card of a job its tier accepts (answers 1162 and 1163). A card kept
 * from a higher tier stays, and nothing is deleted, as in the torch slot.
 */
final class RobotCardSlot {
  private static final int SLOT = 0;
  private static final int SIZE = 1;
  private static final int ONE_CARD = 1;
  private static final String ITEM_KEY = "Item";

  private final SimpleContainer container;

  RobotCardSlot(Predicate<RobotJob> accepted) {
    container =
        new SimpleContainer(SIZE) {
          @Override
          public boolean canPlaceItem(int index, ItemStack stack) {
            return JobCardItem.jobOf(stack).filter(accepted).isPresent();
          }

          @Override
          public int getMaxStackSize() {
            return ONE_CARD;
          }
        };
  }

  Container container() {
    return container;
  }

  /** The job of the card in the slot, if any. */
  Optional<RobotJob> job() {
    return JobCardItem.jobOf(container.getItem(SLOT));
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
