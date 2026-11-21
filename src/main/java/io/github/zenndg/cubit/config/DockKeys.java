package io.github.zenndg.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.domain.dock.DockSpec;
import io.github.zenndg.cubit.domain.dock.DockStorageRules;
import io.github.zenndg.cubit.domain.energy.Energy;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

/**
 * The Induction Dock at one tier, in the section of the tier (answer 1381). The key names are a
 * contract with the pack.
 */
final class DockKeys {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String CAPACITY = "dock_energy_capacity";
  private static final String RECHARGE = "dock_recharge_per_tick";
  private static final String SLOTS = "dock_storage_slots";
  private static final int MIN_POSITIVE = 1;
  private static final int MAX_ENERGY = Integer.MAX_VALUE;
  private static final String NOT_A_ROW = "dock_storage_slots {} is not whole rows of 9, using {}.";

  private final ModConfigSpec.IntValue capacity;
  private final ModConfigSpec.IntValue recharge;
  private final ModConfigSpec.IntValue slots;

  DockKeys(ModConfigSpec.Builder builder, DockSpec defaults) {
    capacity =
        builder.defineInRange(
            CAPACITY, (int) defaults.capacity().amount(), MIN_POSITIVE, MAX_ENERGY);
    recharge =
        builder.defineInRange(
            RECHARGE, (int) defaults.rechargePerTick().amount(), MIN_POSITIVE, MAX_ENERGY);
    slots =
        builder.defineInRange(
            SLOTS, defaults.storageSlots(), DockSpec.ROW, DockStorageRules.MAX_SLOTS);
  }

  /** Slots that are not whole rows fall to the row below, with a warning (rule 17). */
  DockSpec read() {
    int configured = slots.get();
    int rows = configured - configured % DockSpec.ROW;
    if (rows != configured) {
      LOGGER.warn(NOT_A_ROW, configured, rows);
    }
    return new DockSpec(new Energy(capacity.get()), new Energy(recharge.get()), rows);
  }
}
