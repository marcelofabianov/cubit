package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.dock.DockStock;
import io.github.zenndg.cubit.domain.dock.DockSun;
import io.github.zenndg.cubit.domain.dock.DockSunState;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.SolarPanel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

/**
 * The panel on the tower of a Solar Dock. Under the sun it puts its rate into the stock, without
 * passing it and apart from the input of the cables, and otherwise the dock is a normal one
 * (answers 1384 and 1385). What it gave last shows on the screen and in Jade.
 */
final class DockSolarPanel {
  /** The panel sits on the upper half, so the sky is read above it. */
  private static final int ABOVE_THE_PANEL = 2;

  private static final String SUN_KEY = "Sun";

  private Energy given = Energy.ZERO;
  private DockSunState state = DockSunState.NO_SUN;
  private boolean read;
  private boolean skyOpen;
  private boolean sunExists;

  /** On the client, whether the panel is generating, for the pulse of the cell (1392). */
  private boolean sunlit;

  Energy given() {
    return given;
  }

  /** What the panel is doing and why it stops, for the Solar tab (answer 1396). */
  DockSunState state() {
    return state;
  }

  boolean sunlit() {
    return sunlit;
  }

  /** True when the panel started or stopped generating, so the client hears of it once. */
  boolean switched() {
    boolean now = !given.isZero();
    boolean changed = now != sunlit;
    sunlit = now;
    return changed;
  }

  void writeSun(CompoundTag tag) {
    tag.putBoolean(SUN_KEY, sunlit);
  }

  void readSun(CompoundTag tag) {
    sunlit = tag.getBoolean(SUN_KEY);
  }

  DockStock generate(Level level, BlockPos lower, DockStock stock, Energy rate, Runnable changed) {
    given = Energy.ZERO;
    if (!read || level.getGameTime() % CubitMod.CONFIG.solarCheckTicks() == 0) {
      readSky(level, lower);
    }
    boolean day = SolarPanel.isDay(level.getDayTime());
    state = new DockSun(day, skyOpen, level.isRaining() || level.isThundering(), sunExists).state();
    if (rate.isZero() || state != DockSunState.GENERATING) {
      return stock;
    }
    DockStock.Received received = stock.receive(rate, rate);
    given = received.accepted();
    if (!given.isZero()) {
      changed.run();
    }
    return received.stock();
  }

  /**
   * As the panel of the Cubit: light that passes through glass still counts, an opaque block over
   * the panel stops it, and the Nether and the End have no sun. Only this waits for the interval of
   * the Cubit (rule 21).
   */
  private void readSky(Level level, BlockPos lower) {
    read = true;
    skyOpen = level.canSeeSky(lower.above(ABOVE_THE_PANEL));
    sunExists = level.dimensionType().hasSkyLight() && !level.dimensionType().hasCeiling();
  }
}
