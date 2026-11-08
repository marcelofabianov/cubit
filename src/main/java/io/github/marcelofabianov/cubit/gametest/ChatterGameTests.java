package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ChatTopic;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.entity.RobotAnimals;
import io.github.marcelofabianov.cubit.entity.RobotBiomes;
import io.github.marcelofabianov.cubit.entity.RobotChatter;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFace;
import io.github.marcelofabianov.cubit.entity.RobotNotices;
import io.github.marcelofabianov.cubit.entity.RobotScreenReadout;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.locale.Language;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The chatter on the server. The world changes are in the JUnit tests of the domain. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ChatterGameTests {
  private static final String ARENA = "arena";
  private static final List<ChatTopic> WEATHER_TOPICS =
      List.of(
          ChatTopic.DAWN,
          ChatTopic.NICE_DAY,
          ChatTopic.DUSK,
          ChatTopic.RAIN,
          ChatTopic.STORM,
          ChatTopic.COLD);
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final int FIRST_X = 3;
  private static final int SECOND_X = 6;
  private static final BlockPos FLOOR = new BlockPos(FIRST_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(SECOND_X, FLOOR_Y, MIDDLE_Z);
  private static final int NOT_HAPPY_ANYMORE_TICKS = 80;
  private static final int NEXT_TICKS = 2;
  private static final int LONG_TIMEOUT = 150;
  private static final double GETTING_FAR = 17;
  private static final double JUMPED_FAR = 40;
  private static final String CHATTER_KEY = "ChatterEnabled";
  private static final String NO_PHRASE = "A phrase has no translation: ";
  private static final BlockPos NEAR = new BlockPos(FIRST_X, FLOOR_Y + 1, MIDDLE_Z);
  private static final int SURE_ROLL = 0;
  private static final String WRONG_TOPIC = "Wrong topic of an animal";
  private static final String WRONG_GROUP = "Wrong group of a biome";
  private static final String NOT_SAID = "The robot should have congratulated";
  private static final String SAID = "The robot should have stayed quiet";
  private static final String NOT_HAPPY = "The robot should be happy";
  private static final String WRONG_SWITCH = "Wrong switch of the chatter";

  private ChatterGameTests() {}

  @GameTest(template = ARENA)
  public static void everyPhraseHasATranslation(GameTestHelper helper) {
    for (ChatTopic topic : ChatTopic.values()) {
      for (int phrase = 0; phrase < topic.phrases(); phrase++) {
        String key = RobotChatter.keyOf(topic, phrase);
        helper.assertTrue(Language.getInstance().has(key), NO_PHRASE + key);
      }
    }
    helper.succeed();
  }

  @GameTest(template = ARENA, timeoutTicks = LONG_TIMEOUT)
  public static void anAchievementMakesTheRobotHappyAndSpeak(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    helper.runAfterDelay(
        NOT_HAPPY_ANYMORE_TICKS,
        () -> {
          RobotChatter.congratulate(owner, false);
          helper.assertTrue(
              RobotNotices.isHoldingName(robot, ChatTopic.ACHIEVEMENT.name()), NOT_SAID);
        });
    helper.runAfterDelay(
        NOT_HAPPY_ANYMORE_TICKS + NEXT_TICKS,
        () -> {
          helper.assertValueEqual(RobotFace.expression(robot), RobotExpression.HAPPY, NOT_HAPPY);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void aChallengeHasItsOwnPhrases(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);

    RobotChatter.congratulate(owner, true);

    helper.assertTrue(RobotNotices.isHoldingName(robot, ChatTopic.CHALLENGE.name()), NOT_SAID);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theSwitchSilencesTheCongratulations(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.chatter().toggle();

    RobotChatter.congratulate(owner, false);

    helper.assertFalse(RobotNotices.isHoldingName(robot, ChatTopic.ACHIEVEMENT.name()), SAID);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aRobotThatDoesNotFollowStaysQuiet(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);

    RobotChatter.congratulate(owner, false);

    helper.assertFalse(RobotNotices.isHoldingName(second, ChatTopic.ACHIEVEMENT.name()), SAID);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aPlayerWithoutARobotEarnsAchievementsNormally(GameTestHelper helper) {
    RobotChatter.congratulate(GameTestRobots.onlinePlayer(helper), false);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void readingTheWorldWithoutAChangeSaysNothing(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);

    robot.chatter().readTheWorldNow();
    robot.chatter().readTheWorldNow();

    for (ChatTopic topic : WEATHER_TOPICS) {
      helper.assertFalse(RobotNotices.isHoldingName(robot, topic.name()), SAID);
    }
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void itComplainsWhenTheOwnerGetsFar(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    owner.moveTo(robot.position());
    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          owner.moveTo(robot.position().add(GETTING_FAR, 0, 0));
          helper.runAfterDelay(
              NEXT_TICKS,
              () -> {
                helper.assertTrue(
                    RobotNotices.isHoldingName(robot, ChatTopic.LEFT_BEHIND.name()), NOT_SAID);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA)
  public static void aJumpPastTheTeleportDoesNotComplain(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    owner.moveTo(robot.position());
    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          owner.moveTo(robot.position().add(JUMPED_FAR, 0, 0));
          helper.runAfterDelay(
              NEXT_TICKS,
              () -> {
                helper.assertFalse(
                    RobotNotices.isHoldingName(robot, ChatTopic.LEFT_BEHIND.name()), SAID);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA)
  public static void theSwitchSilencesTheComplaint(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    robot.chatter().toggle();
    owner.moveTo(robot.position());
    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          owner.moveTo(robot.position().add(GETTING_FAR, 0, 0));
          helper.runAfterDelay(
              NEXT_TICKS,
              () -> {
                helper.assertFalse(
                    RobotNotices.isHoldingName(robot, ChatTopic.LEFT_BEHIND.name()), SAID);
                helper.succeed();
              });
        });
  }

  @GameTest(template = ARENA)
  public static void biomesBelongToTheirGroupsByTag(GameTestHelper helper) {
    helper.assertValueEqual(
        groupOf(helper, Biomes.DESERT), Optional.of(ChatTopic.BIOME_DESERT), WRONG_GROUP);
    helper.assertValueEqual(
        groupOf(helper, Biomes.BIRCH_FOREST), Optional.of(ChatTopic.BIOME_FOREST), WRONG_GROUP);
    helper.assertValueEqual(
        groupOf(helper, Biomes.BEACH), Optional.of(ChatTopic.BIOME_OCEAN), WRONG_GROUP);
    helper.assertValueEqual(groupOf(helper, Biomes.SNOWY_PLAINS), Optional.empty(), WRONG_GROUP);
    helper.assertValueEqual(groupOf(helper, Biomes.RIVER), Optional.empty(), WRONG_GROUP);
    helper.succeed();
  }

  private static Optional<ChatTopic> groupOf(GameTestHelper helper, ResourceKey<Biome> biome) {
    return RobotBiomes.groupOf(
        helper
            .getLevel()
            .registryAccess()
            .registryOrThrow(Registries.BIOME)
            .getHolderOrThrow(biome));
  }

  @GameTest(template = ARENA)
  public static void eachAnimalHasItsTopic(GameTestHelper helper) {
    helper.assertValueEqual(
        RobotAnimals.topicOf(helper.spawnWithNoFreeWill(EntityType.PIG, NEAR)),
        ChatTopic.ANIMAL_PIG,
        WRONG_TOPIC);
    helper.assertValueEqual(
        RobotAnimals.topicOf(helper.spawnWithNoFreeWill(EntityType.FOX, NEAR)),
        ChatTopic.ANIMAL_OTHER,
        WRONG_TOPIC);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aKilledAnimalIsARemark(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);

    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          robot.chatter().remarkOn(helper.spawnWithNoFreeWill(EntityType.PIG, NEAR), SURE_ROLL);
          helper.assertTrue(
              RobotNotices.isHoldingName(robot, ChatTopic.ANIMAL_PIG.name()), NOT_SAID);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void aPetIsNeverARemark(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    Wolf wolf = helper.spawnWithNoFreeWill(EntityType.WOLF, NEAR);
    wolf.tame(owner);

    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          robot.chatter().remarkOn(wolf, SURE_ROLL);
          helper.assertFalse(
              RobotNotices.isHoldingName(robot, ChatTopic.ANIMAL_OTHER.name()), SAID);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theSwitchSilencesTheRemark(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);
    robot.chatter().toggle();

    helper.runAfterDelay(
        NEXT_TICKS,
        () -> {
          robot.chatter().remarkOn(helper.spawnWithNoFreeWill(EntityType.PIG, NEAR), SURE_ROLL);
          helper.assertFalse(RobotNotices.isHoldingName(robot, ChatTopic.ANIMAL_PIG.name()), SAID);
          helper.succeed();
        });
  }

  @GameTest(template = ARENA)
  public static void theMenuButtonTogglesTheSwitchOnlyForTheOwner(GameTestHelper helper) {
    Player owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    Player stranger = GameTestRobots.onlinePlayer(helper);
    owner.moveTo(robot.position());
    stranger.moveTo(robot.position());

    menuOf(robot, stranger).clickMenuButton(stranger, RobotMenu.CHATTER_BUTTON);
    helper.assertTrue(robot.chatter().isOn(), WRONG_SWITCH);

    menuOf(robot, owner).clickMenuButton(owner, RobotMenu.CHATTER_BUTTON);
    helper.assertFalse(robot.chatter().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void savingKeepsTheSwitch(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);
    robot.chatter().toggle();

    RobotEntity loaded = GameTestRobots.loadRobot(helper, robot.saveWithoutId(new CompoundTag()));

    helper.assertFalse(loaded.chatter().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void anOldSaveHasTheChatterOn(GameTestHelper helper) {
    RobotEntity robot =
        GameTestRobots.placeNewRobot(helper, GameTestRobots.onlinePlayer(helper), FLOOR);
    robot.chatter().toggle();
    CompoundTag saved = robot.saveWithoutId(new CompoundTag());
    saved.remove(CHATTER_KEY);

    helper.assertTrue(GameTestRobots.loadRobot(helper, saved).chatter().isOn(), WRONG_SWITCH);
    helper.succeed();
  }

  private static RobotMenu menuOf(RobotEntity robot, Player player) {
    return RobotScreenReadout.serverMenu(0, player.getInventory(), robot);
  }
}
