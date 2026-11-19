package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.robot.AreaCorner;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.robot.UnloadInterval;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.domain.robot.WorkSettings;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The unload interval of a Cubit in its area: it unloads after the time even when not full, the
 * buttons answer only the owner, and the choice stays in the item (answers 1250 to 1254).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class UnloadIntervalGameTests {
  private static final String ARENA = "arena";
  private static final int ONE_MINUTE_AND_SOME = 1600;
  private static final int FLOOR_Y = 1;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(4, FLOOR_Y, 2);
  private static final BlockPos FIRST_CORNER = new BlockPos(2, FLOOR_Y, 0);
  private static final BlockPos SECOND_CORNER = new BlockPos(8, FLOOR_Y, 4);
  private static final BlockPos CHEST = new BlockPos(6, FLOOR_Y + 1, 0);
  private static final Energy CHARGE = new Energy(300_000);
  private static final float HEALTH = 50f;
  private static final int SOME = 8;
  private static final int UP = 1;
  private static final UnloadInterval ONE_MINUTE = new UnloadInterval(1);
  private static final String NOT_UNLOADED = "The Cubit did not unload after its interval";
  private static final String STRANGER_CHANGED = "Someone else changed the interval";
  private static final String NOT_KEPT = "The interval was not kept in the item";

  private UnloadIntervalGameTests() {}

  /** A Cubit in its area, far from full, unloads once the minute passes (answer 1250). */
  @GameTest(template = ARENA, timeoutTicks = ONE_MINUTE_AND_SOME)
  public static void aCubitUnloadsAfterItsInterval(GameTestHelper helper) {
    helper.setBlock(CHEST, Blocks.CHEST);
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner);
    robot.docking().unload().setInterval(ONE_MINUTE);
    robot.docking().unload().click(owner, helper.absolutePos(CHEST));
    robot.inventory().setItem(0, new ItemStack(Items.WHEAT, SOME));
    robot.workArea().assign(area(helper, robot), owner);
    ChestBlockEntity chest = helper.getBlockEntity(CHEST);

    helper.succeedWhen(() -> helper.assertTrue(chest.getItem(0).is(Items.WHEAT), NOT_UNLOADED));
  }

  /** Only the owner steps the interval (rule 20). */
  @GameTest(template = ARENA)
  public static void onlyTheOwnerChangesTheInterval(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);

    helper.assertFalse(robot.docking().unload().stepInterval(stranger, UP), STRANGER_CHANGED);
    helper.assertValueEqual(
        robot.docking().unload().interval(), UnloadInterval.CONFIG_DEFAULT, STRANGER_CHANGED);
    helper.succeed();
  }

  /** The chosen interval stays in the item (answer 1254). */
  @GameTest(template = ARENA)
  public static void theIntervalStaysInTheItem(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner);
    robot.docking().unload().setInterval(ONE_MINUTE);

    ItemStack item = GameTestRobots.collect(owner, robot);

    RobotState kept = item.get(CubitDataComponents.ROBOT_STATE.get());
    WorkSettings settings = kept == null ? WorkSettings.INITIAL : kept.workSettings();
    helper.assertValueEqual(settings.unloadInterval(), ONE_MINUTE, NOT_KEPT);
    helper.succeed();
  }

  private static RobotEntity robot(GameTestHelper helper, ServerPlayer owner) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.ADVANCED, CHARGE, HEALTH));
    return GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
  }

  private static WorkArea area(GameTestHelper helper, RobotEntity robot) {
    BlockPos one = helper.absolutePos(FIRST_CORNER);
    BlockPos two = helper.absolutePos(SECOND_CORNER);
    return WorkArea.between(
        new AreaCorner(one.getX(), one.getY(), one.getZ()),
        new AreaCorner(two.getX(), two.getY(), two.getZ()),
        robot.level().dimension().location().toString());
  }
}
