package io.github.marcelofabianov.cubit.domain.behavior;

import java.util.OptionalInt;

/**
 * Whether the Farmer harvests a plant: any plant that grows by age, at its last age, also from
 * other mods, unless the tag of ignored plants holds it; and a plant the tag of added crops holds,
 * at its last age when it has one (answers 1218 and 1222).
 */
public final class CropRipeness {
  private CropRipeness() {}

  public static boolean ripe(
      boolean plant, OptionalInt age, int maxAge, boolean added, boolean ignored) {
    if (ignored || !(plant || added)) {
      return false;
    }
    return age.isPresent() ? age.getAsInt() >= maxAge : added;
  }
}
