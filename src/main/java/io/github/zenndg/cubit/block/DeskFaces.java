package io.github.zenndg.cubit.block;

import io.github.zenndg.cubit.item.EnergySlotItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;

/**
 * What the automation sees of each face of the desk, from its front (answers 1436 and 1447): the
 * left puts into the input, the right takes from the output, the back does both, the bottom takes
 * FE and redstone for energy, and the top and the front are closed.
 */
public final class DeskFaces {
  private DeskFaces() {}

  /** Registered by block, because the right half has no block entity of its own. */
  public static void register(RegisterCapabilitiesEvent event, Block desk) {
    event.registerBlock(
        Capabilities.ItemHandler.BLOCK,
        (level, pos, state, entity, side) -> items(level, pos, state, side),
        desk);
    event.registerBlock(
        Capabilities.EnergyStorage.BLOCK,
        (level, pos, state, entity, side) -> {
          EngineeringDeskBlockEntity found = EngineeringDeskBlock.deskAt(level, pos, state);
          return found == null || (side != null && side != Direction.DOWN)
              ? null
              : found.energyStorage();
        },
        desk);
  }

  private static IItemHandler items(Level level, BlockPos pos, BlockState state, Direction side) {
    EngineeringDeskBlockEntity desk = EngineeringDeskBlock.deskAt(level, pos, state);
    if (desk == null) {
      return null;
    }
    Direction front = state.getValue(EngineeringDeskBlock.FACING);
    boolean left = state.getValue(EngineeringDeskBlock.PART) == DeskPart.LEFT;
    if (side == null || side == front.getOpposite()) {
      return new CombinedInvWrapper(input(level, desk), output(desk));
    }
    if (side == Direction.DOWN) {
      return energy(desk.energySlot());
    }
    if (left && side == front.getClockWise()) {
      return input(level, desk);
    }
    return !left && side == front.getCounterClockWise() ? output(desk) : null;
  }

  /** Only materials of the recipes go in, and nothing comes back out (answer 1446). */
  static InvWrapperView input(Level level, EngineeringDeskBlockEntity desk) {
    return new InvWrapperView(desk.input(), desk.slots()) {
      @Override
      public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return DeskMaterialFilter.accepts(level.getRecipeManager(), stack)
            ? super.insertItem(slot, stack, simulate)
            : stack;
      }

      @Override
      public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
      }
    };
  }

  /** The finished items only come out. */
  static InvWrapperView output(EngineeringDeskBlockEntity desk) {
    return new InvWrapperView(desk.output(), desk.slots()) {
      @Override
      public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack;
      }
    };
  }

  /** Redstone from below becomes energy and never comes back out. */
  private static IItemHandler energy(Container slot) {
    return new InvWrapper(slot) {
      @Override
      public ItemStack insertItem(int index, ItemStack stack, boolean simulate) {
        return EnergySlotItems.energyOf(stack).isZero()
            ? stack
            : super.insertItem(index, stack, simulate);
      }

      @Override
      public ItemStack extractItem(int index, int amount, boolean simulate) {
        return ItemStack.EMPTY;
      }
    };
  }

  /** Only the slots of the tier of the desk, over its fixed inventory. */
  static class InvWrapperView extends RangedWrapper {
    InvWrapperView(Container items, int slots) {
      super(new InvWrapper(items), 0, slots);
    }
  }
}
