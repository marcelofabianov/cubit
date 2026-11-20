package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.HibernationReason;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.PowerState;
import io.github.zenndg.cubit.domain.robot.AreaCorner;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.FishingCatch;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The work of the Fisher: it finds the water of its area and fishes from the table of the game,
 * hibernates with no water, leaves the junk out when asked, and keeps Keep junk in the item
 * (answers 1183 to 1191, 1207 and 1208).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FisherGameTests {
  private static final String ARENA = "arena";
  private static final String EMPTY_TEMPLATE = "empty";
  private static final int FISHING_TIMEOUT = 1600;
  private static final int SCAN_TIMEOUT = 400;
  private static final int FLOOR_Y = 1;
  private static final int WATER_Y = 2;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(4, FLOOR_Y, 2);
  private static final int POND_MIN_X = 11;
  private static final int POND_MAX_X = 15;
  private static final int POND_MIN_Z = 0;
  private static final int POND_MAX_Z = 4;
  private static final BlockPos FIRST_CORNER = new BlockPos(POND_MIN_X, WATER_Y, POND_MIN_Z);
  private static final BlockPos SECOND_CORNER = new BlockPos(POND_MAX_X, WATER_Y, POND_MAX_Z);
  private static final BlockPos DRY_FIRST = new BlockPos(18, FLOOR_Y, 0);
  private static final BlockPos DRY_SECOND = new BlockPos(22, FLOOR_Y, 4);
  private static final Energy CHARGE = new Energy(300_000);
  private static final float HEALTH = 50f;
  private static final int CARD_SLOT = 0;
  private static final int ROLLS = 200;
  private static final int MAX_LUCK = 3;
  private static final boolean CLOSED = false;
  private static final boolean KEEP = true;
  private static final boolean DROP = false;
  private static final String NOTHING_CAUGHT = "The Fisher caught nothing";
  private static final String NOT_ASLEEP = "The Fisher should hibernate with no water";
  private static final String WRONG_REASON = "Wrong reason to hibernate";
  private static final String TREASURE = "Closed water gave treasure";
  private static final String JUNK = "Junk came in with Keep junk off";
  private static final String NOT_KEPT = "Keep junk was not kept in the item";

  private FisherGameTests() {}

  /** It walks to the shore of the pond and the catches go to its inventory (answer 1188). */
  @GameTest(template = ARENA, timeoutTicks = FISHING_TIMEOUT)
  public static void aFisherFishesFromThePondOfItsArea(GameTestHelper helper) {
    pond(helper);
    RobotEntity robot = fisher(helper, FIRST_CORNER, SECOND_CORNER);

    helper.succeedWhen(() -> helper.assertTrue(caught(robot.inventory()), NOTHING_CAUGHT));
  }

  /** With no water in its area, it hibernates for that reason (answer 1188). */
  @GameTest(template = ARENA, timeoutTicks = SCAN_TIMEOUT)
  public static void aFisherWithNoWaterHibernates(GameTestHelper helper) {
    RobotEntity robot = fisher(helper, DRY_FIRST, DRY_SECOND);

    helper.succeedWhen(() -> {
      helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING_AWAY, NOT_ASLEEP);
      helper.assertValueEqual(
          robot.workArea().work().hibernation(),
          Optional.of(HibernationReason.NO_WATER),
          WRONG_REASON);
    });
  }

  /** The game gives treasure only in open water, through the bobber (answer 1183). */
  @GameTest(template = EMPTY_TEMPLATE)
  public static void closedWaterNeverGivesTreasure(GameTestHelper helper) {
    Vec3 water = helper.absoluteVec(Vec3.ZERO);
    for (int roll = 0; roll < ROLLS; roll++) {
      for (ItemStack stack : FishingCatch.roll(helper.getLevel(), water, CLOSED, MAX_LUCK, KEEP)) {
        helper.assertFalse(treasure(stack), TREASURE);
      }
    }
    helper.succeed();
  }

  /** With Keep junk off, nothing of the tag of junk comes in unless enchanted (answer 1207). */
  @GameTest(template = EMPTY_TEMPLATE)
  public static void keepJunkOffLeavesTheJunkOut(GameTestHelper helper) {
    Vec3 water = helper.absoluteVec(Vec3.ZERO);
    for (int roll = 0; roll < ROLLS; roll++) {
      for (ItemStack stack : FishingCatch.roll(helper.getLevel(), water, CLOSED, 0, DROP)) {
        helper.assertFalse(stack.is(FishingCatch.JUNK) && !stack.isEnchanted(), JUNK);
      }
    }
    helper.succeed();
  }

  /** Keep junk off survives picking the Cubit up (answer 1191). */
  @GameTest(template = ARENA)
  public static void keepJunkStaysInTheItem(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = advanced(helper, owner);
    robot.workArea().work().fishing().setKeepJunk(false);

    ItemStack item = GameTestRobots.collect(owner, robot);

    RobotState kept = item.get(CubitDataComponents.ROBOT_STATE.get());
    helper.assertTrue(kept != null && !kept.workSettings().keepJunk(), NOT_KEPT);
    helper.succeed();
  }

  /** Still water one block deep, inside a ring of stone the Fisher stands on. */
  private static void pond(GameTestHelper helper) {
    for (int x = POND_MIN_X; x <= POND_MAX_X; x++) {
      for (int z = POND_MIN_Z; z <= POND_MAX_Z; z++) {
        boolean ring = x == POND_MIN_X || x == POND_MAX_X || z == POND_MIN_Z || z == POND_MAX_Z;
        helper.setBlock(new BlockPos(x, WATER_Y, z), ring ? Blocks.STONE : Blocks.WATER);
      }
    }
  }

  private static RobotEntity fisher(GameTestHelper helper, BlockPos first, BlockPos second) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = advanced(helper, owner);
    robot.workArea().work().slot().setItem(CARD_SLOT, new ItemStack(CubitItems.FISHER_CARD.get()));
    robot.workArea().assign(area(helper, robot, first, second), owner);
    return robot;
  }

  private static RobotEntity advanced(GameTestHelper helper, ServerPlayer owner) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.ADVANCED, CHARGE, HEALTH));
    return GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
  }

  private static WorkArea area(
      GameTestHelper helper, RobotEntity robot, BlockPos first, BlockPos second) {
    BlockPos one = helper.absolutePos(first);
    BlockPos two = helper.absolutePos(second);
    return WorkArea.between(
        new AreaCorner(one.getX(), one.getY(), one.getZ()),
        new AreaCorner(two.getX(), two.getY(), two.getZ()),
        robot.level().dimension().location().toString());
  }

  private static boolean caught(Container inventory) {
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      if (!inventory.getItem(slot).isEmpty()) {
        return true;
      }
    }
    return false;
  }

  private static boolean treasure(ItemStack stack) {
    return List.of(Items.NAME_TAG, Items.SADDLE, Items.NAUTILUS_SHELL, Items.ENCHANTED_BOOK)
            .contains(stack.getItem())
        || stack.is(Items.BOW)
        || (stack.is(Items.FISHING_ROD) && stack.isEnchanted());
  }
}
