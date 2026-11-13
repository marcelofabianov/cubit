package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import net.minecraft.world.item.DyeColor;

/** The shell color of a robot on a screen, from the dye of the game (answer 1011). */
final class RobotShell {
  private static final int OPAQUE = 0xFF000000;

  private RobotShell() {}

  static int argb(RobotColor color) {
    return OPAQUE | DyeColor.byName(color.key(), DyeColor.WHITE).getTextureDiffuseColor();
  }
}
