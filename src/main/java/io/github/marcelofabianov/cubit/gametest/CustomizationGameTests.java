package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.entity.RobotLooks;
import io.github.marcelofabianov.cubit.item.RobotItem;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The name of the robot, and what keeps its name and its color. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class CustomizationGameTests {
  private static final String EMPTY_TEMPLATE = "empty";
  private static final String NAME = "Bolt";
  private static final String NO_ITEM = "The robot left no item";
  private static final String WRONG_COLOR = "Wrong color";
  private static final String WRONG_COUNT = "Wrong number of items in the hand";
  private static final String WRONG_NAME = "Wrong name";
  private static final String WRONG_FACE = "Wrong expression";
  private static final String HIDDEN_NAME = "The name should be visible";
  private static final String WRONG_ICON = "Wrong icon";

  private static final int DYES = 2;
  private static final int AFTER_THE_PLACED_FACE = 70;
  private static final int SETTLE_TICKS = 3;
  private static final int TIMEOUT = 200;
  private static final BlockPos FLOOR = new BlockPos(1, 1, 2);
  private static final BlockPos SECOND_FLOOR = new BlockPos(2, 1, 2);

  private CustomizationGameTests() {}

  @GameTest(template = EMPTY_TEMPLATE, timeoutTicks = TIMEOUT)
  public static void anActiveRobotIsHappyWhenNamed(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.runAfterDelay(
        AFTER_THE_PLACED_FACE,
        () -> {
          GameTestRobots.clickRobot(owner, robot, nameTag());
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
  public static void aNameTagOfTheOwnerNamesTheRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    ItemStack tag = nameTag();

    GameTestRobots.clickRobot(owner, robot, tag);

    helper.assertValueEqual(robot.getCustomName(), name(), WRONG_NAME);
    helper.assertTrue(robot.isCustomNameVisible(), HIDDEN_NAME);
    helper.assertTrue(tag.isEmpty(), WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aNameTagOfAStrangerDoesNothing(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    Player stranger = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    ItemStack tag = nameTag();

    GameTestRobots.clickRobot(stranger, robot, tag);

    helper.assertTrue(robot.getCustomName() == null, WRONG_NAME);
    helper.assertValueEqual(tag.getCount(), 1, WRONG_COUNT);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aNamedItemNamesTheRobot(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(DataComponents.CUSTOM_NAME, name());

    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, item, FLOOR);

    helper.assertValueEqual(robot.getCustomName(), name(), WRONG_NAME);
    helper.assertTrue(robot.isCustomNameVisible(), HIDDEN_NAME);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void collectingKeepsTheColorAndTheName(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = paintedAndNamed(helper, owner);

    owner.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    owner.setShiftKeyDown(true);
    robot.interact(owner, InteractionHand.MAIN_HAND);
    owner.setShiftKeyDown(false);
    ItemStack collected = owner.getInventory().getItem(0);
    helper.assertValueEqual(RobotItem.iconColor(collected), iconOf(RobotColor.RED), WRONG_ICON);

    RobotEntity again = GameTestRobots.placeRobot(helper, owner, collected, SECOND_FLOOR);
    helper.assertValueEqual(RobotLooks.color(again), RobotColor.RED, WRONG_COLOR);
    helper.assertValueEqual(again.getCustomName(), name(), WRONG_NAME);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void dyingAndRepairingKeepTheColorAndTheName(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = paintedAndNamed(helper, owner);

    robot.kill();
    ItemStack damaged = droppedItem(helper);
    helper.assertValueEqual(damaged.get(DataComponents.CUSTOM_NAME), name(), WRONG_NAME);

    CraftingInput grid =
        CraftingInput.of(DYES, 1, List.of(damaged, new ItemStack(CubitItems.TOOLBOX.get())));
    ItemStack repaired = repair(helper, grid);
    helper.assertValueEqual(stateOf(repaired).color(), RobotColor.RED, WRONG_COLOR);
    helper.assertValueEqual(repaired.get(DataComponents.CUSTOM_NAME), name(), WRONG_NAME);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void savingKeepsTheColor(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = paintedAndNamed(helper, owner);

    RobotEntity loaded = GameTestRobots.loadRobot(helper, robot.saveWithoutId(new CompoundTag()));

    helper.assertValueEqual(RobotLooks.color(loaded), RobotColor.RED, WRONG_COLOR);
    helper.assertValueEqual(loaded.getCustomName(), name(), WRONG_NAME);
    helper.succeed();
  }

  private static RobotEntity paintedAndNamed(GameTestHelper helper, Player owner) {
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    GameTestRobots.clickRobot(owner, robot, new ItemStack(Items.RED_DYE));
    GameTestRobots.clickRobot(owner, robot, nameTag());
    return robot;
  }

  private static ItemStack nameTag() {
    ItemStack tag = new ItemStack(Items.NAME_TAG);
    tag.set(DataComponents.CUSTOM_NAME, name());
    return tag;
  }

  private static Component name() {
    return Component.literal(NAME);
  }

  private static float iconOf(RobotColor color) {
    return color.ordinal();
  }

  private static RobotState stateOf(ItemStack stack) {
    return stack.get(CubitDataComponents.ROBOT_STATE.get());
  }

  private static ItemStack droppedItem(GameTestHelper helper) {
    return helper.getEntities(EntityType.ITEM).stream()
        .map(ItemEntity::getItem)
        .filter(stack -> stack.is(CubitItems.DAMAGED_GENERAL_ROBOT.get()))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException(NO_ITEM));
  }

  private static ItemStack repair(GameTestHelper helper, CraftingInput grid) {
    return helper
        .getLevel()
        .getRecipeManager()
        .getRecipeFor(RecipeType.CRAFTING, grid, helper.getLevel())
        .map(holder -> holder.value().assemble(grid, helper.getLevel().registryAccess()))
        .orElseThrow(() -> new IllegalStateException(NO_ITEM));
  }
}
