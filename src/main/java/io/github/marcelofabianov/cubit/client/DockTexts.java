package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyFormat;
import io.github.marcelofabianov.cubit.domain.robot.HealTime;
import io.github.marcelofabianov.cubit.menu.InductionDockMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** The lines of the dock screen that change with its state (telas.md, answer 693). */
final class DockTexts {
  private DockTexts() {}

  static Component amount(Energy value, Energy max) {
    return Component.translatable(
        ScreenKeys.ENERGY_AMOUNT, EnergyFormat.compact(value), EnergyFormat.compact(max));
  }

  /** A full stock takes nothing in, so it says so before the input. */
  static Component input(InductionDockMenu menu) {
    if (menu.stock().amount() >= menu.capacity().amount()) {
      return Component.translatable(ScreenKeys.DOCK_FULL);
    }
    if (menu.input().isZero()) {
      return Component.translatable(ScreenKeys.DOCK_NO_INPUT);
    }
    return Component.translatable(ScreenKeys.DOCK_INPUT, EnergyFormat.rate(menu.input()));
  }

  /** The health comes back even without energy, so every line also tells it. */
  static Component robotLine(InductionDockMenu menu) {
    Component heal = heal(menu.healTime());
    return switch (menu.status()) {
      case COMPLETE -> Component.translatable(ScreenKeys.DOCK_COMPLETE_LINE, heal);
      case NO_ENERGY, OFF -> Component.translatable(ScreenKeys.DOCK_NOT_CHARGING_LINE, heal);
      case CHARGING, IDLE ->
          Component.translatable(
              ScreenKeys.DOCK_CHARGING_LINE, EnergyFormat.rate(menu.robotRecharge()), heal);
    };
  }

  private static Component heal(HealTime time) {
    return switch (time.kind()) {
      case FULL -> Component.translatable(ScreenKeys.DOCK_HEAL_FULL);
      case UNDER_A_MINUTE -> Component.translatable(ScreenKeys.DOCK_HEAL_UNDER_A_MINUTE);
      case MINUTES -> Component.translatable(ScreenKeys.DOCK_HEAL_MINUTES, time.minutes());
    };
  }

  static MutableComponent queue(int waiting) {
    if (waiting <= 0) {
      return Component.translatable(ScreenKeys.DOCK_QUEUE_EMPTY);
    }
    return waiting == 1
        ? Component.translatable(ScreenKeys.DOCK_QUEUE_ONE)
        : Component.translatable(ScreenKeys.DOCK_QUEUE_MANY, waiting);
  }

  static MutableComponent owner(InductionDockMenu menu) {
    return menu.ownerName()
        .map(Component::literal)
        .orElseGet(() -> Component.translatable(ScreenKeys.DOCK_OWNER_UNKNOWN));
  }
}
