package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.block.DeskRecipes;
import io.github.zenndg.cubit.block.EngineeringDeskBlockEntity;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.EnergyHalves;
import net.minecraft.world.inventory.ContainerData;

/**
 * The numbers of the desk screen. The server reads them once per tick, and the menu copies them to
 * the client. Energy takes two values, because each one travels in 16 bits. The recipe goes as its
 * place in the catalog, the same on both sides.
 */
final class DeskScreenValues implements ContainerData {
  static final int STOCK = 0;
  static final int CAPACITY = 2;
  static final int STATUS = 4;
  static final int PROGRESS = 5;
  static final int TOTAL = 6;
  static final int DONE = 7;
  static final int RECIPE = 8;
  static final int TIER = 9;
  static final int SLOTS = 10;
  static final int MAX_ORDER = 11;
  static final int COUNT = 12;
  static final int NO_RECIPE = -1;

  private final EngineeringDeskBlockEntity desk;
  private final int[] values = new int[COUNT];

  DeskScreenValues(EngineeringDeskBlockEntity desk) {
    this.desk = desk;
  }

  void refresh() {
    putEnergy(STOCK, desk.energy());
    putEnergy(CAPACITY, desk.capacity());
    values[STATUS] = desk.status().ordinal();
    values[PROGRESS] = desk.progressPercent();
    values[TOTAL] = desk.order().total();
    values[DONE] = desk.order().done();
    values[TIER] = desk.tier().ordinal();
    values[SLOTS] = desk.slots();
    values[MAX_ORDER] = desk.spec().maxOrder();
    values[RECIPE] = recipeIndex();
  }

  private int recipeIndex() {
    if (desk.getLevel() == null || desk.recipeId().isEmpty()) {
      return NO_RECIPE;
    }
    var all = DeskRecipes.all(desk.getLevel().getRecipeManager());
    for (int index = 0; index < all.size(); index++) {
      if (all.get(index).id().equals(desk.recipeId().get())) {
        return index;
      }
    }
    return NO_RECIPE;
  }

  private void putEnergy(int index, Energy energy) {
    values[index] = EnergyHalves.low(energy);
    values[index + 1] = EnergyHalves.high(energy);
  }

  static Energy energy(ContainerData data, int index) {
    return EnergyHalves.join(data.get(index), data.get(index + 1));
  }

  @Override
  public int get(int index) {
    return values[index];
  }

  @Override
  public void set(int index, int value) {
    values[index] = value;
  }

  @Override
  public int getCount() {
    return COUNT;
  }
}
