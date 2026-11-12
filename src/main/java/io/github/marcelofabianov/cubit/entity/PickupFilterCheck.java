package io.github.marcelofabianov.cubit.entity;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.domain.robot.PickupFilter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.slf4j.Logger;

/**
 * Drops the filter entries that name an item or a tag the game does not have, and logs them (answer
 * 892). The entries the tier does not count stay, so a larger config brings them back.
 */
final class PickupFilterCheck {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String MISSING_MESSAGE = "Ignoring a pickup filter entry the game lacks: {}";

  private PickupFilterCheck() {}

  static PickupFilter existing(PickupFilter filter) {
    return new PickupFilter(
        filter.mode(), filter.entries().stream().filter(PickupFilterCheck::exists).toList());
  }

  private static boolean exists(PickupEntry entry) {
    ResourceLocation id = ResourceLocation.tryParse(entry.id());
    boolean found =
        id != null
            && (entry.tag()
                ? BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM, id)).isPresent()
                : BuiltInRegistries.ITEM.containsKey(id));
    if (!found) {
      LOGGER.warn(MISSING_MESSAGE, entry.key());
    }
    return found;
  }
}
