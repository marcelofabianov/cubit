package io.github.marcelofabianov.cubit.block;

import io.github.marcelofabianov.cubit.domain.energy.Energy;
import net.neoforged.neoforge.energy.IEnergyStorage;

/** How other mods see a dock: it takes FE in, up to its input, and never gives any out. */
final class DockEnergyStorage implements IEnergyStorage {
  private final InductionDockBlockEntity dock;

  DockEnergyStorage(InductionDockBlockEntity dock) {
    this.dock = dock;
  }

  @Override
  public int receiveEnergy(int toReceive, boolean simulate) {
    return Math.toIntExact(dock.receive(new Energy(Math.max(toReceive, 0)), simulate).amount());
  }

  @Override
  public int extractEnergy(int toExtract, boolean simulate) {
    return 0;
  }

  @Override
  public int getEnergyStored() {
    return Math.toIntExact(dock.energy().amount());
  }

  @Override
  public int getMaxEnergyStored() {
    return Math.toIntExact(dock.capacityAmount());
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
