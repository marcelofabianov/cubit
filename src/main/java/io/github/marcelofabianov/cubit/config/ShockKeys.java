package io.github.marcelofabianov.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.tier.ShockSpec;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

/** The shock and the defense modes of one tier. The key names are a contract (answer 834). */
final class ShockKeys {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String INVALID_MODE = "Ignoring an invalid name in defense_modes: {}";
  private static final String MODES = "defense_modes";
  private static final String DAMAGE = "shock_damage";
  private static final String RANGE = "shock_range_blocks";
  private static final String INTERVAL = "shock_interval_ticks";
  private static final String COST = "shock_cost";
  private static final String HITS_ALL = "shock_hits_all_in_range";
  private static final String EMPTY_MODE = "";
  private static final int MIN_ZERO = 0;
  private static final int MIN_INTERVAL = 1;
  private static final int MAX_DAMAGE = 1024;
  private static final int MAX_RANGE = 16;
  private static final int MAX_INTERVAL = 72000;
  private static final int MAX_ENERGY = Integer.MAX_VALUE;

  private final ModConfigSpec.ConfigValue<List<? extends String>> modes;
  private final ModConfigSpec.IntValue damage;
  private final ModConfigSpec.IntValue range;
  private final ModConfigSpec.IntValue interval;
  private final ModConfigSpec.IntValue cost;
  private final ModConfigSpec.BooleanValue hitsAll;
  private final AtomicBoolean warnedModes = new AtomicBoolean();

  ShockKeys(ModConfigSpec.Builder builder, ShockSpec defaults) {
    modes =
        builder.defineListAllowEmpty(
            MODES, keysOf(defaults.modes()), () -> EMPTY_MODE, String.class::isInstance);
    damage = builder.defineInRange(DAMAGE, defaults.damage(), MIN_ZERO, MAX_DAMAGE);
    range = builder.defineInRange(RANGE, defaults.rangeBlocks(), MIN_ZERO, MAX_RANGE);
    interval =
        builder.defineInRange(INTERVAL, defaults.intervalTicks(), MIN_INTERVAL, MAX_INTERVAL);
    cost =
        builder.defineInRange(
            COST, Math.toIntExact(defaults.cost().amount()), MIN_ZERO, MAX_ENERGY);
    hitsAll = builder.define(HITS_ALL, defaults.hitsAllInRange());
  }

  ShockSpec read() {
    return new ShockSpec(
        damage.get(), range.get(), interval.get(), new Energy(cost.get()), hitsAll.get(), modes());
  }

  /** An unknown name is skipped and logged once. Peaceful is always added by the domain. */
  private Set<DefenseMode> modes() {
    Set<DefenseMode> allowed = EnumSet.noneOf(DefenseMode.class);
    for (String key : modes.get()) {
      Optional<DefenseMode> mode = DefenseMode.fromKey(key);
      mode.ifPresent(allowed::add);
      if (mode.isEmpty() && warnedModes.compareAndSet(false, true)) {
        LOGGER.warn(INVALID_MODE, key);
      }
    }
    return allowed;
  }

  /** In the order of the modes, so the file reads from the mildest to the strongest. */
  private static List<String> keysOf(Set<DefenseMode> modes) {
    return EnumSet.copyOf(modes).stream().map(DefenseMode::key).toList();
  }
}
