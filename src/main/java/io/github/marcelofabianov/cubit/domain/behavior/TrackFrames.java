package io.github.marcelofabianov.cubit.domain.behavior;

/** Which picture of the tracks shows at a distance walked. Constants: this is only visual. */
public final class TrackFrames {
  public static final int COUNT = 4;

  private static final double BLOCKS_PER_FRAME = 0.4;

  private TrackFrames() {}

  /** An invalid distance shows the first frame, so a bad value never breaks the drawing. */
  public static int frameAt(double blocksWalked) {
    if (!Double.isFinite(blocksWalked) || blocksWalked < 0) {
      return 0;
    }
    return (int) (Math.floor(blocksWalked / BLOCKS_PER_FRAME) % COUNT);
  }
}
