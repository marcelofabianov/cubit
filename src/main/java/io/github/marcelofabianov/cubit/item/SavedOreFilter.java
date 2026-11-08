package io.github.marcelofabianov.cubit.item;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

/** The tags an ore filter is saved as. An invalid tag is dropped and goes to the log. */
public final class SavedOreFilter {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String INVALID_MESSAGE = "Ignoring an invalid ore tag in a robot: {}";

  private SavedOreFilter() {}

  public static OreFilter of(List<String> tags) {
    return new OreFilter(tags.stream().flatMap(tag -> parse(tag).stream()).toList());
  }

  public static List<String> tagsOf(OreFilter filter) {
    return filter.kinds().stream().map(OreKind::tag).toList();
  }

  private static Optional<OreKind> parse(String tag) {
    Optional<OreKind> kind = OreKind.parse(tag);
    if (kind.isEmpty()) {
      LOGGER.warn(INVALID_MESSAGE, tag);
    }
    return kind;
  }
}
