package io.github.zenndg.cubit.block;

import com.mojang.logging.LogUtils;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.slf4j.Logger;

/**
 * How a desk keeps its inventories and its order, in the save and in its item. An invalid order is
 * dropped with a warning, and the items are never lost (rule 17).
 */
final class DeskSaves {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String BAD_ORDER = "The desk at {} has an invalid order: {}";

  private DeskSaves() {}

  static void saveItems(
      CompoundTag tag, String key, SimpleContainer items, HolderLookup.Provider registries) {
    CompoundTag inner = new CompoundTag();
    ContainerHelper.saveAllItems(inner, items.getItems(), registries);
    tag.put(key, inner);
  }

  static void loadItems(
      CompoundTag tag, String key, SimpleContainer items, HolderLookup.Provider registries) {
    NonNullList<ItemStack> loaded = NonNullList.withSize(items.getContainerSize(), ItemStack.EMPTY);
    ContainerHelper.loadAllItems(tag.getCompound(key), loaded, registries);
    for (int slot = 0; slot < loaded.size(); slot++) {
      items.setItem(slot, loaded.get(slot));
    }
  }

  static void saveOrder(CompoundTag tag, String key, Optional<SavedDeskOrder> order) {
    order.ifPresent(saved ->
        SavedDeskOrder.CODEC
            .encodeStart(NbtOps.INSTANCE, saved)
            .ifSuccess(encoded -> tag.put(key, encoded)));
  }

  static Optional<SavedDeskOrder> loadOrder(CompoundTag tag, String key, BlockPos pos) {
    if (!tag.contains(key)) {
      return Optional.empty();
    }
    return SavedDeskOrder.CODEC
        .parse(NbtOps.INSTANCE, tag.get(key))
        .resultOrPartial(error -> LOGGER.warn(BAD_ORDER, pos, error));
  }

  static ItemContainerContents contents(SimpleContainer items) {
    return ItemContainerContents.fromItems(items.getItems());
  }

  static void apply(ItemContainerContents contents, SimpleContainer items) {
    if (contents == null) {
      return;
    }
    NonNullList<ItemStack> saved = NonNullList.withSize(items.getContainerSize(), ItemStack.EMPTY);
    contents.copyInto(saved);
    for (int slot = 0; slot < saved.size(); slot++) {
      items.setItem(slot, saved.get(slot));
    }
  }

  static boolean hasItems(ItemContainerContents contents) {
    return contents != null && contents.nonEmptyItems().iterator().hasNext();
  }
}
