package io.github.marcelofabianov.cubit.player;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.marcelofabianov.cubit.domain.behavior.DeathLog;
import io.github.marcelofabianov.cubit.domain.behavior.DeathRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import org.slf4j.Logger;

/**
 * How the death log is saved in the player data. The field names are a contract (answers 1114 and
 * 1127), and a death that cannot be read is skipped and logged, so a bad save never breaks the
 * player (rule 17).
 */
public final class SavedDeathLog {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String DAY_TIME_FIELD = "DayTime";
  private static final String BLOCK_FIELD = "Block";
  private static final String DIMENSION_FIELD = "Dimension";
  private static final String CAUSE_FIELD = "Cause";
  private static final String INVALID_DEATH = "Skipping a death that cannot be read: {}";

  /** The death as saved, before the domain checks it. */
  private record Saved(long dayTime, long block, String dimension, String cause) {
    static Saved of(DeathRecord death) {
      long block = new BlockPos(death.x(), death.y(), death.z()).asLong();
      return new Saved(death.dayTime(), block, death.dimension(), death.cause());
    }

    Optional<DeathRecord> read() {
      BlockPos position = BlockPos.of(block);
      try {
        return Optional.of(
            new DeathRecord(
                dayTime, position.getX(), position.getY(), position.getZ(), dimension, cause));
      } catch (IllegalArgumentException invalid) {
        LOGGER.warn(INVALID_DEATH, invalid.getMessage());
        return Optional.empty();
      }
    }
  }

  private static final Codec<Saved> SAVED_CODEC =
      RecordCodecBuilder.create(instance ->
          instance
              .group(
                  Codec.LONG.fieldOf(DAY_TIME_FIELD).forGetter(Saved::dayTime),
                  Codec.LONG.fieldOf(BLOCK_FIELD).forGetter(Saved::block),
                  Codec.STRING.fieldOf(DIMENSION_FIELD).forGetter(Saved::dimension),
                  Codec.STRING.fieldOf(CAUSE_FIELD).forGetter(Saved::cause))
              .apply(instance, Saved::new));

  public static final Codec<DeathLog> CODEC =
      SAVED_CODEC.listOf().xmap(SavedDeathLog::read, SavedDeathLog::write);

  private SavedDeathLog() {}

  private static DeathLog read(List<Saved> saved) {
    List<DeathRecord> deaths = new ArrayList<>(saved.size());
    saved.forEach(each -> each.read().ifPresent(deaths::add));
    return new DeathLog(deaths);
  }

  private static List<Saved> write(DeathLog log) {
    return log.deaths().stream().map(Saved::of).toList();
  }
}
