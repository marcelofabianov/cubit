package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.domain.tier.TierUpgrade;
import io.github.zenndg.cubit.item.ModuleItem;
import io.github.zenndg.cubit.item.TierLook;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * What the owner does to a desk with a Module, as in the dock: the Module of the next tier raises
 * it and is used up, and one that does not fit stays in the hand with a reason (answer 1462).
 */
public final class DeskUpgrade {
  public static final String UPGRADED_KEY = "notice.cubit.desk_upgraded";
  public static final String NEEDS_TIER_KEY = "notice.cubit.desk_needs_tier";
  public static final String ALREADY_TIER_KEY = "notice.cubit.desk_already_tier";

  private DeskUpgrade() {}

  static void apply(EngineeringDeskBlockEntity desk, Player player, ItemStack stack) {
    ModuleItem module = (ModuleItem) stack.getItem();
    RobotTier current = desk.tier();
    TierUpgrade upgrade = TierUpgrade.of(current, module.tier());
    if (upgrade.consumesModule()) {
      if (player.containerMenu != player.inventoryMenu) {
        player.closeContainer();
      }
      desk.upgradeTo(module.tier());
      stack.consume(1, player);
    }
    player.displayClientMessage(answer(upgrade, current, module.tier()), true);
  }

  private static Component answer(TierUpgrade upgrade, RobotTier current, RobotTier target) {
    return switch (upgrade) {
      case UPGRADES -> Component.translatable(UPGRADED_KEY, TierLook.name(target));
      case NEEDS_PREVIOUS_TIER ->
          Component.translatable(NEEDS_TIER_KEY, TierLook.name(target.previous().orElseThrow()));
      case ALREADY_THERE -> Component.translatable(ALREADY_TIER_KEY, TierLook.name(current));
    };
  }
}
