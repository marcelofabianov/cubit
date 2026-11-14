package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * How the guide is getting on: the closest it got to the spot and the ticks since it last got a
 * block closer. Waiting for the owner is not being stuck (answer 1123).
 */
public record DeathGuideProgress(double closest, int stillTicks) {
  private static final double PROGRESS_BLOCKS = 1.0;

  public static DeathGuideProgress start(double distance) {
    return new DeathGuideProgress(distance, 0);
  }

  public DeathGuideProgress after(double distance, boolean waiting) {
    if (closest - distance >= PROGRESS_BLOCKS) {
      return new DeathGuideProgress(distance, 0);
    }
    return new DeathGuideProgress(closest, waiting ? 0 : stillTicks + 1);
  }
}
