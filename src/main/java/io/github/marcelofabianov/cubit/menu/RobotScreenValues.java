package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyHalves;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.world.inventory.ContainerData;

/**
 * The numbers of the robot screen. The server reads them once per tick, and the menu copies them to
 * the client. Energy takes two values, because each one travels in 16 bits.
 */
final class RobotScreenValues implements ContainerData {
  static final int CHARGE = 0;
  static final int CAPACITY = 2;
  static final int HEALTH = 4;
  static final int MAX_HEALTH = 5;
  static final int STATUS = 6;
  static final int RECHARGE = 7;
  static final int RECHARGE_RATE = 8;
  static final int SOLAR = 10;
  static final int SOLAR_RATE = 11;
  static final int USE = 13;
  static final int CAUSES = 15;
  static final int FOLLOWING = 16;
  static final int EXPRESSION = 17;
  static final int TIER = 18;
  static final int LAMP = 19;
  static final int SCANNERS = 21;
  static final int CHATTER = 23;
  static final int DEFENSE = 25;
  static final int COUNT = 26;
  static final int TRUE_VALUE = 1;
  private static final int AVAILABLE = 1;

  private final Supplier<RobotScreenReading> source;
  private final RobotSwitch[] switches;
  private final DefenseControl defense;
  private final int[] values = new int[COUNT];

  RobotScreenValues(
      Supplier<RobotScreenReading> source, DefenseControl defense, RobotSwitch... switches) {
    this.source = source;
    this.defense = defense;
    this.switches = switches.clone();
  }

  /** Reads the robot once, so the data slots do not read it each. */
  void refresh() {
    RobotScreenReading reading = source.get();
    putEnergy(CHARGE, reading.battery().charge());
    putEnergy(CAPACITY, reading.battery().capacity());
    values[HEALTH] = (int) Math.ceil(reading.vitals().health());
    values[MAX_HEALTH] = (int) Math.ceil(reading.vitals().maxHealth());
    values[STATUS] = reading.status().ordinal();
    values[RECHARGE] = reading.recharge().ordinal();
    putEnergy(RECHARGE_RATE, reading.rechargeRate());
    values[SOLAR] = reading.solar().ordinal();
    putEnergy(SOLAR_RATE, reading.solarRate());
    putEnergy(USE, reading.use());
    values[CAUSES] = maskOf(reading.causes());
    values[FOLLOWING] = reading.vitals().following() ? TRUE_VALUE : 0;
    values[EXPRESSION] = reading.vitals().expression().ordinal();
    values[TIER] = reading.tier().ordinal();
    values[DEFENSE] = defense.mode().ordinal();
    for (int index = 0; index < switches.length; index++) {
      putSwitch(LAMP + index * 2, switches[index]);
    }
  }

  private void putSwitch(int index, RobotSwitch robotSwitch) {
    values[index] = robotSwitch.isOn() ? TRUE_VALUE : 0;
    values[index + AVAILABLE] = robotSwitch.isAvailable() ? TRUE_VALUE : 0;
  }

  private void putEnergy(int index, Energy energy) {
    values[index] = EnergyHalves.low(energy);
    values[index + 1] = EnergyHalves.high(energy);
  }

  private static int maskOf(List<EnergyUseCause> causes) {
    int mask = 0;
    for (EnergyUseCause cause : causes) {
      mask |= 1 << cause.ordinal();
    }
    return mask;
  }

  static Energy energy(ContainerData data, int index) {
    return EnergyHalves.join(data.get(index), data.get(index + 1));
  }

  static boolean isTrue(ContainerData data, int index) {
    return data.get(index) == TRUE_VALUE;
  }

  static boolean isAvailable(ContainerData data, int switchIndex) {
    return isTrue(data, switchIndex + AVAILABLE);
  }

  static List<EnergyUseCause> causes(ContainerData data) {
    List<EnergyUseCause> causes = new ArrayList<>();
    for (EnergyUseCause cause : EnergyUseCause.values()) {
      if ((data.get(CAUSES) & (1 << cause.ordinal())) != 0) {
        causes.add(cause);
      }
    }
    return causes;
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
