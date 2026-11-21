package io.github.zenndg.cubit.item;

import io.github.zenndg.cubit.block.SavedDeskOrder;
import io.github.zenndg.cubit.domain.desk.DeskOrder;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.EnergyFormat;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/**
 * The tooltip of a collected desk: its tier, its energy, its order and how many items it keeps
 * (answer 1476).
 */
final class EngineeringDeskTooltip {
  private EngineeringDeskTooltip() {}

  static void append(ItemStack stack, List<Component> lines) {
    RobotTier tier = stack.getOrDefault(CubitDataComponents.DESK_TIER.get(), RobotTier.BASE);
    lines.add(Component.translatable(EngineeringDeskItem.TIER_KEY, TierLook.name(tier)));
    long energy = stack.getOrDefault(CubitDataComponents.DESK_ENERGY.get(), 0L);
    lines.add(
        Component.translatable(
                EngineeringDeskItem.ENERGY_KEY, EnergyFormat.compact(new Energy(energy)))
            .withStyle(ChatFormatting.GRAY));
    SavedDeskOrder saved = stack.get(CubitDataComponents.DESK_ORDER.get());
    if (saved != null) {
      DeskOrder order = saved.order();
      lines.add(
          Component.translatable(EngineeringDeskItem.ORDER_KEY, order.done(), order.total())
              .withStyle(ChatFormatting.GRAY));
    }
    int kept =
        EngineeringDeskItem.count(stack.get(CubitDataComponents.DESK_INPUT.get()))
            + EngineeringDeskItem.count(stack.get(CubitDataComponents.DESK_OUTPUT.get()));
    if (kept > 0) {
      lines.add(
          Component.translatable(EngineeringDeskItem.ITEMS_KEY, kept)
              .withStyle(ChatFormatting.GRAY));
    }
  }
}
