package io.github.marcelofabianov.cubit.item;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.domain.robot.PickupFilter;
import io.github.marcelofabianov.cubit.domain.robot.PickupMode;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

/**
 * How the pickup filter is saved, with {@code mode} and {@code entries} (answers 889 and 891). An
 * empty entry is dropped and goes to the log, and an unknown mode is the blacklist.
 */
public final class SavedPickupFilter {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String MODE_FIELD = "mode";
  private static final String ENTRIES_FIELD = "entries";
  private static final String INVALID_MESSAGE = "Ignoring an invalid pickup filter entry: {}";

  private static final Codec<PickupMode> MODE_CODEC =
      Codec.STRING.xmap(PickupMode::fromKeyOrInitial, PickupMode::key);

  public static final Codec<PickupFilter> CODEC =
      RecordCodecBuilder.create(instance ->
          instance
              .group(
                  MODE_CODEC
                      .optionalFieldOf(MODE_FIELD, PickupMode.INITIAL)
                      .forGetter(PickupFilter::mode),
                  Codec.STRING
                      .listOf()
                      .optionalFieldOf(ENTRIES_FIELD, List.of())
                      .forGetter(SavedPickupFilter::keysOf))
              .apply(instance, SavedPickupFilter::of));

  private SavedPickupFilter() {}

  public static PickupFilter of(PickupMode mode, List<String> keys) {
    return new PickupFilter(mode, keys.stream().flatMap(key -> parse(key).stream()).toList());
  }

  public static List<String> keysOf(PickupFilter filter) {
    return filter.entries().stream().map(PickupEntry::key).toList();
  }

  private static Optional<PickupEntry> parse(String key) {
    Optional<PickupEntry> entry = PickupEntry.fromKey(key);
    if (entry.isEmpty()) {
      LOGGER.warn(INVALID_MESSAGE, key);
    }
    return entry;
  }
}
