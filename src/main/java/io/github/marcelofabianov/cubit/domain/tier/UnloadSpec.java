package io.github.marcelofabianov.cubit.domain.tier;

/** The chests a tier may mark and how far they may be from the Cubit (answer 1094). */
public record UnloadSpec(int chests, int reachBlocks) {
  public static final UnloadSpec NONE = new UnloadSpec(0, 0);
  private static final String NEGATIVE_MESSAGE = "Unload numbers cannot be negative: ";
  private static final String SEPARATOR = ", ";

  public UnloadSpec {
    if (chests < 0 || reachBlocks < 0) {
      throw new IllegalArgumentException(NEGATIVE_MESSAGE + chests + SEPARATOR + reachBlocks);
    }
  }
}
