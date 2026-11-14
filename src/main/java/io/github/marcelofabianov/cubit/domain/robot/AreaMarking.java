package io.github.marcelofabianov.cubit.domain.robot;

import java.util.Optional;
import java.util.UUID;

/**
 * A marking of the Area app halfway: the Cubit clicked first and, after it, the first corner. It
 * lives only in the memory of the server, for one player (answers 1054 and 1060).
 */
public record AreaMarking(UUID robot, Optional<AreaCorner> first) {
  public static AreaMarking of(UUID robot) {
    return new AreaMarking(robot, Optional.empty());
  }

  public AreaMarking withFirst(AreaCorner corner) {
    return new AreaMarking(robot, Optional.of(corner));
  }
}
