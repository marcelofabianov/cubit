package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.behavior.MarkLife;
import io.github.marcelofabianov.cubit.domain.behavior.ScanArea;
import io.github.marcelofabianov.cubit.network.FocusMarkPayload;
import io.github.marcelofabianov.cubit.network.MobMarksPayload;
import io.github.marcelofabianov.cubit.network.OreMarksPayload;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.Tags;

/**
 * The boxes the server asked this client to draw. The client never decides what is found: it only
 * drops a box when its time ends, the ore is gone or the mob left the range (answer 613).
 */
final class ScanMarks {
  private static final Map<BlockPos, MarkLife> ORES = new HashMap<>();
  private static MobMarks mobs;
  private static FocusMarkPayload focus;
  private static ClientLevel shownIn;

  private ScanMarks() {}

  static void showOres(OreMarksPayload marks) {
    ClientLevel level = currentLevel();
    if (level == null) {
      return;
    }
    MarkLife life = MarkLife.from(level.getGameTime(), marks.holdTicks());
    marks.positions().forEach(pos -> ORES.put(pos, life));
  }

  static void showMobs(MobMarksPayload marks) {
    ClientLevel level = currentLevel();
    if (level == null) {
      return;
    }
    mobs =
        new MobMarks(
            marks.robotId(),
            new ScanArea(Math.max(marks.radius(), 1)),
            marks.mobIds(),
            MarkLife.from(level.getGameTime(), Math.max(marks.holdTicks(), 0)));
  }

  static void showFocus(FocusMarkPayload mark) {
    if (currentLevel() != null) {
      focus = mark.mobId() == FocusMarkPayload.NO_MOB ? null : mark;
    }
  }

  /** The target of Focus while it is alive. The server clears it when the target ends. */
  static Entity focusTarget(ClientLevel level) {
    if (focus == null) {
      return null;
    }
    Entity mob = level.getEntity(focus.mobId());
    return mob != null && mob.isAlive() ? mob : null;
  }

  /** The ores still worth a box. A block in an unloaded chunk is never read, so it is dropped. */
  static List<BlockPos> ores(ClientLevel level) {
    long now = level.getGameTime();
    ORES.entrySet()
        .removeIf(mark ->
            !mark.getValue().lastsAt(now)
                || !level.hasChunkAt(mark.getKey())
                || !level.getBlockState(mark.getKey()).is(Tags.Blocks.ORES));
    return List.copyOf(ORES.keySet());
  }

  /** The mobs still worth a box: alive and within the range of the robot that found them. */
  static List<Entity> mobs(ClientLevel level) {
    if (mobs == null || !mobs.life().lastsAt(level.getGameTime())) {
      mobs = null;
      return List.of();
    }
    Entity robot = level.getEntity(mobs.robotId());
    List<Entity> shown = new ArrayList<>();
    for (int id : mobs.ids()) {
      Entity mob = level.getEntity(id);
      if (robot != null
          && mob != null
          && (focus == null || focus.mobId() != id)
          && mob.isAlive()
          && mobs.area().holdsDistance(robot.distanceToSqr(mob))) {
        shown.add(mob);
      }
    }
    return shown;
  }

  /** Boxes belong to one level, so a new world or dimension starts with none. */
  static ClientLevel currentLevel() {
    ClientLevel level = Minecraft.getInstance().level;
    if (level != shownIn) {
      ORES.clear();
      mobs = null;
      focus = null;
      shownIn = level;
    }
    return level;
  }

  private record MobMarks(int robotId, ScanArea area, List<Integer> ids, MarkLife life) {}
}
