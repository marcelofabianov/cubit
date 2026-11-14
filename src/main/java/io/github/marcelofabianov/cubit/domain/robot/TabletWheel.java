package io.github.marcelofabianov.cubit.domain.robot;

/**
 * Which way Shift and the wheel turn the apps of the Tablet. On macOS, Shift turns the wheel of a
 * mouse into a sideways scroll, so, like the hotbar of the game, a scroll without a vertical part
 * uses the sideways one, with its sign turned (answer 1006).
 */
public final class TabletWheel {
  private static final int NONE = 0;

  private TabletWheel() {}

  /** The step to the next app, 1 or -1, or 0 for a scroll that does not move. */
  public static int direction(double scrollX, double scrollY) {
    if (scrollY != 0) {
      return (int) -Math.signum(scrollY);
    }
    if (scrollX != 0) {
      return (int) Math.signum(scrollX);
    }
    return NONE;
  }
}
