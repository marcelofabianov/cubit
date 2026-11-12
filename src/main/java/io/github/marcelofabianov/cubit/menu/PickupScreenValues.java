package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyHalves;
import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.domain.robot.PickupMode;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;

/**
 * The numbers of the behavior tab, as the menu sends them. The tag of each filter slot travels as
 * its position in the sorted tags of the shown item plus one, and zero is a plain item (answer
 * 895).
 */
public final class PickupScreenValues implements ContainerData {
  public static final int MAX_FILTER_SLOTS = 27;
  private static final int PICKUP_ON = 0;
  private static final int MAGNET_ON = 1;
  private static final int MAGNET_UNLOCKED = 2;
  private static final int MODE = 3;
  private static final int FILTER_SLOTS = 4;
  private static final int FILTER_TAGS = 5;
  private static final int RADIUS = 6;
  private static final int COST = 7;
  private static final int UNLOCK_TIER = 9;
  private static final int FIRST_TAG = 10;
  private static final int COUNT = FIRST_TAG + MAX_FILTER_SLOTS;
  private static final int TRUE_VALUE = 1;
  private static final int PLAIN_ITEM = 0;

  private final int[] values = new int[COUNT];

  void refresh(PickupControl control) {
    PickupSettings settings = control.settings();
    PickupSpec spec = control.spec();
    values[PICKUP_ON] = flag(settings.pickupOn());
    values[MAGNET_ON] = flag(settings.magnetOn());
    values[MAGNET_UNLOCKED] = flag(spec.magnetUnlocked());
    values[MODE] = settings.filter().mode().ordinal();
    values[FILTER_SLOTS] = Math.min(spec.filterSlots(), MAX_FILTER_SLOTS);
    values[FILTER_TAGS] = flag(spec.filterTags());
    values[RADIUS] = spec.magnetRadiusBlocks();
    values[COST] = EnergyHalves.low(spec.magnetCostPerItem());
    values[COST + 1] = EnergyHalves.high(spec.magnetCostPerItem());
    values[UNLOCK_TIER] = control.magnetUnlockTier().map(tier -> tier.ordinal() + 1).orElse(0);
    List<PickupEntry> entries = settings.filter().entries();
    for (int slot = 0; slot < MAX_FILTER_SLOTS; slot++) {
      values[FIRST_TAG + slot] = slot < entries.size() ? tagValue(entries.get(slot)) : PLAIN_ITEM;
    }
  }

  private static int tagValue(PickupEntry entry) {
    if (!entry.tag()) {
      return PLAIN_ITEM;
    }
    return PickupDisplay.sortedTags(PickupDisplay.stackOf(entry)).indexOf(entry.id()) + 1;
  }

  private static int flag(boolean value) {
    return value ? TRUE_VALUE : 0;
  }

  public static boolean pickupOn(ContainerData data) {
    return data.get(PICKUP_ON) == TRUE_VALUE;
  }

  public static boolean magnetOn(ContainerData data) {
    return data.get(MAGNET_ON) == TRUE_VALUE;
  }

  public static boolean magnetUnlocked(ContainerData data) {
    return data.get(MAGNET_UNLOCKED) == TRUE_VALUE;
  }

  /** The magnet can change only on a tier with it and with pickup on (answers 876 and 881). */
  public static boolean magnetAvailable(ContainerData data) {
    return magnetUnlocked(data) && pickupOn(data);
  }

  public static PickupMode mode(ContainerData data) {
    return ScreenEnums.of(PickupMode.values(), data.get(MODE));
  }

  public static int filterSlots(ContainerData data) {
    return data.get(FILTER_SLOTS);
  }

  public static boolean filterTags(ContainerData data) {
    return data.get(FILTER_TAGS) == TRUE_VALUE;
  }

  public static int radius(ContainerData data) {
    return data.get(RADIUS);
  }

  public static Energy cost(ContainerData data) {
    return EnergyHalves.join(data.get(COST), data.get(COST + 1));
  }

  public static Optional<RobotTier> magnetUnlockTier(ContainerData data) {
    int value = data.get(UNLOCK_TIER);
    return value == 0
        ? Optional.empty()
        : Optional.of(ScreenEnums.of(RobotTier.values(), value - 1));
  }

  public static Optional<String> tagAt(ContainerData data, int slot, ItemStack shown) {
    int value = data.get(FIRST_TAG + slot);
    return value == PLAIN_ITEM ? Optional.empty() : PickupDisplay.tagAt(shown, value - 1);
  }

  @Override
  public int get(int index) {
    return values[index];
  }

  @Override
  public void set(int index, int value) {
    values[index] = value;
  }

  @Override
  public int getCount() {
    return COUNT;
  }
}
