package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.domain.behavior.HibernationReason;
import io.github.zenndg.cubit.domain.behavior.RobotScreenStatus;
import io.github.zenndg.cubit.domain.energy.EnergyFormat;
import io.github.zenndg.cubit.domain.energy.EnergyReading;
import io.github.zenndg.cubit.domain.energy.EnergyUseCause;
import io.github.zenndg.cubit.domain.energy.SolarStatus;
import io.github.zenndg.cubit.domain.robot.OreKind;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.entity.OreNames;
import io.github.zenndg.cubit.menu.RobotMenu;
import io.github.zenndg.cubit.menu.ScreenKeys;
import io.github.zenndg.cubit.menu.WorkKeys;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/** The lines of the robot screen that change with its state (telas.md, answer 693). */
final class RobotTexts {
  private static final String CAUSE_SEPARATOR = ", ";

  private RobotTexts() {}

  static Component status(RobotMenu menu) {
    return status(menu.status(), menu.job(), menu.hibernationReason());
  }

  /**
   * The text of a state, with the job while working and the reason while hibernating away, as the
   * top of the screens and the list of the Tablet show it (answer 1168).
   */
  static Component status(
      RobotScreenStatus status, Optional<RobotJob> job, Optional<HibernationReason> reason) {
    String key = ScreenKeys.robotStatus(status);
    return switch (status) {
      case WORKING -> Component.translatable(key, jobName(job));
      case HIBERNATING_AWAY ->
          Component.translatable(
              key,
              Component.translatable(
                  WorkKeys.reason(reason.orElse(HibernationReason.INVENTORY_FULL))));
      default -> Component.translatable(key);
    };
  }

  static Component jobName(Optional<RobotJob> job) {
    return job.map(found -> (Component) Component.translatable(WorkKeys.job(found)))
        .orElseGet(() -> Component.translatable(WorkKeys.NO_JOB));
  }

  static int statusColor(RobotScreenStatus status) {
    return switch (status) {
      case CRITICAL_BATTERY -> ScreenPainter.STATUS_RED;
      case FOLLOWING -> ScreenPainter.STATUS_GREEN;
      case HIBERNATING, HIBERNATING_AWAY -> ScreenPainter.STATUS_BLUE;
      case IN_AREA, WORKING -> ScreenPainter.STATUS_PURPLE;
      case INVENTORY_FULL -> ScreenPainter.STATUS_ORANGE;
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

  static Component solarDetail(RobotMenu menu) {
    return Component.translatable(ScreenKeys.SOLAR_DETAIL, menu.opening().solarDay());
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
      case FULL, OFF, SWITCHED_OFF -> ScreenPainter.STATUS_GRAY;
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
