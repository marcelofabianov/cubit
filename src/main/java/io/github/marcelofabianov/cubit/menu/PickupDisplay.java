package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.domain.robot.PickupItem;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;

/**
 * How the game shows a filter entry and reads an item for the filter. A tag shows its first item,
 * and the tags of an item are sorted, so the server and the client count them the same way.
 */
public final class PickupDisplay {
  private PickupDisplay() {}

  public static ItemStack stackOf(PickupEntry entry) {
    ResourceLocation id = ResourceLocation.tryParse(entry.id());
    if (id == null) {
      return ItemStack.EMPTY;
    }
    if (!entry.tag()) {
      return BuiltInRegistries.ITEM.getOptional(id).map(ItemStack::new).orElse(ItemStack.EMPTY);
    }
    return BuiltInRegistries.ITEM
        .getTag(TagKey.create(Registries.ITEM, id))
        .flatMap(items -> items.stream().findFirst())
        .map(item -> new ItemStack(item.value()))
        .orElse(ItemStack.EMPTY);
  }

  public static String idOf(ItemStack stack) {
    return BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
  }

  public static List<String> sortedTags(ItemStack stack) {
    return stack.getTags().map(tag -> tag.location().toString()).sorted().toList();
  }

  public static PickupItem itemOf(ItemStack stack) {
    Set<String> tags =
        stack.getTags().map(tag -> tag.location().toString()).collect(Collectors.toSet());
    return new PickupItem(idOf(stack), tags);
  }

  /** The tag shown on a slot, from its position in the sorted tags of the shown item. */
  public static Optional<String> tagAt(ItemStack shown, int position) {
    List<String> tags = sortedTags(shown);
    return position >= 0 && position < tags.size()
        ? Optional.of(tags.get(position))
        : Optional.empty();
  }
}
