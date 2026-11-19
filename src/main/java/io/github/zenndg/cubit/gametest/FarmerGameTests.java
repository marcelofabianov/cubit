package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.robot.AreaCorner;
import io.github.zenndg.cubit.domain.robot.RobotState;
import io.github.zenndg.cubit.domain.robot.WorkArea;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * The work of the Farmer: it harvests and plants again, tills by the water only, plants with the
 * seeds of the chest of its area, keeps the farmland and keeps its fertilizer in the item (answers
 * 1220 to 1246).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FarmerGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 1200;
  private static final int FLOOR_Y = 1;
  private static final int PLANT_Y = 2;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(4, FLOOR_Y, 2);
  private static final BlockPos FIRST_CORNER = new BlockPos(14, FLOOR_Y, 0);
  private static final BlockPos SECOND_CORNER = new BlockPos(22, FLOOR_Y, 4);
  private static final BlockPos WATER = new BlockPos(14, FLOOR_Y, 2);
  private static final BlockPos FARMLAND = new BlockPos(15, FLOOR_Y, 2);
  private static final BlockPos NEAR_GRASS = new BlockPos(16, FLOOR_Y, 3);
  private static final BlockPos FAR_GRASS = new BlockPos(22, FLOOR_Y, 2);
  private static final BlockPos CHEST = new BlockPos(20, PLANT_Y, 0);
  private static final Energy CHARGE = new Energy(300_000);
  private static final float HEALTH = 50f;
  private static final int CARD_SLOT = 0;
  private static final int SEEDS = 16;
  private static final float FALL = 1f;
  private static final String NOT_HARVESTED = "The Farmer did not harvest and plant again";
  private static final String NOT_TILLED = "The Farmer did not till by the water";
  private static final String TILLED_FAR = "The Farmer tilled far from the water";
  private static final String NOT_PLANTED = "The Farmer did not plant the seeds of the chest";
  private static final String TRAMPLES = "The Farmer would trample the farmland";
  private static final String NOT_KEPT = "The fertilizer was not kept in the item";
  private static final String NOT_FERTILIZER = "The fertilizer slot took the wrong item";
  private static final String NOT_FERTILIZED = "The Farmer did not use the fertilizer";

  private FarmerGameTests() {}

  /** It harvests the ripe wheat, keeps the crop and plants it again (answer 1220). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFarmerHarvestsAndPlantsAgain(GameTestHelper helper) {
    helper.setBlock(WATER, Blocks.WATER);
    helper.setBlock(FARMLAND, Blocks.FARMLAND);
    helper.setBlock(FARMLAND.above(), ripeWheat());
    RobotEntity robot = farmer(helper);

    helper.succeedWhen(() -> {
      helper.assertTrue(holds(robot.inventory(), Items.WHEAT), NOT_HARVESTED);
      helper.assertBlockPresent(Blocks.WHEAT, FARMLAND.above());
    });
  }

  /** Grass by the water is tilled, and far from it is not (answer 1232). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFarmerTillsOnlyByTheWater(GameTestHelper helper) {
    helper.setBlock(WATER, Blocks.WATER);
    helper.setBlock(NEAR_GRASS, Blocks.GRASS_BLOCK);
    helper.setBlock(FAR_GRASS, Blocks.GRASS_BLOCK);
    farmer(helper);

    helper.succeedWhen(() -> {
      helper.assertBlockPresent(Blocks.FARMLAND, NEAR_GRASS);
      helper.assertBlockPresent(Blocks.GRASS_BLOCK, FAR_GRASS);
    });
  }

  /** With no seeds of its own, it fetches them from the chest of its area (answer 1234). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFarmerPlantsTheSeedsOfTheChest(GameTestHelper helper) {
    helper.setBlock(WATER, Blocks.WATER);
    helper.setBlock(FARMLAND, Blocks.FARMLAND);
    helper.setBlock(CHEST, Blocks.CHEST);
    ChestBlockEntity chest = helper.getBlockEntity(CHEST);
    chest.setItem(0, new ItemStack(Items.WHEAT_SEEDS, SEEDS));
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = farmer(helper, owner);
    robot.docking().unload().click(owner, helper.absolutePos(CHEST));

    helper.succeedWhen(() -> helper.assertBlockPresent(Blocks.WHEAT, FARMLAND.above()));
  }

  /** Only the Farmer keeps the farmland (answer 1223). */
  @GameTest(template = ARENA)
  public static void onlyTheFarmerKeepsTheFarmland(GameTestHelper helper) {
    RobotEntity robot = farmer(helper);
    BlockPos pos = helper.absolutePos(FARMLAND);
    helper.assertFalse(robot.canTrample(Blocks.FARMLAND.defaultBlockState(), pos, FALL), TRAMPLES);
    helper.succeed();
  }

  /** The plant it replants after the harvest gets the fertilizer (answers 1243 and 1248). */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theFertilizerGoesOnWhatItReplants(GameTestHelper helper) {
    helper.setBlock(WATER, Blocks.WATER);
    helper.setBlock(FARMLAND, Blocks.FARMLAND);
    helper.setBlock(FARMLAND.above(), ripeWheat());
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = farmer(helper, owner);
    Container slot = robot.workArea().work().fertilizerSlot();
    slot.setItem(0, new ItemStack(Items.BONE_MEAL, SEEDS));

    helper.succeedWhen(() -> helper.assertTrue(slot.getItem(0).getCount() < SEEDS, NOT_FERTILIZED));
  }

  /** The fertilizer slot takes only fertilizer and is kept in the item (answers 1243 and 1244). */
  @GameTest(template = ARENA)
  public static void theFertilizerSlotIsKeptInTheItem(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = robot(helper, owner, RobotTier.ULTIMATE);
    Container slot = robot.workArea().work().fertilizerSlot();
    helper.assertFalse(slot.canPlaceItem(0, new ItemStack(Items.WHEAT_SEEDS)), NOT_FERTILIZER);
    slot.setItem(0, new ItemStack(Items.BONE_MEAL, SEEDS));

    ItemStack item = GameTestRobots.collect(owner, robot);

    ItemContainerContents kept = item.get(CubitDataComponents.FERTILIZER_SLOT.get());
    helper.assertTrue(
        kept != null && kept.stream().anyMatch(stack -> stack.is(Items.BONE_MEAL)), NOT_KEPT);
    helper.succeed();
  }

  /** Fertilizer from the Advanced, the first tier of the Farmer, but never on the Base (1248). */
  @GameTest(template = ARENA)
  public static void fertilizerStartsOnTheAdvanced(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Container advanced =
        robot(helper, owner, RobotTier.ADVANCED).workArea().work().fertilizerSlot();
    Container base = robot(helper, owner, RobotTier.BASE).workArea().work().fertilizerSlot();
    helper.assertTrue(advanced.canPlaceItem(0, new ItemStack(Items.BONE_MEAL)), NOT_FERTILIZER);
    helper.assertFalse(base.canPlaceItem(0, new ItemStack(Items.BONE_MEAL)), NOT_FERTILIZER);
    helper.succeed();
  }

  private static RobotEntity farmer(GameTestHelper helper) {
    return farmer(helper, GameTestRobots.onlinePlayer(helper));
  }

  private static RobotEntity farmer(GameTestHelper helper, ServerPlayer owner) {
    return farmer(helper, owner, RobotTier.ADVANCED);
  }

  private static RobotEntity farmer(GameTestHelper helper, ServerPlayer owner, RobotTier tier) {
    RobotEntity robot = robot(helper, owner, tier);
    robot.workArea().work().slot().setItem(CARD_SLOT, new ItemStack(CubitItems.FARMER_CARD.get()));
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

  private static RobotEntity robot(GameTestHelper helper, ServerPlayer owner, RobotTier tier) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(tier, CHARGE, HEALTH));
    return GameTestRobots.placeRobot(helper, owner, item, ROBOT_FLOOR);
  }

  private static BlockState ripeWheat() {
    CropBlock wheat = (CropBlock) Blocks.WHEAT;
    return wheat.getStateForAge(wheat.getMaxAge());
  }

  private static boolean holds(Container inventory, net.minecraft.world.item.Item item) {
    for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
      if (inventory.getItem(slot).is(item)) {
        return true;
      }
    }
    return false;
  }
}
