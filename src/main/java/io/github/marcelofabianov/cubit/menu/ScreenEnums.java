package io.github.marcelofabianov.cubit.menu;

/** Reads an enum sent to a screen by its position, with a safe value for an invalid one. */
final class ScreenEnums {
  private ScreenEnums() {}

  /** A value out of range shows the first one, so a screen never breaks (AGENTS.md, rule 17). */
  static <T extends Enum<T>> T of(T[] values, int ordinal) {
    return ordinal >= 0 && ordinal < values.length ? values[ordinal] : values[0];
  }
}
