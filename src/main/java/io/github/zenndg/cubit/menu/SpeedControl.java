package io.github.zenndg.cubit.menu;

import net.minecraft.world.entity.player.Player;

/**
 * The speed control of the Settings tab and of the Resources tab of the Tablet: the percentage of
 * the speed of the tier, the lowest one for the hint, and the buttons − and + (answers 1259, 1269).
 */
public interface SpeedControl {
  /** On the client, where the real values come from the server. */
  SpeedControl NONE =
      new SpeedControl() {
        @Override
        public int percent() {
          return 0;
        }

        @Override
        public int minPercent() {
          return 0;
        }

        @Override
        public boolean step(Player player, int direction) {
          return false;
        }
      };

  int percent();

  int minPercent();

  /** One press of − or +, only for the owner (rule 20). */
  boolean step(Player player, int direction);
}
