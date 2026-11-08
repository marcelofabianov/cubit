package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

/** The color of the shell, one for each dye of the game. */
public enum RobotColor {
  WHITE,
  ORANGE,
  MAGENTA,
  LIGHT_BLUE,
  YELLOW,
  LIME,
  PINK,
  GRAY,
  LIGHT_GRAY,
  CYAN,
  PURPLE,
  BLUE,
  BROWN,
  GREEN,
  RED,
  BLACK;

  /**
   * The name of the dye, used in saves and pictures, so it cannot change without a migration. The
   * domain matches it to the dyes by this name, without knowing the game.
   */
  public String key() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static Optional<RobotColor> fromKey(String key) {
    return Arrays.stream(values()).filter(color -> color.key().equals(key)).findFirst();
  }
}
