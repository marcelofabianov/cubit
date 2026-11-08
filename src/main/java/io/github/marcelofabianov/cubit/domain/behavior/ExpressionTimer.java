package io.github.marcelofabianov.cubit.domain.behavior;

/** How long a temporary expression still holds. */
public record ExpressionTimer(int ticksLeft) {
  public static final ExpressionTimer STOPPED = new ExpressionTimer(0);
  private static final String NEGATIVE_MESSAGE = "A time left cannot be negative: ";

  public ExpressionTimer {
    if (ticksLeft < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + ticksLeft);
    }
  }

  public static ExpressionTimer started(int holdTicks) {
    return new ExpressionTimer(holdTicks);
  }

  public ExpressionTimer tick() {
    return new ExpressionTimer(Math.max(ticksLeft - 1, 0));
  }

  public boolean isRunning() {
    return ticksLeft > 0;
  }
}
