package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

/**
 * Takes a robot to the tier of the module, only from the tier right before it. The robot does the
 * upgrade, because the module is applied with a click on it (answers 226 and 800).
 */
public final class ModuleItem extends Item {
  private static final String TOOLTIP_SUFFIX = ".tooltip";

  private final RobotTier tier;

  public ModuleItem(Properties properties, RobotTier tier) {
    super(properties);
    this.tier = tier;
  }

  public RobotTier tier() {
    return tier;
  }

  /** In the color of the collar of the tier (answer 804). */
  @Override
  public Component getName(ItemStack stack) {
    return Component.translatable(getDescriptionId(stack)).withColor(TierLook.color(tier));
  }

  /** A whole sentence per module, because the article before the tier name changes with it. */
  public String tooltipKey() {
    return getDescriptionId() + TOOLTIP_SUFFIX;
  }

  @Override
  public void appendHoverText(
      ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
    lines.add(Component.translatable(tooltipKey()).withStyle(ChatFormatting.GRAY));
  }
}
