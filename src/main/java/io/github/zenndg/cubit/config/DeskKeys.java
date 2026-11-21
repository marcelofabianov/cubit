package io.github.zenndg.cubit.config;

import io.github.zenndg.cubit.domain.desk.DeskSpec;
import io.github.zenndg.cubit.domain.energy.Energy;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The Engineering Desk at one tier, in the section of the tier (answer 1475). The key names are a
 * contract with the pack.
 */
final class DeskKeys {
  private static final String CAPACITY = "engineering_desk_energy_capacity";
  private static final String CRAFT_TICKS = "engineering_desk_craft_ticks";
  private static final String SLOTS = "engineering_desk_storage_slots";
  private static final String MAX_ORDER = "engineering_desk_max_order";
  private static final int MIN_POSITIVE = 1;
  private static final int MAX_ENERGY = Integer.MAX_VALUE;
  private static final int MAX_CRAFT_TICKS = 72_000;
  private static final int MAX_ORDER_LIMIT = 4_096;

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
    slots = builder.defineInRange(SLOTS, defaults.storageSlots(), MIN_POSITIVE, DeskSpec.MAX_SLOTS);
    maxOrder = builder.defineInRange(MAX_ORDER, defaults.maxOrder(), MIN_POSITIVE, MAX_ORDER_LIMIT);
  }

  DeskSpec read() {
    return new DeskSpec(new Energy(capacity.get()), craftTicks.get(), slots.get(), maxOrder.get());
  }
}
