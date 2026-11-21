package io.github.zenndg.cubit.config;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.domain.desk.DeskSpec;
import io.github.zenndg.cubit.domain.energy.Energy;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.slf4j.Logger;

/**
 * The Engineering Desk at one tier, in the section of the tier (answer 1475). The key names are a
 * contract with the pack.
 */
final class DeskKeys {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String CAPACITY = "engineering_desk_energy_capacity";
  private static final String CRAFT_TICKS = "engineering_desk_craft_ticks";
  private static final String SLOTS = "engineering_desk_storage_slots";
  private static final String MAX_ORDER = "engineering_desk_max_order";
  private static final int MIN_POSITIVE = 1;
  private static final int MAX_ENERGY = Integer.MAX_VALUE;
  private static final int MAX_CRAFT_TICKS = 72_000;
  private static final int MAX_ORDER_LIMIT = 4_096;
  private static final String NOT_A_ROW = "engineering_desk_storage_slots {} is off, using {}.";

  private final ModConfigSpec.IntValue capacity;
  private final ModConfigSpec.IntValue craftTicks;
  private final ModConfigSpec.IntValue slots;
  private final ModConfigSpec.IntValue maxOrder;

  DeskKeys(ModConfigSpec.Builder builder, DeskSpec defaults) {
    capacity =
        builder.defineInRange(
            CAPACITY, (int) defaults.capacity().amount(), MIN_POSITIVE, MAX_ENERGY);
    craftTicks =
        builder.defineInRange(CRAFT_TICKS, defaults.craftTicks(), MIN_POSITIVE, MAX_CRAFT_TICKS);
    slots = builder.defineInRange(SLOTS, defaults.storageSlots(), DeskSpec.ROW, DeskSpec.MAX_SLOTS);
    maxOrder = builder.defineInRange(MAX_ORDER, defaults.maxOrder(), MIN_POSITIVE, MAX_ORDER_LIMIT);
  }

  /** Slots that are not whole rows fall to the row below, with a warning (rule 17). */
  DeskSpec read() {
    int configured = slots.get();
    int rows = configured - configured % DeskSpec.ROW;
    if (rows != configured) {
      LOGGER.warn(NOT_A_ROW, configured, rows);
    }
    return new DeskSpec(new Energy(capacity.get()), craftTicks.get(), rows, maxOrder.get());
  }
}
