package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockedRobot;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyHalves;
import io.github.marcelofabianov.cubit.domain.robot.HealTime;
import net.minecraft.world.inventory.ContainerData;

/**
 * The numbers of the dock screen. The server reads them once per tick, and the menu copies them to
 * the client. Energy takes two values, because each one travels in 16 bits.
 */
final class DockScreenValues implements ContainerData {
  static final int STOCK = 0;
  static final int CAPACITY = 2;
  static final int INPUT = 4;
  static final int STATUS = 6;
  static final int WAITING = 7;
  static final int ROBOT_PRESENT = 8;
  static final int ROBOT_CHARGE = 9;
  static final int ROBOT_CAPACITY = 11;
  static final int ROBOT_HEALTH = 13;
  static final int ROBOT_MAX_HEALTH = 14;
  static final int ROBOT_RECHARGE = 15;
  static final int HEAL_KIND = 17;
  static final int HEAL_MINUTES = 18;
  static final int EXPRESSION = 19;
  static final int TIER = 20;
  static final int COUNT = 21;
  private static final int TRUE_VALUE = 1;

  private final InductionDockBlockEntity dock;
  private final int[] values = new int[COUNT];

  DockScreenValues(InductionDockBlockEntity dock) {
    this.dock = dock;
  }

  /** Reads the dock and the robot in it once, so the data slots do not read them each. */
  void refresh() {
    putEnergy(STOCK, dock.energy());
    putEnergy(CAPACITY, new Energy(CubitMod.CONFIG.dockEnergyCapacity()));
    putEnergy(INPUT, dock.inputShown());
    values[STATUS] = dock.status().ordinal();
    values[WAITING] = dock.waitingCount();
    values[ROBOT_PRESENT] = 0;
    dock.dockedRobot().ifPresent(this::putRobot);
  }

  private void putRobot(DockedRobot robot) {
    values[ROBOT_PRESENT] = TRUE_VALUE;
    putEnergy(ROBOT_CHARGE, robot.battery().charge());
    putEnergy(ROBOT_CAPACITY, robot.battery().capacity());
    values[ROBOT_HEALTH] = (int) Math.ceil(robot.health());
    values[ROBOT_MAX_HEALTH] = (int) Math.ceil(robot.maxHealth());
    putEnergy(ROBOT_RECHARGE, robot.rechargePerTick());
    HealTime heal =
        HealTime.of(robot.health(), robot.maxHealth(), CubitMod.CONFIG.dockFullHealTicks());
    values[HEAL_KIND] = heal.kind().ordinal();
    values[HEAL_MINUTES] = heal.minutes();
    values[EXPRESSION] = robot.expression().ordinal();
    values[TIER] = robot.tier().ordinal();
  }

  private void putEnergy(int index, Energy energy) {
    values[index] = EnergyHalves.low(energy);
    values[index + 1] = EnergyHalves.high(energy);
  }

  /** On either side: the client gets the halves back from its own copy. */
  static Energy energy(ContainerData data, int index) {
    return EnergyHalves.join(data.get(index), data.get(index + 1));
  }

  static boolean isTrue(ContainerData data, int index) {
    return data.get(index) == TRUE_VALUE;
  }

  @Override
  public int get(int index) {
    return values[index];
  }

  /** The server never takes values from the client. */
  @Override
  public void set(int index, int value) {}

  @Override
  public int getCount() {
    return COUNT;
  }
}
