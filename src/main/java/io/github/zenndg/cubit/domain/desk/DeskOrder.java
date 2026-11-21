package io.github.zenndg.cubit.domain.desk;

/**
 * An order of the Engineering Desk: how many items were asked and how many are done. The desk
 * crafts one at a time and stops when the order ends. A chosen recipe starts with no order, and
 * plus asks the first items (answers 1429, 1442, 1444 and 1506).
 */
public record DeskOrder(int total, int done) {
  public static final DeskOrder NONE = new DeskOrder(0, 0);

  /** The minus and plus buttons move one, and eight with Shift (answer 1444). */
  public static final int STEP = 1;

  public static final int SHIFT_STEP = 8;

  /** The comparator gives 15 at the start of an order and 0 at its end (answer 1453). */
  public static final int MAX_SIGNAL = 15;

  private static final String INVALID = "Invalid order: ";
  private static final String OF = " of ";

  public DeskOrder {
    if (total < 0 || done < 0 || done > total) {
      throw new IllegalArgumentException(INVALID + done + OF + total);
    }
  }

  public boolean isActive() {
    return remaining() > 0;
  }

  public int remaining() {
    return total - done;
  }

  /** The total stays between one and the largest order, and reaching what is done ends it. */
  public DeskOrder adjust(int delta, int maxOrder) {
    int wanted = Math.max(STEP, Math.min(maxOrder, total + delta));
    return wanted <= done ? NONE : new DeskOrder(wanted, done);
  }

  /** A smaller largest order, after a config change or a tier, limits the total. */
  public DeskOrder limitedTo(int maxOrder) {
    return total <= maxOrder ? this : adjust(maxOrder - total, maxOrder);
  }

  public DeskOrder crafted() {
    int next = done + 1;
    return next >= total ? NONE : new DeskOrder(total, next);
  }

  /** What is left of the order, from 15 down to 0, rounded up so the last item still shows. */
  public int comparatorSignal() {
    if (!isActive()) {
      return 0;
    }
    return (int) Math.ceil((double) remaining() * MAX_SIGNAL / total);
  }
}
