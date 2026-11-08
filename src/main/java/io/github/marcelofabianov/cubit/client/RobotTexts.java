package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.EnergyFormat;
import io.github.marcelofabianov.cubit.domain.energy.EnergyReading;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus;
import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import io.github.marcelofabianov.cubit.entity.OreNames;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** The lines of the robot screen that change with its state (telas.md, answer 693). */
final class RobotTexts {
  private static final String CAUSE_SEPARATOR = ", ";

  private RobotTexts() {}

  static Component status(RobotMenu menu) {
    return Component.translatable(ScreenKeys.robotStatus(menu.status()));
  }

  static int statusColor(RobotScreenStatus status) {
    return switch (status) {
      case CRITICAL_BATTERY -> ScreenPainter.STATUS_RED;
      case FOLLOWING -> ScreenPainter.STATUS_GREEN;
      case HIBERNATING -> ScreenPainter.STATUS_BLUE;
      case ACTIVE, OFF -> ScreenPainter.STATUS_GRAY;
    };
  }

  static Component recharge(RobotMenu menu) {
    return Component.translatable(
        ScreenKeys.recharge(menu.recharge()), EnergyFormat.rate(menu.rechargeRate()));
  }

  static Component batteryPercent(RobotMenu menu) {
    int percent = EnergyReading.capped(menu.charge().amount(), menu.capacity().amount()).percent();
    return Component.translatable(ScreenKeys.SETTINGS_BATTERY, percent);
  }

  static Component lampDetail(RobotMenu menu) {
    return Component.translatable(
        ScreenKeys.LAMP_DETAIL, menu.opening().lampOnBelow(), menu.opening().lampCost());
  }

  static Component scannersDetail(RobotMenu menu) {
    return Component.translatable(
        ScreenKeys.SCANNERS_DETAIL,
        menu.opening().oreRadius(),
        menu.opening().mobRadius(),
        menu.opening().scanCost());
  }

  static Component filter(RobotMenu menu) {
    if (!menu.opening().hasFilter()) {
      return Component.translatable(ScreenKeys.NO_FILTER);
    }
    MutableComponent names = Component.empty();
    for (String tag : menu.opening().filterTags()) {
      OreKind.parse(tag)
          .ifPresent(kind -> {
            if (!names.getSiblings().isEmpty()) {
              names.append(CAUSE_SEPARATOR);
            }
            names.append(OreNames.of(kind));
          });
    }
    return Component.translatable(ScreenKeys.LOOKING_FOR, names);
  }

  static Component solar(RobotMenu menu) {
    return Component.translatable(
        ScreenKeys.solar(menu.solar()), EnergyFormat.rate(menu.solarRate()));
  }

  static int solarColor(SolarStatus status) {
    return switch (status) {
      case CHARGING -> ScreenPainter.STATUS_GREEN;
      case NO_SKY, NIGHT, NO_SUN -> ScreenPainter.STATUS_ORANGE;
      case FULL, OFF -> ScreenPainter.STATUS_GRAY;
    };
  }

  /** In the base it spends nothing, and without a cause the robot is idle. */
  static Component energyUse(RobotMenu menu) {
    String use = EnergyFormat.rate(menu.use());
    if (menu.status() == RobotScreenStatus.HIBERNATING) {
      return Component.translatable(
          ScreenKeys.ENERGY_USE_VALUE, use, Component.translatable(ScreenKeys.HIBERNATING));
    }
    List<EnergyUseCause> causes = menu.causes();
    if (causes.isEmpty()) {
      return Component.translatable(
          ScreenKeys.ENERGY_USE_VALUE, use, Component.translatable(ScreenKeys.IDLE));
    }
    MutableComponent listed = Component.empty();
    for (int index = 0; index < causes.size(); index++) {
      if (index > 0) {
        listed.append(CAUSE_SEPARATOR);
      }
      listed.append(Component.translatable(ScreenKeys.cause(causes.get(index))));
    }
    return Component.translatable(ScreenKeys.ENERGY_USE_VALUE, use, listed);
  }

  static Component health(RobotMenu menu) {
    if (menu.health() >= menu.maxHealth()) {
      return Component.translatable(ScreenKeys.HEALTH_AMOUNT, menu.health(), menu.maxHealth());
    }
    String key =
        menu.status() == RobotScreenStatus.HIBERNATING
            ? ScreenKeys.HEALTH_RECOVERING
            : ScreenKeys.HEALTH_RECOVERS;
    return Component.translatable(key, menu.health(), menu.maxHealth());
  }

  static Component following(RobotMenu menu) {
    return Component.translatable(
        menu.following() ? ScreenKeys.FOLLOWING_YOU : ScreenKeys.FOLLOWING_NOBODY);
  }
}
