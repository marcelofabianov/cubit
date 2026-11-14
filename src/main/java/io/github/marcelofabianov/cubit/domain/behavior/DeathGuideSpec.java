package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * The numbers of the guide, from the config: how close counts as there, how far ahead of the owner
 * the Cubit may walk, and how long without getting closer before it gives up (answer 1126).
 */
public record DeathGuideSpec(int arrivalBlocks, int leadBlocks, int stuckTicks) {
  private static final String NOT_POSITIVE_MESSAGE = "The guide numbers must be positive: ";
  private static final String SEPARATOR = ", ";

  public DeathGuideSpec {
    if (arrivalBlocks <= 0 || leadBlocks <= 0 || stuckTicks <= 0) {
      throw new IllegalArgumentException(
          NOT_POSITIVE_MESSAGE + arrivalBlocks + SEPARATOR + leadBlocks + SEPARATOR + stuckTicks);
    }
  }
}
