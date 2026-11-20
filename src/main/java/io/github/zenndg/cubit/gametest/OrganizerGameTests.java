package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.robot.AreaCorner;
import io.github.zenndg.cubit.domain.robot.OrganizerMode;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.robot.UnloadChests;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.entity.RobotOrganizerMarks;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The work of the Organizer: it stores in the chest with the most of the item, takes only the
 * output of a marked machine, supplies a destination up to its amount, answers only the owner, and
 * keeps its mode in the item and its marks in the save (answers 1285 to 1304).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class OrganizerGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 1200;
  private static final int SETTLE = 400;
  private static final int FLOOR_Y = 1;
  private static final int ON_FLOOR_Y = 2;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(6, FLOOR_Y, 2);
  private static final BlockPos FIRST_CORNER = new BlockPos(2, FLOOR_Y, 0);
  private static final BlockPos SECOND_CORNER = new BlockPos(12, FLOOR_Y + 2, 4);
  private static final BlockPos EMPTY_CHEST = new BlockPos(3, ON_FLOOR_Y, 0);
  private static final BlockPos STONE_CHEST = new BlockPos(5, ON_FLOOR_Y, 0);
  private static final BlockPos FURNACE = new BlockPos(9, ON_FLOOR_Y, 4);
  private static final BlockPos DROP = new BlockPos(8, ON_FLOOR_Y, 2);
  private static final Energy CHARGE = new Energy(3_000_000);
  private static final float HEALTH = 100f;
  private static final int CARD_SLOT = 0;
  private static final int SOME = 4;
  private static final int MORE = 6;
  private static final int COAL = 32;
  private static final int WANTED = 8;
  private static final int PRESSES_TO_EIGHT = 7;
  private static final int INPUT_SLOT = 0;
  private static final int FUEL_SLOT = 1;
  private static final int RESULT_SLOT = 2;
  private static final int ELITE_MARKS = 8;
  private static final int ELITE_CHESTS = 9;
  private static final int DOWN = -1;
  private static final int MARK_ROW_Z = 4;
  private static final double HALF = 0.5;
  private static final String NOT_STORED = "The Organizer did not store in the chest with the most";
  private static final String NOT_TAKEN = "The Organizer did not take the output of the furnace";
  private static final String TOUCHED_INPUT = "The Organizer touched the input of the furnace";
  private static final String NOT_SUPPLIED = "The Organizer did not supply up to the amount";
  private static final String STRANGER_MARKED = "Someone else marked a block";
  private static final String NOT_FULL = "The tier allowed more marks than its limit";
  private static final String NOT_KEPT = "The Organizer did not keep its mode or its marks";
  private static final String WRONG_CHESTS = "The Organizer did not get the chests of its tier";

  private OrganizerGameTests() {}

  /** An item on the ground goes to the chest that already has the most of it (answer 208). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anOrganizerStoresInTheChestWithTheMost(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ChestBlockEntity stones = chests(helper, owner, organizer(helper, owner));
    stones.setItem(0, new ItemStack(Items.COBBLESTONE, SOME));
    drop(helper, new ItemStack(Items.COBBLESTONE, MORE));
    ChestBlockEntity empty = helper.getBlockEntity(EMPTY_CHEST);

    helper.succeedWhen(() -> {
      helper.assertTrue(count(stones, Items.COBBLESTONE) == SOME + MORE, NOT_STORED);
      helper.assertTrue(empty.isEmpty(), NOT_STORED);
    });
  }

  /**
   * It takes the result through the bottom face and never the input, and with no chest holding it,
   * it goes to the first chest with room (answers 1289 and 1291).
   */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anOrganizerTakesOnlyTheOutputOfAMarkedMachine(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = organizer(helper, owner);
    chests(helper, owner, robot);
    ChestBlockEntity chest = helper.getBlockEntity(EMPTY_CHEST);
    FurnaceBlockEntity furnace = furnace(helper);
    furnace.setItem(INPUT_SLOT, new ItemStack(Items.DIRT));
    furnace.setItem(RESULT_SLOT, new ItemStack(Items.IRON_INGOT, SOME));
    marks(robot).click(owner, helper.absolutePos(FURNACE), Direction.UP);

    helper.succeedWhen(() -> {
      helper.assertTrue(count(chest, Items.IRON_INGOT) == SOME, NOT_TAKEN);
      helper.assertTrue(furnace.getItem(INPUT_SLOT).is(Items.DIRT), TOUCHED_INPUT);
    });
  }

  /** The side face fills the fuel up to the amount, and never past it (answers 1294, 1296). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anOrganizerSuppliesUpToTheAmount(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = organizer(helper, owner);
    ChestBlockEntity chest = chests(helper, owner, robot);
    chest.setItem(0, new ItemStack(Items.COAL, COAL));
    FurnaceBlockEntity furnace = furnace(helper);
    robot.workArea().work().organizing().switchMode(owner);
    marks(robot).click(owner, helper.absolutePos(FURNACE), Direction.NORTH);
    marks(robot).setWanted(owner, 0, new ItemStack(Items.COAL));
    for (int press = 0; press < PRESSES_TO_EIGHT; press++) {
      marks(robot).stepAmount(owner, 0, DOWN, true);
    }

    helper.runAfterDelay(
        SETTLE,
        () -> {
          helper.assertTrue(furnace.getItem(FUEL_SLOT).getCount() == WANTED, NOT_SUPPLIED);
          helper.assertTrue(count(chest, Items.COAL) == COAL - WANTED, NOT_SUPPLIED);
          helper.succeed();
        });
  }

  /** Only the owner marks, and the tier stops at its limit (answers 1297 and 1300). */
  @GameTest(template = ARENA)
  public static void onlyTheOwnerMarksUpToTheLimit(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = organizer(helper, owner);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    helper.setBlock(FURNACE, Blocks.CHEST);
    BlockPos first = helper.absolutePos(FURNACE);

    helper.assertTrue(marks(robot).click(stranger, first, Direction.UP).isEmpty(), STRANGER_MARKED);
    Optional<UnloadChests.Mark> last = Optional.empty();
    for (int index = 0; index <= ELITE_MARKS; index++) {
      BlockPos pos = new BlockPos(FIRST_CORNER.getX() + index, ON_FLOOR_Y, MARK_ROW_Z);
      helper.setBlock(pos, Blocks.CHEST);
      last = marks(robot).click(owner, helper.absolutePos(pos), Direction.UP);
    }
    helper.assertValueEqual(last, Optional.of(UnloadChests.Mark.FULL), NOT_FULL);
    helper.assertTrue(robot.docking().unload().chestLimit() == ELITE_CHESTS, WRONG_CHESTS);
    helper.succeed();
  }

  /** The mode stays in the item and the marks in the save, and collecting erases them (1301). */
  @GameTest(template = ARENA)
  public static void theModeStaysInTheItemAndTheMarksInTheSave(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = organizer(helper, owner);
    furnace(helper);
    robot.workArea().work().organizing().switchMode(owner);
    marks(robot).click(owner, helper.absolutePos(FURNACE), Direction.NORTH);

    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    RobotEntity loaded = GameTestRobots.loadRobot(helper, saved);
    helper.assertTrue(marks(loaded).targets().size() == 1, NOT_KEPT);
    ItemStack item = GameTestRobots.collect(owner, robot);
    RobotState state = item.get(CubitDataComponents.ROBOT_STATE.get());
    helper.assertValueEqual(state.workSettings().organizerMode(), OrganizerMode.SUPPLY, NOT_KEPT);
    RobotEntity placed = GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
    helper.assertTrue(marks(placed).targets().isEmpty(), NOT_KEPT);
    helper.succeed();
  }

  private static RobotEntity organizer(GameTestHelper helper, ServerPlayer owner) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.ELITE, CHARGE, HEALTH));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
    robot
        .workArea()
        .work()
        .slot()
        .setItem(CARD_SLOT, new ItemStack(CubitItems.ORGANIZER_CARD.get()));
    BlockPos one = helper.absolutePos(FIRST_CORNER);
    BlockPos two = helper.absolutePos(SECOND_CORNER);
    robot
        .workArea()
        .assign(
            WorkArea.between(
                new AreaCorner(one.getX(), one.getY(), one.getZ()),
                new AreaCorner(two.getX(), two.getY(), two.getZ()),
                robot.level().dimension().location().toString()),
            owner);
    return robot;
  }

  /** The empty chest first and the one with stone second, in the order of the marks. */
  private static ChestBlockEntity chests(
      GameTestHelper helper, ServerPlayer owner, RobotEntity robot) {
    helper.setBlock(EMPTY_CHEST, Blocks.CHEST);
    helper.setBlock(STONE_CHEST, Blocks.CHEST);
    robot.docking().unload().click(owner, helper.absolutePos(EMPTY_CHEST));
    robot.docking().unload().click(owner, helper.absolutePos(STONE_CHEST));
    return helper.getBlockEntity(STONE_CHEST);
  }

  private static FurnaceBlockEntity furnace(GameTestHelper helper) {
    helper.setBlock(FURNACE, Blocks.FURNACE);
    return helper.getBlockEntity(FURNACE);
  }

  private static void drop(GameTestHelper helper, ItemStack stack) {
    BlockPos at = helper.absolutePos(DROP);
    ItemEntity item =
        new ItemEntity(helper.getLevel(), at.getX() + HALF, at.getY(), at.getZ() + HALF, stack);
    helper.getLevel().addFreshEntity(item);
  }

  private static RobotOrganizerMarks marks(RobotEntity robot) {
    return robot.workArea().work().organizing().marks();
  }

  private static int count(ChestBlockEntity chest, net.minecraft.world.item.Item item) {
    int found = 0;
    for (int slot = 0; slot < chest.getContainerSize(); slot++) {
      if (chest.getItem(slot).is(item)) {
        found += chest.getItem(slot).getCount();
      }
    }
    return found;
  }
}
