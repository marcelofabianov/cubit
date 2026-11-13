package io.github.marcelofabianov.cubit.item;

import com.mojang.logging.LogUtils;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyFormat;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.slf4j.Logger;

/**
 * A portable battery of one tier. It only takes energy in the slot of the Induction Dock and only
 * gives it to a Cubit, so it has no FE capability for other mods (answers 903 to 909).
 */
public final class PowerBankItem extends Item {
  public static final String CHARGES_AT_DOCK_KEY = "item.cubit.power_bank.charges_at_dock";
  public static final String AMOUNT_KEY = "item.cubit.power_bank.amount";
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String OVER_CAPACITY_MESSAGE = "A Power Bank held {} FE above its {} FE.";
  private static final int BAR_STEPS = 13;
  private static final int BAR_COLOR = 0x4CAF50;

  private final RobotTier tier;

  public PowerBankItem(Properties properties, RobotTier tier) {
    super(properties);
    this.tier = tier;
  }

  public RobotTier tier() {
    return tier;
  }

  public static Energy capacityOf(RobotTier tier) {
    return CubitMod.CONFIG.tierCatalog().spec(CubitConfig.GENERAL_ROBOT, tier).powerBankCapacity();
  }

  /** The saved charge fitted to the capacity of today, which the config may have lowered. */
  public static Battery batteryOf(ItemStack stack) {
    Energy capacity = capacityOf(((PowerBankItem) stack.getItem()).tier);
    Energy saved =
        SavedEnergy.of(stack.getOrDefault(CubitDataComponents.POWER_BANK_ENERGY.get(), 0L));
    if (saved.amount() > capacity.amount()) {
      LOGGER.warn(OVER_CAPACITY_MESSAGE, saved.amount(), capacity.amount());
      return new Battery(capacity, capacity);
    }
    return new Battery(capacity, saved);
  }

  public static void store(ItemStack stack, Battery battery) {
    stack.set(CubitDataComponents.POWER_BANK_ENERGY.get(), battery.charge().amount());
  }

  public static boolean is(ItemStack stack) {
    return stack.getItem() instanceof PowerBankItem;
  }

  /** In the color of the collar of the tier, as the Modules (answer 909). */
  @Override
  public Component getName(ItemStack stack) {
    return Component.translatable(getDescriptionId(stack)).withColor(TierLook.color(tier));
  }

  @Override
  public void appendHoverText(
      ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
    Battery battery = batteryOf(stack);
    lines.add(
        Component.translatable(
                AMOUNT_KEY,
                EnergyFormat.compact(battery.charge()),
                EnergyFormat.compact(battery.capacity()))
            .withStyle(ChatFormatting.GREEN));
    lines.add(Component.translatable(CHARGES_AT_DOCK_KEY).withStyle(ChatFormatting.GRAY));
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    return true;
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    return batteryOf(stack).level(BAR_STEPS);
  }

  @Override
  public int getBarColor(ItemStack stack) {
    return BAR_COLOR;
  }
}
