package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotLamp;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.item.RobotStateComponent;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The server decides when the lamp lights and what it costs. The light itself is client only. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class LampGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int CHECKED_TICKS = 45;
  private static final int LIT_TICKS = 60;
  private static final int COST_TICKS = 20;
  private static final int NEXT_TICKS = 2;
  private static final long COST_OF_THE_LAMP = 20;
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final int ROOM_X = 3;
  private static final int ROOM_HEIGHT = 2;
  private static final BlockPos FLOOR = new BlockPos(ROOM_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(ROOM_X + ROOM_X, FLOOR_Y, MIDDLE_Z);
  private static final int LAMP_LIGHT = 10;
  private static final int SCREEN_LIGHT = 4;
  private static final int NO_LIGHT = 0;
  private static final int UNKNOWN_BUTTON = 7;
  private static final double FAR_AWAY = 20;
  private static final float HEALTH = 25f;
  private static final Energy PLENTY = new Energy(100_000);
  private static final String LAMP_KEY = "LampEnabled";
  private static final String TIER_FIELD = "tier";
  private static final String ENERGY_FIELD = "energy";
  private static final String HEALTH_FIELD = "health";
  private static final String NOT_LIT = "The lamp should be lit";
  private static final String LIT = "The lamp should be out";
  private static final String WRONG_LIGHT = "Wrong light level";
  private static final String WRONG_COST = "Wrong cost of the lamp";
  private static final String SPENT = "The robot spent energy";
  private static final String SWITCH_LOST = "The switch of the lamp was not kept";
  private static final String SWITCH_CHANGED = "The switch of the lamp changed";
  private static final String SWITCH_KEPT = "The switch of the lamp did not change";
  private static final String NOT_ACTIVE = "The robot should be active";
  private static final String NOT_HIBERNATING = "The robot should be hibernating";
  private static final String NO_REPAIR = "The repair recipe did not match";

  private LampGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void lightsInADarkRoomAndPaysForIt(GameTestHelper helper) {
    RobotEntity robot = placeInADarkRoom(helper, PLENTY);

    helper.runAfterDelay(
        LIT_TICKS,
        () -> {
          helper.assertTrue(RobotLamp.isLit(robot), NOT_LIT);
          helper.assertValueEqual(RobotLamp.lightLevel(robot), LAMP_LIGHT, WRONG_LIGHT);
          long before = robot.state().energy().amount();
          helper.runAfterDelay(
              COST_TICKS,
              () -> {
                long spent = before - robot.state().energy().amount();
                helper.assertValueEqual(spent, COST_OF_THE_LAMP, WRONG_COST);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT, batch = SolarGameTests.DAYLIGHT_BATCH)
  public static void staysOutInTheOpenDuringTheDay(GameTestHelper helper) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), PLENTY, FLOOR);

    helper.runAfterDelay(
        CHECKED_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, NOT_ACTIVE);
          helper.assertTrue(!RobotLamp.isLit(robot), LIT);
          helper.assertValueEqual(RobotLamp.lightLevel(robot), SCREEN_LIGHT, WRONG_LIGHT);
          // The panel may add energy here, and the lamp must not take any.
          helper.assertTrue(robot.state().energy().amount() >= PLENTY.amount(), SPENT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aBlockThatGivesLightKeepsItOut(GameTestHelper helper) {
    RobotEntity robot = placeInADarkRoom(helper, PLENTY);
    helper.setBlock(FLOOR.above().east(), Blocks.GLOWSTONE);

    helper.runAfterDelay(
        LIT_TICKS,
        () -> {
          helper.assertTrue(!RobotLamp.isLit(robot), LIT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theSwitchKeepsItOutInTheDark(GameTestHelper helper) {
    RobotEntity robot = placeInADarkRoom(helper, PLENTY);
    robot.lampSwitch().toggle();

    helper.runAfterDelay(
        LIT_TICKS,
        () -> {
          helper.assertTrue(!RobotLamp.isLit(robot), LIT);
          helper.assertValueEqual(RobotLamp.lightLevel(robot), SCREEN_LIGHT, WRONG_LIGHT);
          helper.assertValueEqual(robot.state().energy(), PLENTY, SPENT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void theSwitchPutsItOutWithoutWaitingForTheInterval(GameTestHelper helper) {
    RobotEntity robot = placeInADarkRoom(helper, PLENTY);

    helper.runAfterDelay(
        LIT_TICKS,
        () -> {
          helper.assertTrue(RobotLamp.isLit(robot), NOT_LIT);
          robot.lampSwitch().toggle();
          helper.runAfterDelay(
              NEXT_TICKS,
              () -> {
                helper.assertTrue(!RobotLamp.isLit(robot), LIT);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotWithoutEnergyGivesNoLight(GameTestHelper helper) {
    RobotEntity robot = placeInADarkRoom(helper, Energy.ZERO);

    helper.runAfterDelay(
        LIT_TICKS,
        () -> {
          helper.assertTrue(!RobotLamp.isLit(robot), LIT);
          helper.assertValueEqual(RobotLamp.lightLevel(robot), NO_LIGHT, WRONG_LIGHT);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void hibernatingKeepsTheScreenLightAndLocksTheSwitch(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, DockScene.FLOOR, PLENTY);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, PLENTY);
    RobotMenu menu = menuOf(robot, owner);

    helper.runAfterDelay(
        CHECKED_TICKS,
        () -> {
          helper.assertValueEqual(robot.powerState(), PowerState.HIBERNATING, NOT_HIBERNATING);
          helper.assertTrue(!RobotLamp.isLit(robot), LIT);
          helper.assertValueEqual(RobotLamp.lightLevel(robot), SCREEN_LIGHT, WRONG_LIGHT);
          helper.assertTrue(!menu.clickMenuButton(owner, RobotMenu.LAMP_BUTTON), SWITCH_CHANGED);
          helper.assertTrue(robot.lampSwitch().isOn(), SWITCH_CHANGED);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theOwnerNearbyFlipsTheSwitch(GameTestHelper helper) {
    ServerPlayer owner = ownerNear(helper);
    RobotEntity robot = place(helper, owner, PLENTY, FLOOR);
    RobotMenu menu = menuOf(robot, owner);

    helper.assertTrue(menu.clickMenuButton(owner, RobotMenu.LAMP_BUTTON), SWITCH_KEPT);
    helper.assertTrue(!robot.lampSwitch().isOn(), SWITCH_KEPT);
    helper.assertTrue(!menu.clickMenuButton(owner, UNKNOWN_BUTTON), SWITCH_CHANGED);
    helper.assertTrue(!robot.lampSwitch().isOn(), SWITCH_CHANGED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aStrangerCannotFlipTheSwitch(GameTestHelper helper) {
    ServerPlayer owner = ownerNear(helper);
    RobotEntity robot = place(helper, owner, PLENTY, FLOOR);
    Player stranger = GameTestRobots.onlinePlayer(helper);
    stranger.moveTo(owner.getX(), owner.getY(), owner.getZ());

    helper.assertTrue(
        !menuOf(robot, stranger).clickMenuButton(stranger, RobotMenu.LAMP_BUTTON), SWITCH_CHANGED);
    helper.assertTrue(robot.lampSwitch().isOn(), SWITCH_CHANGED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theOwnerFarAwayCannotFlipTheSwitch(GameTestHelper helper) {
    ServerPlayer owner = ownerNear(helper);
    RobotEntity robot = place(helper, owner, PLENTY, FLOOR);
    RobotMenu menu = menuOf(robot, owner);
    owner.moveTo(owner.getX() + FAR_AWAY, owner.getY(), owner.getZ());

    helper.assertTrue(!menu.clickMenuButton(owner, RobotMenu.LAMP_BUTTON), SWITCH_CHANGED);
    helper.assertTrue(robot.lampSwitch().isOn(), SWITCH_CHANGED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void collectingAndPlacingKeepTheSwitch(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = place(helper, owner, PLENTY, FLOOR);
    robot.lampSwitch().toggle();

    owner.setShiftKeyDown(true);
    robot.interact(owner, InteractionHand.MAIN_HAND);
    owner.setShiftKeyDown(false);

    ItemStack collected = owner.getInventory().getItem(owner.getInventory().selected);
    RobotEntity again = GameTestRobots.placeRobot(helper, owner, collected, SECOND_FLOOR);
    helper.assertTrue(!again.lampSwitch().isOn(), SWITCH_LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void deathAndRepairKeepTheSwitch(GameTestHelper helper) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), PLENTY, FLOOR);
    robot.lampSwitch().toggle();

    robot.kill();

    ItemStack damaged = helper.getEntities(EntityType.ITEM).getFirst().getItem();
    helper.assertTrue(!stateOf(damaged).lampOn(), SWITCH_LOST);
    CraftingInput input =
        CraftingInput.of(2, 1, List.of(damaged, new ItemStack(CubitItems.TOOLBOX.get())));
    ItemStack repaired =
        helper
            .getLevel()
            .getRecipeManager()
            .getRecipeFor(RecipeType.CRAFTING, input, helper.getLevel())
            .map(holder -> holder.value().assemble(input, helper.getLevel().registryAccess()))
            .orElseThrow(() -> new IllegalStateException(NO_REPAIR));
    helper.assertTrue(!stateOf(repaired).lampOn(), SWITCH_LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void savingKeepsTheSwitch(GameTestHelper helper) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), PLENTY, FLOOR);
    robot.lampSwitch().toggle();

    RobotEntity loaded = load(helper, robot.saveWithoutId(new CompoundTag()));

    helper.assertTrue(!loaded.lampSwitch().isOn(), SWITCH_LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aRobotSavedBeforeTheLampHasItOn(GameTestHelper helper) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), PLENTY, FLOOR);
    robot.lampSwitch().toggle();
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    saved.remove(LAMP_KEY);

    helper.assertTrue(load(helper, saved).lampSwitch().isOn(), SWITCH_LOST);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void anItemSavedBeforeTheLampHasItOn(GameTestHelper helper) {
    CompoundTag saved = new CompoundTag();
    saved.putString(TIER_FIELD, RobotTier.BASE.key());
    saved.putLong(ENERGY_FIELD, PLENTY.amount());
    saved.putFloat(HEALTH_FIELD, HEALTH);

    RobotState state =
        RobotStateComponent.CODEC
            .parse(NbtOps.INSTANCE, saved)
            .getOrThrow(IllegalStateException::new);

    helper.assertTrue(state.lampOn(), SWITCH_LOST);
    helper.succeed();
  }

  private static RobotState stateOf(ItemStack stack) {
    return stack.get(CubitDataComponents.ROBOT_STATE.get());
  }

  private static RobotEntity load(GameTestHelper helper, CompoundTag saved) {
    RobotEntity loaded = CubitEntities.GENERAL_ROBOT.get().create(helper.getLevel());
    loaded.load(saved);
    return loaded;
  }

  private static ServerPlayer ownerNear(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 near = helper.absoluteVec(Vec3.atBottomCenterOf(FLOOR.above().west()));
    owner.moveTo(near.x, near.y, near.z);
    return owner;
  }

  private static RobotMenu menuOf(RobotEntity robot, Player player) {
    return RobotScreenReadout.serverMenu(0, player.getInventory(), robot);
  }

  private static RobotEntity place(
      GameTestHelper helper, Player owner, Energy energy, BlockPos floor) {
    return DockScene.placeRobot(helper, owner, floor, energy);
  }

  /** The room goes up after the robot, because a robot cannot be placed inside closed walls. */
  private static RobotEntity placeInADarkRoom(GameTestHelper helper, Energy energy) {
    RobotEntity robot = place(helper, helper.makeMockPlayer(GameType.SURVIVAL), energy, FLOOR);
    BlockPos middle = FLOOR.above();
    for (int x = -1; x <= 1; x++) {
      for (int z = -1; z <= 1; z++) {
        helper.setBlock(middle.offset(x, ROOM_HEIGHT, z), Blocks.STONE);
        for (int y = 0; y < ROOM_HEIGHT; y++) {
          if (x != 0 || z != 0) {
            helper.setBlock(middle.offset(x, y, z), Blocks.STONE);
          }
        }
      }
    }
    return robot;
  }
}
