package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.config.CubitConfig;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.entity.RobotLooks;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The modules take a robot up one tier at a time, keeping what it carries (answers 93 and 800). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class TierUpgradeGameTests {
  private static final String EMPTY_TEMPLATE = "empty";
  private static final String ITEM_TEXTURES = "/assets/cubit/textures/item/";
  private static final String PNG = ".png";
  private static final String COLLAR_LAYER = "general_robot_collar";
  private static final String NO_FILE = "Missing picture: ";
  private static final String BAD_FILE = "A picture could not be read: ";
  private static final String WRONG_TIER = "Wrong tier";
  private static final String WRONG_COUNT = "Wrong number of modules in the hand";
  private static final String WRONG_HEALTH = "Wrong health";
  private static final String WRONG_MAX_HEALTH = "Wrong maximum health";
  private static final String WRONG_CHARGE = "The charge should stay";
  private static final String WRONG_CAPACITY = "Wrong battery capacity";
  private static final String WRONG_SLOTS = "Wrong number of slots";
  private static final String WRONG_ITEM = "An item moved or disappeared";
  private static final String WRONG_STATE = "The robot should be switched off";
  private static final String WRONG_FACE = "Wrong expression";
  private static final String WRONG_COLOR = "Wrong color of the module name";

  private static final float ADVANCED_MAX_HEALTH = 50f;
  private static final int ADVANCED_SLOTS = 36;
  private static final Energy ADVANCED_BATTERY = new Energy(600_000);
  private static final int ADVANCED_NAME_COLOR = 0x3B82F6;
  private static final int LAST_BASE_SLOT = 26;
  private static final int DIAMONDS = 5;
  private static final int AFTER_THE_PLACED_FACE = 70;
  private static final int SETTLE_TICKS = 3;
  private static final int TIMEOUT = 200;
  private static final BlockPos FLOOR = new BlockPos(1, 1, 2);
  private static final BlockPos SECOND_FLOOR = new BlockPos(2, 1, 2);

  private TierUpgradeGameTests() {}

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theNextModuleUpgradesAndIsConsumed(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    Energy charge = robot.state().energy();
    float health = robot.getHealth();

    ItemStack left = GameTestRobots.clickRobot(owner, robot, module(CubitItems.ADVANCED_MODULE));

    helper.assertValueEqual(robot.state().tier(), RobotTier.ADVANCED, WRONG_TIER);
    helper.assertTrue(left.isEmpty(), WRONG_COUNT);
    helper.assertValueEqual(robot.getMaxHealth(), ADVANCED_MAX_HEALTH, WRONG_MAX_HEALTH);
    helper.assertValueEqual(robot.getHealth(), health, WRONG_HEALTH);
    helper.assertValueEqual(robot.state().energy(), charge, WRONG_CHARGE);
    helper.assertValueEqual(
        robot.state().batteryFor(spec(RobotTier.ADVANCED)).capacity(),
        ADVANCED_BATTERY,
        WRONG_CAPACITY);
    helper.assertValueEqual(robot.inventory().getContainerSize(), ADVANCED_SLOTS, WRONG_SLOTS);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aModuleThatSkipsATierStaysInTheHand(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    ItemStack left = GameTestRobots.clickRobot(owner, robot, module(CubitItems.ELITE_MODULE));

    helper.assertValueEqual(robot.state().tier(), RobotTier.BASE, WRONG_TIER);
    helper.assertValueEqual(left.getCount(), 1, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aModuleOfTheSameTierStaysInTheHand(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    GameTestRobots.clickRobot(owner, robot, module(CubitItems.ADVANCED_MODULE));

    ItemStack left = GameTestRobots.clickRobot(owner, robot, module(CubitItems.ADVANCED_MODULE));

    helper.assertValueEqual(robot.state().tier(), RobotTier.ADVANCED, WRONG_TIER);
    helper.assertValueEqual(left.getCount(), 1, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aModuleOfAStrangerDoesNothing(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    ItemStack left = GameTestRobots.clickRobot(stranger, robot, module(CubitItems.ADVANCED_MODULE));

    helper.assertValueEqual(robot.state().tier(), RobotTier.BASE, WRONG_TIER);
    helper.assertValueEqual(left.getCount(), 1, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aSwitchedOffRobotIsUpgraded(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);
    helper.assertValueEqual(second.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);

    GameTestRobots.clickRobot(owner, second, module(CubitItems.ADVANCED_MODULE));

    helper.assertValueEqual(second.state().tier(), RobotTier.ADVANCED, WRONG_TIER);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void upgradingKeepsTheItemsAndTheItemKeepsTheTier(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.inventory().setItem(0, new ItemStack(Items.DIAMOND, DIAMONDS));
    robot.inventory().setItem(LAST_BASE_SLOT, new ItemStack(Items.TORCH));

    GameTestRobots.clickRobot(owner, robot, module(CubitItems.ADVANCED_MODULE));

    helper.assertValueEqual(robot.inventory().getItem(0).getCount(), DIAMONDS, WRONG_ITEM);
    helper.assertTrue(robot.inventory().getItem(LAST_BASE_SLOT).is(Items.TORCH), WRONG_ITEM);
    ItemStack collected = collect(owner, robot);
    helper.assertValueEqual(TierLook.tierOf(collected), RobotTier.ADVANCED, WRONG_TIER);
    RobotEntity again = GameTestRobots.placeRobot(helper, owner, collected, SECOND_FLOOR);
    helper.assertValueEqual(again.inventory().getContainerSize(), ADVANCED_SLOTS, WRONG_SLOTS);
    helper.assertTrue(again.inventory().getItem(LAST_BASE_SLOT).is(Items.TORCH), WRONG_ITEM);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = TIMEOUT)
  public static void anActiveRobotIsHappyAndTheClientSeesTheTier(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.runAfterDelay(
        AFTER_THE_PLACED_FACE,
        () -> {
          GameTestRobots.clickRobot(owner, robot, module(CubitItems.ADVANCED_MODULE));
          helper.runAfterDelay(
              SETTLE_TICKS,
              () -> {
                helper.assertValueEqual(
                    RobotFace.expression(robot), RobotExpression.HAPPY, WRONG_FACE);
                helper.assertValueEqual(RobotLooks.tier(robot), RobotTier.ADVANCED, WRONG_TIER);
                helper.succeed();
              });
        });
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theModuleNameHasTheColorOfTheTier(GameTestHelper helper) {
    ItemStack stack = module(CubitItems.ADVANCED_MODULE);

    TextColor color = stack.getHoverName().getStyle().getColor();

    helper.assertValueEqual(color, TextColor.fromRgb(ADVANCED_NAME_COLOR), WRONG_COLOR);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void everyModuleAndTheCollarLayerHaveAPicture(GameTestHelper helper) {
    for (Supplier<Item> module :
        List.of(CubitItems.ADVANCED_MODULE, CubitItems.ELITE_MODULE, CubitItems.ULTIMATE_MODULE)) {
      assertFile(helper, module.get().builtInRegistryHolder().key().location().getPath());
    }
    assertFile(helper, COLLAR_LAYER);
    helper.succeed();
  }

  private static ItemStack module(Supplier<Item> item) {
    return new ItemStack(item.get());
  }

  private static TierSpec spec(RobotTier tier) {
    return CubitMod.CONFIG.tierCatalog().spec(CubitConfig.GENERAL_ROBOT, tier);
  }

  private static ItemStack collect(Player owner, RobotEntity robot) {
    owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    owner.setShiftKeyDown(true);
    robot.interact(owner, InteractionHand.MAIN_HAND);
    owner.setShiftKeyDown(false);
    return owner.getInventory().getItem(0);
  }

  private static void assertFile(GameTestHelper helper, String name) {
    String path = ITEM_TEXTURES + name + PNG;
    try (InputStream file = TierUpgradeGameTests.class.getResourceAsStream(path)) {
      helper.assertTrue(file != null, NO_FILE + path);
    } catch (IOException exception) {
      helper.fail(BAD_FILE + path);
    }
  }
}
