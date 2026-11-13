package io.github.marcelofabianov.cubit.menu;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;

/**
 * What only the Tablet sends when the screen of a Cubit opens: which Cubit, for the requests of the
 * screen, and its dimension, for the place in the header.
 */
public record TabletOpening(UUID robot, String dimension) {
  public void write(RegistryFriendlyByteBuf buffer) {
    buffer.writeUUID(robot);
    buffer.writeUtf(dimension);
  }

  static TabletOpening read(RegistryFriendlyByteBuf buffer) {
    return new TabletOpening(buffer.readUUID(), buffer.readUtf());
  }
}
