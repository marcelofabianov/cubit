package io.github.marcelofabianov.cubit.domain.dock;

import io.github.marcelofabianov.cubit.domain.energy.Battery;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * The storage of the dock: what it keeps, when and which Power Bank it charges, and what the
 * automation may take out (answers 947 to 959 and 965 to 968).
 */
public final class DockStorageRules {
  /** The size of a single chest of the game (answer 947). */
  public static final int SLOTS = 27;

  private DockStorageRules() {}

  public static boolean accepts(DockStoredKind kind) {
    return kind != DockStoredKind.OTHER;
  }

  /** The robot comes first: the banks wait while a docked robot still charges (answer 952). */
  public static boolean chargesBanks(boolean occupied, boolean robotFull) {
    return !occupied || robotFull;
  }

  /**
   * One bank at a time, the first that is not full in the order of the slots, so it fills before
   * the next one starts (answer 949). An empty slot or another item is an empty optional.
   */
  public static OptionalInt bankToCharge(List<Optional<Battery>> slots) {
    return IntStream.range(0, slots.size())
        .filter(index -> slots.get(index).filter(bank -> !bank.isFull()).isPresent())
        .findFirst();
  }

  /** The toolbox and the robot items leave at any time, and a bank only when full (953, 967). */
  public static boolean releases(DockStoredKind kind, boolean bankFull) {
    return switch (kind) {
      case TOOLBOX, ROBOT -> true;
      case POWER_BANK -> bankFull;
      case OTHER -> false;
    };
  }
}
