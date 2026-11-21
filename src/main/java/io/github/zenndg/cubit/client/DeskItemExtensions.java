package io.github.zenndg.cubit.client;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

/** Gives the item of the desk its GeckoLib renderer, made once, on first use. */
final class DeskItemExtensions implements IClientItemExtensions {
  static final DeskItemExtensions INSTANCE = new DeskItemExtensions();
  private DeskItemRenderer renderer;

  private DeskItemExtensions() {}

  @Override
  public BlockEntityWithoutLevelRenderer getCustomRenderer() {
    if (renderer == null) {
      renderer = new DeskItemRenderer();
    }
    return renderer;
  }
}
