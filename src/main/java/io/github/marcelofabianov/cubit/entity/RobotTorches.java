package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.TorchPlanner;
import io.github.marcelofabianov.cubit.domain.behavior.TorchRules;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TorchSpec;
import io.github.marcelofabianov.cubit.menu.RobotSwitch;
import io.github.marcelofabianov.cubit.menu.TorchControl;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Places torches where mobs could spawn, one per scan, where it makes the most spots safe (answers
 * 971 to 984). The switch is kept in the item, and the rules live in the domain.
 */
public final class RobotTorches implements RobotSwitch, TorchControl {
  private static final float VOLUME = 1f;
  private static final float PITCH = 1f;

  private final RobotEntity robot;
  private boolean switchedOn = true;
  private long nextCheckTick;

  RobotTorches(RobotEntity robot) {
    this.robot = robot;
  }

  @Override
  public boolean isOn() {
    return switchedOn;
  }

  /** Only a tier with torches can change the switch, as the magnet (answer 974). */
  @Override
  public boolean isAvailable() {
    return robot.spec().torches().unlocked();
  }

  @Override
  public void toggle() {
    switchedOn = !switchedOn;
  }

  @Override
  public Energy cost() {
    return robot.spec().torches().cost();
  }

  @Override
  public Optional<RobotTier> unlockTier() {
    return Arrays.stream(RobotTier.values())
        .filter(tier -> robot.specAt(tier).torches().unlocked())
        .findFirst();
  }

  /** The torch slot, for the screen and the tests. */
  public Container slot() {
    return robot.robotInventory().torchSlot().container();
  }

  boolean switchedOn() {
    return switchedOn;
  }

  void setSwitchedOn(boolean on) {
    switchedOn = on;
  }

  /** Scans by the game time, every few ticks, and never more than one torch at a time. */
  void tick() {
    Level level = robot.level();
    long now = level.getGameTime();
    if (now < nextCheckTick) {
      return;
    }
    nextCheckTick = now + CubitMod.CONFIG.torchCheckTicks();
    TorchSpec spec = robot.spec().torches();
    RobotTorchSlot slot = robot.robotInventory().torchSlot();
    if (!TorchRules.places(
        robot.powerState(), switchedOn, spec, slot.hasTorch(), robot.battery())) {
      return;
    }
    TorchScan.Settings settings =
        new TorchScan.Settings(
            CubitMod.CONFIG.torchScanRadiusBlocks(),
            CubitMod.CONFIG.torchReachBlocks(),
            spec,
            CubitMod.CONFIG.lampPolicy().onBelow());
    TorchScan.around(level, robot.blockPosition(), settings)
        .flatMap(scan ->
            TorchPlanner.best(scan.grid(), TorchScan.torchLight(), spec.lightLevel())
                .flatMap(scan::placement))
        .ifPresent(placement -> place(level, placement, spec, slot));
  }

  private void place(
      Level level, TorchScan.Placement placement, TorchSpec spec, RobotTorchSlot slot) {
    level.setBlock(placement.pos(), placement.state(), Block.UPDATE_ALL);
    level.gameEvent(robot, GameEvent.BLOCK_PLACE, placement.pos());
    level.playSound(
        null,
        placement.pos(),
        placement.state().getSoundType().getPlaceSound(),
        SoundSource.BLOCKS,
        VOLUME,
        PITCH);
    slot.useOne();
    robot.setBattery(TorchRules.pay(robot.battery(), spec));
  }
}
