package io.github.zenndg.cubit.block;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

/**
 * The two halves of the Engineering Desk, seen from its front: the left one, with the input and the
 * block entity, and the right one, with the output (answer 1456). The names are a contract with the
 * saves.
 */
public enum DeskPart implements StringRepresentable {
  LEFT,
  RIGHT;

  @Override
  public String getSerializedName() {
    return name().toLowerCase(Locale.ROOT);
  }
}
