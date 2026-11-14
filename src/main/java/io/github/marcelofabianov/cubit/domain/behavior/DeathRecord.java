package io.github.marcelofabianov.cubit.domain.behavior;

/**
 * One death of the player: the time of the world, the block, the dimension and the cause, as the
 * game's death message in its saved form (answers 1111, 1114 and 1116).
 */
public record DeathRecord(long dayTime, int x, int y, int z, String dimension, String cause) {
  /** The Overworld, the only dimension where the Cubit guides (answer 1117). */
  public static final String OVERWORLD = "minecraft:overworld";

  private static final String NEGATIVE_TIME_MESSAGE = "A death cannot happen before the world: ";
  private static final String NO_DIMENSION_MESSAGE = "A death needs a dimension";
  private static final String NO_CAUSE_MESSAGE = "A death needs a cause";

  public DeathRecord {
    if (dayTime < 0) {
      throw new IllegalArgumentException(NEGATIVE_TIME_MESSAGE + dayTime);
    }
    if (dimension == null || dimension.isBlank()) {
      throw new IllegalArgumentException(NO_DIMENSION_MESSAGE);
    }
    if (cause == null) {
      throw new IllegalArgumentException(NO_CAUSE_MESSAGE);
    }
  }

  public boolean inOverworld() {
    return OVERWORLD.equals(dimension);
  }
}
