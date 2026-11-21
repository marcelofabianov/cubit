package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.domain.energy.Energy;
import net.neoforged.neoforge.energy.IEnergyStorage;

/** How other mods see a desk: it takes FE in from below, up to its input, and never gives any. */
final class DeskEnergyStorage implements IEnergyStorage {
  private final EngineeringDeskBlockEntity desk;

  DeskEnergyStorage(EngineeringDeskBlockEntity desk) {
    this.desk = desk;
  }

  @Override
  public int receiveEnergy(int toReceive, boolean simulate) {
    return Math.toIntExact(desk.receive(new Energy(Math.max(toReceive, 0)), simulate).amount());
  }

  @Override
  public int extractEnergy(int toExtract, boolean simulate) {
    return 0;
  }

  @Override
  public int getEnergyStored() {
    return (int) Math.min(Integer.MAX_VALUE, desk.energy().amount());
  }

  @Override
  public int getMaxEnergyStored() {
    return (int) Math.min(Integer.MAX_VALUE, desk.capacity().amount());
  }

  @Override
  public boolean canExtract() {
    return false;
  }

  @Override
  public boolean canReceive() {
    return true;
  }
}
