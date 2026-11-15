package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.robot.WorkArea;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * The search for the work of the Farmer in its area, spread over the ticks with the budget of
 * blocks of the scanners (rules 21 and 22): from the marked blocks up to one block over the top,
 * where the plants grow (answer 1224).
 */
final class FarmingScan {
  /** Reading the water around ground to till costs much more than one block. */
  private static final int READS_PER_BLOCK = 4;

  private final Level level;
  private final WorkArea area;
  private final boolean special;
  private final List<FarmTask> found = new ArrayList<>();
  private final long volume;
  private long next;

  FarmingScan(Level level, WorkArea area, boolean special) {
    this.level = level;
    this.area = area;
    this.special = special;
    this.volume = (long) area.width() * area.depth() * (area.height() + 1);
  }

  boolean done() {
    return next >= volume;
  }

  List<FarmTask> found() {
    return found;
  }

  void step(int budget) {
    int blocks = Math.max(1, budget / READS_PER_BLOCK);
    for (int read = 0; read < blocks && !done(); read++) {
      BlockPos pos = posAt(next++);
      if (level.isLoaded(pos)) {
        FarmPlants.actionAt(level, pos, special)
            .ifPresent(action -> found.add(new FarmTask(pos, action)));
      }
    }
  }

  private BlockPos posAt(long index) {
    int x = (int) (index % area.width());
    int z = (int) (index / area.width() % area.depth());
    int y = (int) (index / ((long) area.width() * area.depth()));
    return new BlockPos(area.min().x() + x, area.min().y() + y, area.min().z() + z);
  }
}
