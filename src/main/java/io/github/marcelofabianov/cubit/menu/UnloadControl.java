package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.robot.UnloadCall;
import net.minecraft.world.entity.player.Player;

/** `Unload` of the Tablet. The server decides whether the Cubit goes (answer 1089). */
public interface UnloadControl {
  /** On the client, where the real state comes from the server. */
  UnloadControl NONE =
      new UnloadControl() {
        @Override
        public UnloadCall goal() {
          return UnloadCall.NO_CHEST;
        }

        @Override
        public boolean unload(Player player) {
          return false;
        }
      };

  /** Whether the Cubit can unload now, or why not. */
  UnloadCall goal();

  /** False when the player is not the owner or the Cubit cannot unload. */
  boolean unload(Player player);
}
