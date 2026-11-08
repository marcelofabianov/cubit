package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.energy.RechargeSource;
import io.github.marcelofabianov.cubit.domain.energy.RechargeSource.RechargeView;
import io.github.marcelofabianov.cubit.domain.energy.SolarOutput;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus.SolarView;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import io.github.marcelofabianov.cubit.domain.tier.ScannerSpec;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.RobotScreenOpening;
import io.github.marcelofabianov.cubit.menu.RobotScreenReading;
import io.github.marcelofabianov.cubit.menu.RobotScreenReading.Vitals;
import io.github.marcelofabianov.cubit.menu.RobotSwitch;
import net.minecraft.world.entity.player.Inventory;

/** Reads a robot for its screen. Only reads, and the rules come from the domain. */
public final class RobotScreenReadout {
  private static final String NO_ICON = "";

  private RobotScreenReadout() {}

  /** The menu of the robot on the server, for its owner, as the click and the tests open it. */
  public static RobotMenu serverMenu(int containerId, Inventory inventory, RobotEntity robot) {
    RobotMenu.Contents contents =
        new RobotMenu.Contents(robot.inventory(), robot.energySlot(), opening(robot));
    RobotSwitch[] switches = {robot.lampSwitch(), robot.scanners(), robot.chatter()};
    return RobotMenu.onServer(
        containerId,
        inventory,
        contents,
        new RobotMenu.RobotSource(() -> read(robot), switches, robot::canUseInventory));
  }

  static RobotScreenReading read(RobotEntity robot) {
    RobotTickReading tick = RobotTickReading.of(robot);
    Battery battery = robot.battery();
    PowerState state = robot.powerState();
    boolean following = RobotInteractions.followedPlayer(robot) != null;
    RechargeSource recharge = recharge(tick, state, battery);
    SolarStatus solar =
        SolarStatus.of(
            new SolarView(state, tick.sunExists(), tick.skyOpen(), tick.day()),
            battery,
            robot.spec().solar());
    return new RobotScreenReading(
        battery,
        new Vitals(robot.getHealth(), robot.getMaxHealth(), following, RobotFace.expression(robot)),
        RobotScreenStatus.of(state, following, battery, CubitMod.CONFIG.criticalCharge()),
        recharge,
        recharge == RechargeSource.SOLAR ? tick.solarCharged() : robot.spec().rechargePerTick(),
        solar,
        solarRate(solar, tick.day(), robot.spec().solar()),
        tick.spent(),
        EnergyUseCause.of(tick.activity()),
        robot.state().tier());
  }

  private static RechargeSource recharge(RobotTickReading tick, PowerState state, Battery battery) {
    RechargeView view =
        new RechargeView(tick.dockCharged(), tick.slotCharged(), !tick.solarCharged().isZero());
    return RechargeSource.of(state, view, battery);
  }

  /** The rate the panel gives now, by day or by night, as the screen says "Charging · 5 FE/t". */
  private static Energy solarRate(SolarStatus solar, boolean day, SolarOutput output) {
    if (solar != SolarStatus.CHARGING) {
      return Energy.ZERO;
    }
    return day ? output.dayPerTick() : output.nightPerTick();
  }

  static RobotScreenOpening opening(RobotEntity robot) {
    TierSpec spec = robot.spec();
    ScannerSpec scanners = spec.scanners();
    OreFilter filter = robot.scanners().oreFilter();
    return new RobotScreenOpening(
        robot.inventory().getContainerSize(),
        CubitMod.CONFIG.lampPolicy().onBelow().value(),
        spec.extras().lampPerTick().amount(),
        scanners.oreRadiusBlocks(),
        scanners.mobRadiusBlocks(),
        scanners.oreCost().amount(),
        filter.kinds().stream().map(OreKind::tag).toList(),
        filter.kinds().stream().findFirst().map(OreKind::tag).orElse(NO_ICON));
  }
}
