package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.domain.tier.TierUpgrade;
import io.github.marcelofabianov.cubit.item.ModuleItem;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** What the owner does to a robot with a module. The rules live in the domain. */
public final class RobotUpgrade {
  public static final String UPGRADED_KEY = "notice.cubit.upgraded";
  public static final String NEEDS_TIER_KEY = "notice.cubit.needs_tier";
  public static final String ALREADY_TIER_KEY = "notice.cubit.already_tier";

  private RobotUpgrade() {}

  /** The click is always used, so a module that does not fit does not open the inventory. */
  static InteractionResult apply(
      RobotEntity robot, Player player, ItemStack stack, ModuleItem module) {
    RobotTier current = robot.state().tier();
    TierUpgrade upgrade = TierUpgrade.of(current, module.tier());
    if (upgrade.consumesModule()) {
      upgradeTo(robot, player, module.tier());
      stack.consume(1, player);
      RobotPaint.react(robot);
    }
    if (player instanceof ServerPlayer serverPlayer) {
      CubitNetwork.sendNotice(serverPlayer, answer(upgrade, current, module.tier()));
    }
    return InteractionResult.CONSUME;
  }

  /**
   * The charge and the current health stay, and the new numbers apply from now on (answer 93). An
   * open screen closes first, so it never holds the items of the smaller inventory.
   */
  private static void upgradeTo(RobotEntity robot, Player player, RobotTier tier) {
    if (player.containerMenu != player.inventoryMenu) {
      player.closeContainer();
    }
    robot.setTier(tier);
    TierSpec spec = robot.spec();
    robot.setBattery(robot.state().batteryFor(spec));
    RobotInteractions.applyAttributes(robot);
    robot.robotInventory().resize(spec.inventorySlots());
    RobotLooks.update(robot);
  }

  private static Component answer(TierUpgrade upgrade, RobotTier current, RobotTier target) {
    Component text =
        switch (upgrade) {
          case UPGRADES -> Component.translatable(UPGRADED_KEY, TierLook.name(target));
          case NEEDS_PREVIOUS_TIER ->
              Component.translatable(
                  NEEDS_TIER_KEY, TierLook.name(target.previous().orElseThrow()));
          case ALREADY_THERE -> Component.translatable(ALREADY_TIER_KEY, TierLook.name(current));
        };
    return Component.translatable(RobotNotices.FORMAT_KEY, text);
  }
}
