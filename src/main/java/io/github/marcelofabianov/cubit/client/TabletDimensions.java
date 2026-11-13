package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.menu.TabletKeys;
import java.util.Map;
import net.minecraft.network.chat.Component;

/** The name of a dimension on the Tablet: the three of the game by name, and others by id. */
final class TabletDimensions {
  private static final String OVERWORLD = "minecraft:overworld";
  private static final String NETHER = "minecraft:the_nether";
  private static final String END = "minecraft:the_end";
  private static final Map<String, String> KEYS =
      Map.of(OVERWORLD, TabletKeys.OVERWORLD, NETHER, TabletKeys.NETHER, END, TabletKeys.END);

  private TabletDimensions() {}

  static Component name(String id) {
    String key = KEYS.get(id);
    return key == null ? Component.literal(id) : Component.translatable(key);
  }
}
