package io.github.zenndg.cubit.menu;

import net.minecraft.world.entity.player.Player;

/**
 * The buttons − and + of the speed, the last row of the Settings tab and of the Resources tab of
 * the Tablet, after the buttons of the unload interval (answer 1269).
 */
public final class SpeedButtons {
  public static final int DOWN_BUTTON = 25;
  public static final int UP_BUTTON = 26;
  private static final int DOWN = -1;
  private static final int UP = 1;

  private SpeedButtons() {}

  static boolean handles(int button) {
    return button == DOWN_BUTTON || button == UP_BUTTON;
  }

  /** The server checks the menu before, and the control checks the owner (rule 20). */
  static boolean press(SpeedControl speed, Player player, int button) {
    return speed.step(player, button == DOWN_BUTTON ? DOWN : UP);
  }
}
