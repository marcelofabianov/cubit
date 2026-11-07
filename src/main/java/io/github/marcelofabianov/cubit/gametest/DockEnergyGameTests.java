package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockStatusProperty;
import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The energy of the dock: what comes in, what never leaves, its slot and what the screen shows. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DockEnergyGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final int START_X = 4;
  private static final int NEXT_TICK = 2;
  private static final int HOPPER_TICKS = 30;
  private static final int SOME_DUSTS = 3;
  private static final int MANY = 1_000_000;
  private static final int INPUT = 5_000;
  private static final int CAPACITY = 100_000;
  private static final long ONE_DUST = 5_000;
  private static final long SAVED = 12_345;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos DOCK = FLOOR.above();
  private static final String WRONG_INPUT = "Wrong energy accepted";
  private static final String GAVE_OUT = "The dock gave energy out";
  private static final String WRONG_ENERGY = "Wrong energy in the dock";
  private static final String WRONG_SLOT = "Wrong item in the slot";
  private static final String STRANGER = "Someone else used the dock";
  private static final String TOOK_OUT = "A hopper took the item out of the dock";

  private DockEnergyGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theScreenShowsOffAndIdle(GameTestHelper helper) {
    InductionDockBlockEntity dock =
        place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);

    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertBlockProperty(DOCK, InductionDockBlock.STATUS, DockStatusProperty.OFF);
          dock.energyStorage().receiveEnergy(INPUT, false);
          helper.runAfterDelay(
              NEXT_TICK,
              () -> {
                helper.assertBlockProperty(
                    DOCK, InductionDockBlock.STATUS, DockStatusProperty.IDLE);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void energyEntersUpToTheInputPerTick(GameTestHelper helper) {
    place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);
    IEnergyStorage energy = energyOf(helper);

    helper.assertValueEqual(energy.receiveEnergy(MANY, true), INPUT, WRONG_INPUT);
    helper.assertValueEqual(energy.getEnergyStored(), 0, WRONG_INPUT);
    helper.assertValueEqual(energy.receiveEnergy(MANY, false), INPUT, WRONG_INPUT);
    helper.assertValueEqual(energy.receiveEnergy(MANY, false), 0, WRONG_INPUT);
    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertValueEqual(energy.receiveEnergy(MANY, false), INPUT, WRONG_INPUT);
          helper.assertValueEqual(energy.getMaxEnergyStored(), CAPACITY, WRONG_INPUT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void energyNeverLeavesTheDock(GameTestHelper helper) {
    place(helper, helper.makeMockPlayer(GameType.SURVIVAL), new Energy(SAVED));
    IEnergyStorage energy = energyOf(helper);

    helper.assertValueEqual(energy.extractEnergy(MANY, false), 0, GAVE_OUT);
    helper.assertFalse(energy.canExtract(), GAVE_OUT);
    helper.assertValueEqual(energy.getEnergyStored(), (int) SAVED, GAVE_OUT);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void redstoneInTheHandEntersTheSlotAndBecomesEnergy(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock = place(helper, owner, Energy.ZERO);
    owner.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.REDSTONE));

    click(helper, owner);

    helper.assertTrue(owner.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), WRONG_SLOT);
    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertValueEqual(dock.energy(), new Energy(ONE_DUST), WRONG_ENERGY);
          helper.assertTrue(dock.slot().isEmpty(), WRONG_SLOT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void onlyTheOwnerPutsRedstoneInTheSlot(GameTestHelper helper) {
    InductionDockBlockEntity dock =
        place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);
    stranger.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.REDSTONE));

    click(helper, stranger);

    helper.assertFalse(stranger.getItemInHand(InteractionHand.MAIN_HAND).isEmpty(), STRANGER);
    helper.assertTrue(dock.slot().isEmpty(), STRANGER);
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anItemThatDoesNotFitStaysInTheSlot(GameTestHelper helper) {
    Energy almostFull = new Energy(CAPACITY - ONE_DUST + 1);
    InductionDockBlockEntity dock =
        place(helper, helper.makeMockPlayer(GameType.SURVIVAL), almostFull);
    dock.slot().setItem(0, new ItemStack(Items.REDSTONE, SOME_DUSTS));

    helper.runAfterDelay(
        NEXT_TICK,
        () -> {
          helper.assertValueEqual(dock.slot().getItem(0).getCount(), SOME_DUSTS, WRONG_SLOT);
          helper.assertValueEqual(dock.energy(), almostFull, WRONG_ENERGY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aHopperPutsRedstoneInButTakesNothingOut(GameTestHelper helper) {
    InductionDockBlockEntity dock =
        place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);
    dock.slot().setItem(0, new ItemStack(Items.STICK));
    helper.setBlock(FLOOR, Blocks.HOPPER);
    BlockPos above = DOCK.above(2);
    helper.setBlock(above, Blocks.HOPPER.defaultBlockState());
    HopperBlockEntity feeder = helper.getBlockEntity(above);
    feeder.setItem(0, new ItemStack(Items.REDSTONE));

    helper.runAfterDelay(
        HOPPER_TICKS,
        () -> {
          HopperBlockEntity below = helper.getBlockEntity(FLOOR);
          helper.assertTrue(below.isEmpty(), TOOK_OUT);
          dock.slot().setItem(0, ItemStack.EMPTY);
          helper.runAfterDelay(
              HOPPER_TICKS,
              () -> {
                helper.assertValueEqual(dock.energy(), new Energy(ONE_DUST), WRONG_ENERGY);
                helper.succeed();
              });
        });
  }

  private static InductionDockBlockEntity place(
      GameTestHelper helper, Player owner, Energy energy) {
    return DockScene.placeDock(helper, owner, FLOOR, energy);
  }

  private static IEnergyStorage energyOf(GameTestHelper helper) {
    return helper
        .getLevel()
        .getCapability(Capabilities.EnergyStorage.BLOCK, helper.absolutePos(DOCK), null);
  }

  private static void click(GameTestHelper helper, Player player) {
    BlockPos absolute = helper.absolutePos(DOCK);
    BlockHitResult hit =
        new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false);
    helper
        .getBlockState(DOCK)
        .useItemOn(
            player.getItemInHand(InteractionHand.MAIN_HAND),
            helper.getLevel(),
            player,
            InteractionHand.MAIN_HAND,
            hit);
  }
}
