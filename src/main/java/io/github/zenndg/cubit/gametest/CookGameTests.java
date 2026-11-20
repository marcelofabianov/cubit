package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.robot.AreaCorner;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.robot.UnloadChests;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.entity.RobotMarks;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The work of the Cook: it cooks raw food in a marked smoker with the fuel of the tag, never takes
 * ore nor burns planks, respects what the player left in an oven, and only marks ovens, up to the
 * limit of its tier (answers 1314 to 1329).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CookGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 2400;
  private static final int SETTLE = 400;
  private static final int FLOOR_Y = 1;
  private static final int ON_FLOOR_Y = 2;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(6, FLOOR_Y, 2);
  private static final BlockPos FIRST_CORNER = new BlockPos(2, FLOOR_Y, 0);
  private static final BlockPos SECOND_CORNER = new BlockPos(12, FLOOR_Y + 2, 4);
  private static final BlockPos CHEST = new BlockPos(3, ON_FLOOR_Y, 0);
  private static final BlockPos OVEN = new BlockPos(9, ON_FLOOR_Y, 4);
  private static final int MARK_ROW_Z = 4;
  private static final Energy CHARGE = new Energy(3_000_000);
  private static final float HEALTH = 100f;
  private static final int CARD_SLOT = 0;
  private static final int RAW = 4;
  private static final int COAL = 8;
  private static final int INPUT_SLOT = 0;
  private static final int FUEL_SLOT = 1;
  private static final int ELITE_OVENS = 4;
  private static final String NOT_COOKED = "The Cook did not cook the raw food in the smoker";
  private static final String TOOK_ORE = "The Cook took ore or burned planks";
  private static final String TOUCHED_INPUT = "The Cook touched what the player left in the oven";
  private static final String MARKED_CHEST = "The Cook marked a block that is not an oven";
  private static final String NOT_FULL = "The tier allowed more ovens than its limit";

  private CookGameTests() {}

  /** Raw beef and coal from the chest go into the smoker, and the steak comes out (1317). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aCookCooksRawFoodInASmoker(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = cook(helper, owner);
    ChestBlockEntity chest = chest(helper, owner, robot);
    chest.setItem(0, new ItemStack(Items.BEEF, RAW));
    chest.setItem(1, new ItemStack(Items.COAL, COAL));
    oven(helper, Blocks.SMOKER);
    marks(robot).click(owner, helper.absolutePos(OVEN), Direction.UP);

    helper.succeedWhen(() ->
        helper.assertTrue(
            count(robot.inventory(), Items.COOKED_BEEF) + count(chest, Items.COOKED_BEEF) > 0,
            NOT_COOKED));
  }

  /** Only raw food and the fuel of the tag move, so ore and planks stay in the chest (1319). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aCookNeverTakesOreNorBurnsPlanks(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = cook(helper, owner);
    ChestBlockEntity chest = chest(helper, owner, robot);
    chest.setItem(0, new ItemStack(Items.IRON_ORE, RAW));
    chest.setItem(1, new ItemStack(Items.OAK_PLANKS, COAL));
    AbstractFurnaceBlockEntity furnace = oven(helper, Blocks.FURNACE);
    marks(robot).click(owner, helper.absolutePos(OVEN), Direction.UP);

    helper.runAfterDelay(
        SETTLE,
        () -> {
          helper.assertTrue(furnace.getItem(INPUT_SLOT).isEmpty(), TOOK_ORE);
          helper.assertTrue(furnace.getItem(FUEL_SLOT).isEmpty(), TOOK_ORE);
          helper.assertTrue(count(chest, Items.IRON_ORE) == RAW, TOOK_ORE);
          helper.succeed();
        });
  }

  /** What the player left in the input never leaves, and no other food goes in (answer 1320). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aCookRespectsWhatThePlayerLeft(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = cook(helper, owner);
    ChestBlockEntity chest = chest(helper, owner, robot);
    chest.setItem(0, new ItemStack(Items.BEEF, RAW));
    AbstractFurnaceBlockEntity furnace = oven(helper, Blocks.FURNACE);
    furnace.setItem(INPUT_SLOT, new ItemStack(Items.DIRT));
    marks(robot).click(owner, helper.absolutePos(OVEN), Direction.UP);

    helper.runAfterDelay(
        SETTLE,
        () -> {
          helper.assertTrue(furnace.getItem(INPUT_SLOT).is(Items.DIRT), TOUCHED_INPUT);
          helper.assertTrue(count(chest, Items.BEEF) == RAW, TOUCHED_INPUT);
          helper.succeed();
        });
  }

  /** The Mark app only takes ovens for the Cook, up to the limit of its tier (1316, 1326). */
  @GameTest(template = ARENA)
  public static void aCookMarksOnlyOvensUpToTheLimit(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = cook(helper, owner);
    helper.setBlock(CHEST, Blocks.CHEST);
    helper.assertTrue(
        marks(robot).click(owner, helper.absolutePos(CHEST), Direction.UP).isEmpty(), MARKED_CHEST);
    Optional<UnloadChests.Mark> last = Optional.empty();
    for (int index = 0; index <= ELITE_OVENS; index++) {
      BlockPos pos = new BlockPos(FIRST_CORNER.getX() + index, ON_FLOOR_Y, MARK_ROW_Z);
      helper.setBlock(pos, Blocks.SMOKER);
      last = marks(robot).click(owner, helper.absolutePos(pos), Direction.UP);
    }
    helper.assertValueEqual(last, Optional.of(UnloadChests.Mark.FULL), NOT_FULL);
    helper.succeed();
  }

  private static RobotEntity cook(GameTestHelper helper, ServerPlayer owner) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.ELITE, CHARGE, HEALTH));
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
    robot.workArea().work().slot().setItem(CARD_SLOT, new ItemStack(CubitItems.COOK_CARD.get()));
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

  private static ChestBlockEntity chest(
      GameTestHelper helper, ServerPlayer owner, RobotEntity robot) {
    helper.setBlock(CHEST, Blocks.CHEST);
    robot.docking().unload().click(owner, helper.absolutePos(CHEST));
    return helper.getBlockEntity(CHEST);
  }

  private static AbstractFurnaceBlockEntity oven(
      GameTestHelper helper, net.minecraft.world.level.block.Block block) {
    helper.setBlock(OVEN, block);
    return helper.getBlockEntity(OVEN);
  }

  private static RobotMarks marks(RobotEntity robot) {
    return robot.workArea().work().marks();
  }

  private static int count(Container container, Item item) {
    int found = 0;
    for (int slot = 0; slot < container.getContainerSize(); slot++) {
      if (container.getItem(slot).is(item)) {
        found += container.getItem(slot).getCount();
      }
    }
    return found;
  }
}
