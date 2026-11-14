package io.github.marcelofabianov.cubit.domain.tier;

/**
 * The largest area of a tier: the side of its base and its height, with 0 for a tier without the
 * area mode (answers 342 and 1050).
 */
public record AreaSpec(int sizeBlocks, int heightBlocks) {
  public static final AreaSpec NONE = new AreaSpec(0, 0);
  private static final String NEGATIVE_MESSAGE = "An area cannot be negative: ";
  private static final String SEPARATOR = ", ";

  public AreaSpec {
    if (sizeBlocks < 0 || heightBlocks < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + sizeBlocks + SEPARATOR + heightBlocks);
    }
  }

  public boolean unlocked() {
    return sizeBlocks > 0 && heightBlocks > 0;
  }
}
