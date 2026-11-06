package io.github.marcelofabianov.cubit.domain.behavior;

/** How long a notice shown to the owner still waits before it can repeat. */
public record NoticeRepeat(int ticksLeft) {
  public static final NoticeRepeat READY = new NoticeRepeat(0);

  public boolean canShow() {
    return ticksLeft == 0;
  }

  public NoticeRepeat shown(int intervalTicks) {
    return new NoticeRepeat(intervalTicks);
  }

  public NoticeRepeat tick() {
    return new NoticeRepeat(Math.max(ticksLeft - 1, 0));
  }
}
