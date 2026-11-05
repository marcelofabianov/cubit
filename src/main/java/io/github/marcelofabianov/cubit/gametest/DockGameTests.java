package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.DockStatusProperty;
import io.github.marcelofabianov.cubit.block.InductionDockBlock;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.menu.InductionDockMenu;
import io.github.marcelofabianov.cubit.registry.CubitBlockEntities;
import io.github.marcelofabianov.cubit.registry.CubitBlocks;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The dock as a machine: its owner, its energy, its slot and its protection. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DockGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final int START_X = 4;
  private static final int BLOCKED_X = 8;
  private static final int NEXT_TICK = 2;
  private static final int HOPPER_TICKS = 30;
  private static final int SOME_DUSTS = 3;
  private static final int MANY = 1_000_000;
  private static final int INPUT = 5_000;
  private static final int CAPACITY = 100_000;
  private static final long ONE_DUST = 5_000;
  private static final long SAVED = 12_345;
  private static final float EXPLOSION_POWER = 6f;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos DOCK = FLOOR.above();
  private static final String NO_OWNER = "The dock did not get its owner";
  private static final String WRONG_INPUT = "Wrong energy accepted";
  private static final String GAVE_OUT = "The dock gave energy out";
  private static final String WRONG_ENERGY = "Wrong energy in the dock";
  private static final String WRONG_SLOT = "Wrong item in the slot";
  private static final String STRANGER = "Someone else used the dock";
  private static final String TOOK_OUT = "A hopper took the item out of the dock";
  private static final String BROKEN = "The dock should still be there";
  private static final String NOT_BROKEN = "The owner could not break the dock";
  private static final String LOST = "The energy of the dock was lost";
  private static final String NO_ITEM = "The slot did not drop its item";
  private static final String MOVABLE = "A piston could move the dock";
  private static final String WRONG_MENU = "Wrong access to the screen of the dock";

  private DockGameTests() {}

  @GameTest(template = ARENA)
  public static void theDockTakesTwoBlocksAndNeedsTheSpace(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    place(helper, owner, Energy.ZERO);
    helper.assertBlockPresent(CubitBlocks.INDUCTION_DOCK.get(), DOCK.above());
    BlockPos blocked = new BlockPos(BLOCKED_X, FLOOR_Y, MIDDLE_Z);
    helper.setBlock(blocked.above(2), Blocks.STONE);
    GameTestRobots.use(helper, owner, new ItemStack(CubitItems.INDUCTION_DOCK.get()), blocked);
    helper.assertBlockNotPresent(CubitBlocks.INDUCTION_DOCK.get(), blocked.above());
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void breakingTheUpperPartBreaksBoth(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    place(helper, owner, new Energy(SAVED));

    owner.gameMode.destroyBlock(helper.absolutePos(DOCK.above()));

    helper.assertBlockNotPresent(CubitBlocks.INDUCTION_DOCK.get(), DOCK);
    helper.assertBlockNotPresent(CubitBlocks.INDUCTION_DOCK.get(), DOCK.above());
    long drops =
        helper.getEntities(EntityType.ITEM).stream()
            .map(ItemEntity::getItem)
            .filter(item -> item.is(CubitItems.INDUCTION_DOCK.get()))
            .count();
    helper.assertValueEqual(drops, 1L, LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aStrangerDoesNotBreakTheUpperPart(GameTestHelper helper) {
    place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);

    GameTestRobots.onlinePlayer(helper).gameMode.destroyBlock(helper.absolutePos(DOCK.above()));

    helper.assertBlockPresent(CubitBlocks.INDUCTION_DOCK.get(), DOCK.above());
    helper.succeed();
  }

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

  @GameTest(template = ARENA)
  public static void whoPlacesTheDockOwnsIt(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock = place(helper, owner, Energy.ZERO);

    helper.assertValueEqual(dock.owner(), Optional.of(owner.getUUID()), NO_OWNER);
    helper.succeed();
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

  @GameTest(template = ARENA)
  public static void breakingInCreativeKeepsTheEnergyAndDropsTheSlot(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    InductionDockBlockEntity dock = place(helper, owner, new Energy(SAVED));
    dock.slot().setItem(0, new ItemStack(Items.STICK));

    helper.assertTrue(owner.gameMode.destroyBlock(helper.absolutePos(DOCK)), NOT_BROKEN);

    ItemStack item = droppedItem(helper, CubitItems.INDUCTION_DOCK.get().getDefaultInstance());
    helper.assertValueEqual(item.get(CubitDataComponents.DOCK_ENERGY.get()), SAVED, LOST);
    droppedItem(helper, new ItemStack(Items.STICK));
    helper.succeed();
  }

  /** The simulated player is always creative, so this one goes through the loot table. */
  @GameTest(template = ARENA)
  public static void theLootTableKeepsTheEnergyInTheItem(GameTestHelper helper) {
    place(helper, helper.makeMockPlayer(GameType.SURVIVAL), new Energy(SAVED));

    helper.getLevel().destroyBlock(helper.absolutePos(DOCK), true);

    ItemStack item = droppedItem(helper, CubitItems.INDUCTION_DOCK.get().getDefaultInstance());
    helper.assertValueEqual(item.get(CubitDataComponents.DOCK_ENERGY.get()), SAVED, LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theItemGivesItsEnergyBackWhenPlaced(GameTestHelper helper) {
    InductionDockBlockEntity dock =
        place(helper, helper.makeMockPlayer(GameType.SURVIVAL), new Energy(SAVED));

    helper.assertValueEqual(dock.energy(), new Energy(SAVED), LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void onlyTheOwnerBreaksTheDock(GameTestHelper helper) {
    place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);

    stranger.gameMode.destroyBlock(helper.absolutePos(DOCK));

    helper.assertBlockPresent(CubitBlocks.INDUCTION_DOCK.get(), DOCK);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aCreativePlayerWithoutPermissionDoesNotBreakIt(GameTestHelper helper) {
    place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);

    stranger.gameMode.destroyBlock(helper.absolutePos(DOCK));

    helper.assertBlockPresent(CubitBlocks.INDUCTION_DOCK.get(), DOCK);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void explosionsDoNotBreakTheDock(GameTestHelper helper) {
    place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);
    Vec3 center = helper.absoluteVec(Vec3.atCenterOf(DOCK.above()));

    helper
        .getLevel()
        .explode(
            null, center.x, center.y, center.z, EXPLOSION_POWER, Level.ExplosionInteraction.TNT);

    helper.assertBlockPresent(CubitBlocks.INDUCTION_DOCK.get(), DOCK);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void pistonsDoNotMoveTheDock(GameTestHelper helper) {
    place(helper, helper.makeMockPlayer(GameType.SURVIVAL), Energy.ZERO);
    BlockState state = helper.getBlockState(DOCK);

    helper.assertValueEqual(state.getPistonPushReaction(), PushReaction.BLOCK, MOVABLE);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void savingKeepsTheOwnerTheEnergyAndTheSlot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    InductionDockBlockEntity dock = place(helper, owner, new Energy(SAVED));
    dock.slot().setItem(0, new ItemStack(Items.STICK));
    CompoundTag saved = dock.saveWithoutMetadata(helper.getLevel().registryAccess());

    InductionDockBlockEntity loaded =
        CubitBlockEntities.INDUCTION_DOCK
            .get()
            .create(helper.absolutePos(DOCK), helper.getBlockState(DOCK));
    loaded.loadWithComponents(saved, helper.getLevel().registryAccess());

    helper.assertValueEqual(loaded.owner(), Optional.of(owner.getUUID()), NO_OWNER);
    helper.assertValueEqual(loaded.energy(), new Energy(SAVED), LOST);
    helper.assertTrue(loaded.slot().getItem(0).is(Items.STICK), WRONG_SLOT);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void onlyTheOwnerKeepsTheScreenOpen(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    InductionDockBlockEntity dock = place(helper, owner, Energy.ZERO);
    Vec3 near = helper.absoluteVec(Vec3.atCenterOf(DOCK));
    owner.moveTo(near.x, near.y, near.z);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    stranger.moveTo(near.x, near.y, near.z);

    InductionDockMenu menu = InductionDockMenu.onServer(1, owner.getInventory(), dock);

    helper.assertTrue(menu.stillValid(owner), WRONG_MENU);
    helper.assertFalse(menu.stillValid(stranger), WRONG_MENU);
    helper.succeed();
  }

  private static InductionDockBlockEntity place(
      GameTestHelper helper, Player owner, Energy energy) {
    ItemStack item = new ItemStack(CubitItems.INDUCTION_DOCK.get());
    item.set(CubitDataComponents.DOCK_ENERGY.get(), energy.amount());
    GameTestRobots.use(helper, owner, item, FLOOR);
    return helper.getBlockEntity(DOCK);
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

  private static ItemStack droppedItem(GameTestHelper helper, ItemStack kind) {
    return helper.getEntities(EntityType.ITEM).stream()
        .map(ItemEntity::getItem)
        .filter(stack -> stack.is(kind.getItem()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(NO_ITEM));
  }
}
