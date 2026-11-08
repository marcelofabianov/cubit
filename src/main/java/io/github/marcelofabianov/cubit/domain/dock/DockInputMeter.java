package io.github.marcelofabianov.cubit.domain.dock;

import io.github.marcelofabianov.cubit.domain.energy.Energy;

/**
 * What entered a dock per tick, on average over the last second, for its screen. Cables push energy
 * in bursts, so one tick alone would make the number jump.
 */
public final class DockInputMeter {
  public static final int TICKS_PER_SECOND = 20;

  private final long[] received = new long[TICKS_PER_SECOND];
  private long total;
  private int next;

  /** Called once per tick, with what entered the stock in that tick. */
  public void record(Energy energy) {
    total += energy.amount() - received[next];
    received[next] = energy.amount();
    next = (next + 1) % TICKS_PER_SECOND;
  }

  public Energy perTick() {
    return new Energy(total / TICKS_PER_SECOND);
  }
}
