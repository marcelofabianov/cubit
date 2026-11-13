package io.github.marcelofabianov.cubit.config;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * The magnet, the pickup filter and the fetch of one tier. The key names are a contract (answers
 * 889 and 943).
 */
final class PickupKeys {
  private static final String MAGNET_RADIUS = "magnet_radius_blocks";
  private static final String MAGNET_COST = "magnet_cost_per_item";
  private static final String FILTER_SLOTS = "pickup_filter_slots";
  private static final String FILTER_TAGS = "pickup_filter_tags";
  private static final String FETCH_RADIUS = "fetch_radius_blocks";
  private static final int MIN_ZERO = 0;
  private static final int MIN_SLOTS = 1;
  private static final int MAX_RADIUS = 16;
  private static final int MAX_FILTER_SLOTS = 27;
  private static final int MAX_ENERGY = Integer.MAX_VALUE;

  private final ModConfigSpec.IntValue magnetRadius;
  private final ModConfigSpec.IntValue magnetCost;
  private final ModConfigSpec.IntValue filterSlots;
  private final ModConfigSpec.BooleanValue filterTags;
  private final ModConfigSpec.IntValue fetchRadius;

  PickupKeys(ModConfigSpec.Builder builder, PickupSpec defaults) {
    magnetRadius =
        builder.defineInRange(MAGNET_RADIUS, defaults.magnetRadiusBlocks(), MIN_ZERO, MAX_RADIUS);
    magnetCost =
        builder.defineInRange(
            MAGNET_COST,
            Math.toIntExact(defaults.magnetCostPerItem().amount()),
            MIN_ZERO,
            MAX_ENERGY);
    filterSlots =
        builder.defineInRange(FILTER_SLOTS, defaults.filterSlots(), MIN_SLOTS, MAX_FILTER_SLOTS);
    filterTags = builder.define(FILTER_TAGS, defaults.filterTags());
    fetchRadius =
        builder.defineInRange(FETCH_RADIUS, defaults.fetchRadiusBlocks(), MIN_ZERO, MAX_RADIUS);
  }

  PickupSpec read() {
    return new PickupSpec(
        magnetRadius.get(),
        new Energy(magnetCost.get()),
        filterSlots.get(),
        filterTags.get(),
        fetchRadius.get());
  }
}
