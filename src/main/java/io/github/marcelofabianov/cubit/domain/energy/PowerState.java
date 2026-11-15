package io.github.marcelofabianov.cubit.domain.energy;

/** The robot states. Inactive keeps its cause, because only an exhausted robot wakes by itself. */
public enum PowerState {
  ACTIVE(true),
  INACTIVE_EXHAUSTED(false),
  INACTIVE_SWITCHED_OFF(false),
  HIBERNATING(true),

  /** Hibernating away from the Dock, with a job, until Resume (answers 198 and 1164). */
  HIBERNATING_AWAY(true);

  private final boolean interactive;

  PowerState(boolean interactive) {
    this.interactive = interactive;
  }

  public boolean isInteractive() {
    return interactive;
  }
}
