package io.github.marcelofabianov.cubit.gametest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.behavior.ContinuousSound;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotAudio;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** A dedicated server hears nothing, so these check what reaches the client: names and state. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RobotSoundGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 200;
  private static final int FLOOR_Y = 1;
  private static final int START_X = 1;
  private static final int MIDDLE_Z = 2;
  private static final int FAR_X = 18;
  private static final int SLOT = 0;
  private static final int SOME_DUST = 8;
  private static final int FIRE_SECONDS = 8;
  private static final long CAPACITY = 300_000;
  private static final long LOW_ENERGY = 1_000;
  private static final long DOCK_ENERGY = 50_000;
  private static final float HEALTH = 25f;
  private static final BlockPos FLOOR = new BlockPos(START_X, FLOOR_Y, MIDDLE_Z);
  private static final String SOUNDS_FILE = "/assets/cubit/sounds.json";
  private static final String TRACKS_SOUND = "general_robot.tracks";
  private static final String HOSTILE_MOB_ALERT_SOUND = "general_robot.hostile_mob_alert";
  private static final String RARE_ORE_ALERT_SOUND = "general_robot.rare_ore_alert";
  private static final String EXPRESSION_BEEP_SOUND = "general_robot.expression_beep";
  private static final String SHORT_CIRCUIT_SOUND = "general_robot.short_circuit";
  private static final String POWER_ON_SOUND = "general_robot.power_on";
  private static final String POWER_OFF_SOUND = "general_robot.power_off";
  private static final String RECHARGING_SOUND = "general_robot.recharging";
  private static final String PLACED_SOUND = "general_robot.placed";
  private static final List<String> SOUND_NAMES =
      List.of(
          TRACKS_SOUND,
          HOSTILE_MOB_ALERT_SOUND,
          RARE_ORE_ALERT_SOUND,
          EXPRESSION_BEEP_SOUND,
          SHORT_CIRCUIT_SOUND,
          POWER_ON_SOUND,
          POWER_OFF_SOUND,
          RECHARGING_SOUND,
          PLACED_SOUND);
  private static final String NOT_REGISTERED = "Sound not registered: ";
  private static final String NO_FILE = "sounds.json is missing";
  private static final String NOT_DEFINED = "Sound not in sounds.json: ";
  private static final String BAD_FILE = "sounds.json could not be read";
  private static final String PLAYING_WHILE_FULL = "A full battery does not recharge";
  private static final String PLAYING_WITHOUT_ENERGY = "An empty dock does not recharge";

  private RobotSoundGameTests() {}

  @GameTest(template = ARENA)
  public static void theNineSoundsAreRegistered(GameTestHelper helper) {
    for (String name : SOUND_NAMES) {
      ResourceLocation id = ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, name);
      helper.assertTrue(BuiltInRegistries.SOUND_EVENT.containsKey(id), NOT_REGISTERED + name);
    }
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theSoundsFileDefinesTheNineSounds(GameTestHelper helper) {
    try (InputStream file = RobotSoundGameTests.class.getResourceAsStream(SOUNDS_FILE)) {
      helper.assertTrue(file != null, NO_FILE);
      JsonObject sounds =
          JsonParser.parseReader(new InputStreamReader(file, StandardCharsets.UTF_8))
              .getAsJsonObject();
      for (String name : SOUND_NAMES) {
        helper.assertTrue(sounds.has(name), NOT_DEFINED + name);
      }
    } catch (IOException exception) {
      helper.fail(BAD_FILE);
    }
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aWalkingRobotPlaysTheTracks(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(CAPACITY), FLOOR);
    Vec3 far = helper.absoluteVec(new Vec3(FAR_X, FLOOR_Y + 1, MIDDLE_Z));
    owner.moveTo(far.x, far.y, far.z);

    helper.succeedWhen(() -> assertPlaying(helper, robot, ContinuousSound.TRACKS));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aBurningSwitchedOffRobotPlaysTheShortCircuit(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    GameTestRobots.placeRobot(helper, owner, itemWith(CAPACITY), FLOOR);
    RobotEntity second =
        GameTestRobots.placeRobot(helper, owner, itemWith(CAPACITY), FLOOR.offset(MIDDLE_Z, 0, 0));

    second.igniteForSeconds(FIRE_SECONDS);

    helper.succeedWhen(() -> assertPlaying(helper, second, ContinuousSound.SHORT_CIRCUIT));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void redstoneInTheSlotPlaysTheRecharge(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(LOW_ENERGY), FLOOR);

    robot.energySlot().setItem(SLOT, new ItemStack(Items.REDSTONE, SOME_DUST));

    helper.succeedWhen(() -> assertPlaying(helper, robot, ContinuousSound.RECHARGING));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aFullBatteryPlaysNoRecharge(GameTestHelper helper) {
    Player owner = helper.makeMockPlayer(GameType.SURVIVAL);
    RobotEntity robot = GameTestRobots.placeRobot(helper, owner, itemWith(CAPACITY), FLOOR);

    robot.energySlot().setItem(SLOT, new ItemStack(Items.REDSTONE, SOME_DUST));

    helper.runAfterDelay(
        MIDDLE_Z,
        () -> {
          helper.assertFalse(
              RobotAudio.playing(robot).contains(ContinuousSound.RECHARGING), PLAYING_WHILE_FULL);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotOnADockWithEnergyPlaysTheRecharge(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    DockScene.placeDock(helper, owner, DockScene.FLOOR, new Energy(DOCK_ENERGY));
    RobotEntity robot = DockScene.placeOnDock(helper, owner, new Energy(LOW_ENERGY));

    helper.succeedWhen(() -> assertPlaying(helper, robot, ContinuousSound.RECHARGING));
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotOnAnEmptyDockPlaysNoRecharge(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    InductionDockBlockEntity dock =
        DockScene.placeDock(helper, owner, DockScene.FLOOR, Energy.ZERO);
    RobotEntity robot = DockScene.placeOnDock(helper, owner, new Energy(LOW_ENERGY));

    helper.assertValueEqual(dock.energy(), Energy.ZERO, PLAYING_WITHOUT_ENERGY);
    helper.runAfterDelay(
        MIDDLE_Z,
        () -> {
          helper.assertFalse(
              RobotAudio.playing(robot).contains(ContinuousSound.RECHARGING),
              PLAYING_WITHOUT_ENERGY);
          helper.succeed();
        });
  }

  private static void assertPlaying(
      GameTestHelper helper, RobotEntity robot, ContinuousSound sound) {
    helper.assertTrue(RobotAudio.playing(robot).contains(sound), sound.name());
  }

  private static ItemStack itemWith(long energy) {
    ItemStack stack = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    stack.set(
        CubitDataComponents.ROBOT_STATE.get(),
        new RobotState(RobotTier.BASE, new Energy(energy), HEALTH));
    return stack;
  }
}
