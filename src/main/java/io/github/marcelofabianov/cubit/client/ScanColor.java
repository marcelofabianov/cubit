package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.behavior.ScanSignal;
import java.util.Optional;
import net.minecraft.util.FastColor;

/**
 * The colors of the scanners, the same in the boxes, the antennas and the halo, so each color has a
 * single meaning (answers 555 and 642). Only visual, so they stay here and not in the config.
 */
enum ScanColor {
  ORE(0.55f, 0.8f, 1f),
  MOB(1f, 0.45f, 0.45f),
  /** The yellow of the shock, for the target of Focus (answer 853). */
  FOCUS(0.96f, 0.77f, 0f);

  private static final int MAX = 255;

  final float red;
  final float green;
  final float blue;

  ScanColor(float red, float green, float blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  static Optional<ScanColor> of(ScanSignal signal) {
    return switch (signal) {
      case MOB -> Optional.of(MOB);
      case ORE -> Optional.of(ORE);
      case NONE -> Optional.empty();
    };
  }

  /** Opaque, for a tint over a texture. */
  int argb() {
    return FastColor.ARGB32.color(MAX, channel(red), channel(green), channel(blue));
  }

  static int channel(float value) {
    return Math.round(value * MAX);
  }
}
