package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.domain.tier.TierUpgrade;
import io.github.zenndg.cubit.item.ModuleItem;
import io.github.zenndg.cubit.item.TierLook;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * What the owner does to a dock with a Module: the Module of the next tier raises it and is used
 * up, as in the Cubit, and one that does not fit stays in the hand with a reason (1377 and 1378).
 */
public final class DockUpgrade {
  public static final String UPGRADED_KEY = "notice.cubit.dock_upgraded";
  public static final String NEEDS_TIER_KEY = "notice.cubit.dock_needs_tier";
  public static final String ALREADY_TIER_KEY = "notice.cubit.dock_already_tier";

  private DockUpgrade() {}

  static void apply(InductionDockBlockEntity dock, Player player, ItemStack stack) {
    ModuleItem module = (ModuleItem) stack.getItem();
    RobotTier current = dock.tier();
    TierUpgrade upgrade = TierUpgrade.of(current, module.tier());
    if (upgrade.consumesModule()) {
      if (player.containerMenu != player.inventoryMenu) {
        player.closeContainer();
      }
      dock.upgradeTo(module.tier());
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
