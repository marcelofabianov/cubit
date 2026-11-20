package io.github.zenndg.cubit.domain.behavior;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The blocks the Cubits with a job hold, one per Cubit, so two never go for the same one: the stand
 * of a Fisher or the plant of a Farmer (answers 1189 and 1224). It lives only in the memory of the
 * server and never goes to the save.
 */
public final class SpotReservations {
  private final Map<Stand, UUID> holders = new HashMap<>();
  private final Map<UUID, Stand> held = new HashMap<>();

  /** Takes the stand for the robot, letting go of the one it held. False if another holds it. */
  public boolean reserve(String dimension, long stand, UUID robot) {
    Stand wanted = new Stand(dimension, stand);
    UUID holder = holders.get(wanted);
    if (holder != null && !holder.equals(robot)) {
      return false;
    }
    release(robot);
    holders.put(wanted, robot);
    held.put(robot, wanted);
    return true;
  }

  public boolean freeFor(String dimension, long stand, UUID robot) {
    UUID holder = holders.get(new Stand(dimension, stand));
    return holder == null || holder.equals(robot);
  }

  public void release(UUID robot) {
    Stand stand = held.remove(robot);
    if (stand != null) {
      holders.remove(stand);
    }
  }

  private record Stand(String dimension, long block) {}
}
