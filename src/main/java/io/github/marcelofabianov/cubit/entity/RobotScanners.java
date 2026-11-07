package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ExpressionTimer;
import io.github.marcelofabianov.cubit.domain.behavior.ScanRules;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import io.github.marcelofabianov.cubit.domain.robot.OreSignals;
import io.github.marcelofabianov.cubit.domain.tier.ScannerSpec;
import io.github.marcelofabianov.cubit.menu.RobotSwitch;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

/**
 * Runs the scanners of the robot that follows, spends on each scan and warns the owner of what is
 * new. The rules live in the domain. Only the switch and the filter are saved.
 */
public final class RobotScanners implements RobotSwitch {
  public static final String ENEMY_KEY = "notice.cubit.enemy";
  public static final String ORE_KEY = "notice.cubit.ore";
  public static final String ORE_FOUND_KEY = "notice.cubit.ore_found";
  public static final String LOOKING_KEY = "notice.cubit.looking_for";
  public static final String CLEARED_KEY = "notice.cubit.filter_cleared";

  private final RobotEntity robot;
  private final RobotMobScan mobs;
  private final RobotOreScan ores;
  private boolean switchedOn = true;
  private OreFilter oreFilter = OreFilter.EMPTY;
  private ExpressionTimer surprised = ExpressionTimer.STOPPED;

  RobotScanners(RobotEntity robot) {
    this.robot = robot;
    this.mobs = new RobotMobScan(robot);
    this.ores = new RobotOreScan(robot);
  }

  public OreFilter oreFilter() {
    return oreFilter;
  }

  public boolean enemyInRange() {
    return mobs.enemyInRange();
  }

  public boolean surprised() {
    return surprised.isRunning();
  }

  void setOreFilter(OreFilter filter) {
    oreFilter = filter;
  }

  void setSwitchedOn(boolean switchedOn) {
    this.switchedOn = switchedOn;
  }

  boolean switchedOn() {
    return switchedOn;
  }

  @Override
  public boolean isOn() {
    return switchedOn;
  }

  @Override
  public boolean isAvailable() {
    return robot.powerState() != PowerState.HIBERNATING;
  }

  @Override
  public void toggle() {
    switchedOn = !switchedOn;
  }

  void tick() {
    surprised = surprised.tick();
    Player owner = ownerToWarn();
    if (owner == null) {
      mobs.reset();
      ores.reset();
      return;
    }
    ScannerSpec spec = robot.spec().scanners();
    if (ScanRules.isDue(robot.tickCount, spec.mobIntervalTicks())) {
      scanMobs(owner, spec);
    }
    if (!ores.isSweeping() && ScanRules.isDue(robot.tickCount, spec.oreIntervalTicks())) {
      startOres(spec);
    }
    if (ores.isSweeping()) {
      continueOres();
    }
  }

  /** Runs a scan now, for what cannot wait the interval. It obeys the same rules. */
  public void scanMobsNow() {
    Player owner = ownerToWarn();
    if (owner != null) {
      scanMobs(owner, robot.spec().scanners());
    }
  }

  /** Runs a whole scan now, for what cannot wait the interval. It obeys the same rules. */
  public void scanOresNow() {
    if (ownerToWarn() != null) {
      startOres(robot.spec().scanners());
      while (ores.isSweeping()) {
        continueOres();
      }
    }
  }

  private Player ownerToWarn() {
    Player owner = RobotInteractions.followedPlayer(robot);
    boolean follows = owner != null && owner.level() == robot.level();
    return ScanRules.runs(switchedOn, robot.powerState(), follows) ? owner : null;
  }

  private void scanMobs(Player owner, ScannerSpec spec) {
    spend(spec.mobCost());
    mobs.scan(owner, spec.mobRadiusBlocks()).ifPresent(this::warnOfMob);
  }

  private void warnOfMob(Mob mob) {
    Component text = Component.translatable(ENEMY_KEY, mob.getType().getDescription());
    if (robot.notices().showNamed(mob.getType().getDescriptionId(), text)) {
      robot.playSound(CubitSounds.HOSTILE_MOB_ALERT.get());
    }
  }

  private void startOres(ScannerSpec spec) {
    spend(spec.oreCost());
    ores.start(spec.oreRadiusBlocks());
  }

  private void continueOres() {
    OreSignals signals = CubitMod.CONFIG.oreSignals();
    ores.continueSweep(CubitMod.CONFIG.scanBlocksPerTick(), signals, oreFilter)
        .ifPresent(fresh -> warnOfOres(fresh, signals));
  }

  private void warnOfOres(List<OreKind> fresh, OreSignals signals) {
    signals.lead(fresh, oreFilter).ifPresent(kind -> warnOfOre(kind, signals));
  }

  private void warnOfOre(OreKind kind, OreSignals signals) {
    boolean rare = signals.isRare(kind);
    if (rare) {
      surprised = ExpressionTimer.started(CubitMod.CONFIG.surprisedExpressionTicks());
    }
    boolean shown = robot.notices().showNamed(kind.tag(), oreText(kind));
    if (shown && rare) {
      robot.playSound(CubitSounds.RARE_ORE_ALERT.get());
    }
  }

  private static Component oreText(OreKind kind) {
    return kind.name()
        .<Component>map(name -> Component.translatable(ORE_KEY, name))
        .orElseGet(() -> Component.translatable(ORE_FOUND_KEY));
  }

  /** With less charge than the cost, it spends what is left, like any other use. */
  private void spend(Energy cost) {
    robot.setBattery(robot.battery().consume(cost).battery());
  }
}
