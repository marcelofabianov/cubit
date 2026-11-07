package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.entity.RobotLooks;
import io.github.marcelofabianov.cubit.item.RobotStateComponent;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A dedicated server draws nothing, so these check the rules of the color, its saves and files. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ColorGameTests {
  private static final String EMPTY_TEMPLATE = "empty";
  private static final String COLOR_KEY = "Color";
  private static final String INVALID_COLOR = "plaid";
  private static final String TIER_FIELD = "tier";
  private static final String ENERGY_FIELD = "energy";
  private static final String HEALTH_FIELD = "health";
  private static final String COLOR_FIELD = "color";
  private static final String ENTITY_PICTURE = "/assets/cubit/textures/entity/general_robot_color/";
  private static final String ITEM_PICTURE = "/assets/cubit/textures/item/general_robot_color/";
  private static final String PICTURE_FILE = ".png";
  private static final String NO_FILE = "Missing picture: ";
  private static final String BAD_FILE = "A picture could not be read: ";
  private static final String WRONG_COLOR = "Wrong color";
  private static final String WRONG_COUNT = "Wrong number of items in the hand";
  private static final String WRONG_FACE = "Wrong expression";

  private static final int DYES = 2;
  private static final int AFTER_THE_PLACED_FACE = 70;
  private static final int SETTLE_TICKS = 3;
  private static final int TIMEOUT = 200;
  private static final float HEALTH = 25f;
  private static final BlockPos FLOOR = new BlockPos(1, 1, 2);

  private ColorGameTests() {}

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aDyePaintsTheRobotAndIsSpent(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    ItemStack dyes = GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.RED_DYE, DYES));

    helper.assertValueEqual(RobotLooks.color(robot), RobotColor.RED, WRONG_COLOR);
    helper.assertValueEqual(dyes.getCount(), 1, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theSameColorSpendsNothing(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    ItemStack dyes = GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.WHITE_DYE, DYES));

    helper.assertValueEqual(RobotLooks.color(robot), RobotColor.WHITE, WRONG_COLOR);
    helper.assertValueEqual(dyes.getCount(), DYES, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void whiteBringsTheOriginalLookBack(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.RED_DYE));
    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.WHITE_DYE));

    helper.assertValueEqual(RobotLooks.color(robot), RobotColor.WHITE, WRONG_COLOR);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void creativeSpendsNoDye(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.CREATIVE);
    // The simulated player only answers to being creative, and the game reads this ability.
    owner.getAbilities().instabuild = true;
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    ItemStack dyes = GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.RED_DYE, DYES));

    helper.assertValueEqual(RobotLooks.color(robot), RobotColor.RED, WRONG_COLOR);
    helper.assertValueEqual(dyes.getCount(), DYES, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void onlyTheOwnerPaints(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    ItemStack dyes = GameTestRobots.clickRobot(stranger, robot, new ItemStack(Items.RED_DYE));

    helper.assertValueEqual(RobotLooks.color(robot), RobotColor.WHITE, WRONG_COLOR);
    helper.assertValueEqual(dyes.getCount(), 1, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aRobotWithoutPowerIsPaintedAndStaysDark(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = placeWithoutPower(helper, owner);

    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.RED_DYE));

    helper.runAfterDelay(
        SETTLE_TICKS,
        () -> {
          helper.assertValueEqual(RobotLooks.color(robot), RobotColor.RED, WRONG_COLOR);
          helper.assertValueEqual(RobotFace.expression(robot), RobotExpression.OFF, WRONG_FACE);
          helper.succeed();
        });
  }

  @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = TIMEOUT)
  public static void anActiveRobotIsHappyWhenPainted(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.runAfterDelay(
        AFTER_THE_PLACED_FACE,
        () -> {
          helper.assertValueEqual(RobotFace.expression(robot), RobotExpression.NORMAL, WRONG_FACE);
          GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.RED_DYE));
          helper.runAfterDelay(
              SETTLE_TICKS,
              () -> {
                helper.assertValueEqual(
                    RobotFace.expression(robot), RobotExpression.HAPPY, WRONG_FACE);
                helper.succeed();
              });
        });
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void anOldOrInvalidSaveIsWhite(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.RED_DYE));
    CompoundTag old = robot.saveWithoutId(new CompoundTag());
    old.remove(COLOR_KEY);
    CompoundTag invalid = robot.saveWithoutId(new CompoundTag());
    invalid.putString(COLOR_KEY, INVALID_COLOR);

    helper.assertValueEqual(
        RobotLooks.color(GameTestRobots.loadRobot(helper, old)), RobotColor.WHITE, WRONG_COLOR);
    helper.assertValueEqual(
        RobotLooks.color(GameTestRobots.loadRobot(helper, invalid)), RobotColor.WHITE, WRONG_COLOR);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void anOldOrInvalidItemIsWhite(GameTestHelper helper) {
    CompoundTag old = new CompoundTag();
    old.putString(TIER_FIELD, RobotTier.BASE.key());
    old.putLong(ENERGY_FIELD, 1);
    old.putFloat(HEALTH_FIELD, HEALTH);
    CompoundTag invalid = old.copy();
    invalid.putString(COLOR_FIELD, INVALID_COLOR);

    helper.assertValueEqual(parse(old).color(), RobotColor.WHITE, WRONG_COLOR);
    helper.assertValueEqual(parse(invalid).color(), RobotColor.WHITE, WRONG_COLOR);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void everyColorHasItsPictures(GameTestHelper helper) {
    for (RobotColor color : RobotColor.values()) {
      if (color != RobotColor.WHITE) {
        assertPictureExists(helper, ENTITY_PICTURE + color.key() + PICTURE_FILE);
        assertPictureExists(helper, ITEM_PICTURE + color.key() + PICTURE_FILE);
      }
    }
    helper.succeed();
  }

  private static RobotEntity placeWithoutPower(GameTestHelper helper, Player owner) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(
        CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.BASE, Energy.ZERO, HEALTH));
    return GameTestRobots.placeRobot(helper, owner, item, FLOOR);
  }

  private static RobotState parse(CompoundTag tag) {
    return RobotStateComponent.CODEC
        .parse(NbtOps.INSTANCE, tag)
        .getOrThrow(IllegalStateException::new);
  }

  private static void assertPictureExists(GameTestHelper helper, String path) {
    try (InputStream picture = ColorGameTests.class.getResourceAsStream(path)) {
      helper.assertTrue(picture != null, NO_FILE + path);
    } catch (IOException e) {
      throw new IllegalStateException(BAD_FILE + path, e);
    }
  }
}
