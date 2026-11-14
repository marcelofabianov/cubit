package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.robot.FollowCall;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotFollowCall;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * `Follow` of the Tablet: the chosen Cubit takes the place, from switched off or from the Dock, and
 * the one that followed switches off where it is (answers 1073 to 1075).
 */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class FollowCallGameTests {
  private static final String ARENA = "arena";
  private static final BlockPos FIRST_FLOOR =
      new BlockPos(4, DockScene.FLOOR_Y, DockScene.MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR =
      new BlockPos(6, DockScene.FLOOR_Y, DockScene.MIDDLE_Z);
  private static final Energy CHARGE = new Energy(100_000);
  private static final Energy STOCK = new Energy(50_000);
  private static final String WRONG_FOLLOWER = "The wrong Cubit follows the player";
  private static final String WRONG_STATE = "The Cubit has the wrong state";
  private static final String WRONG_CALL = "Follow gave the wrong answer";

  private FollowCallGameTests() {}

  @GameTest(template = ARENA)
  public static void followSwapsTheCubitThatFollows(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity first = DockScene.placeRobot(helper, owner, FIRST_FLOOR, CHARGE);
    RobotEntity second = DockScene.placeRobot(helper, owner, SECOND_FLOOR, CHARGE);
    helper.assertValueEqual(second.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);

    helper.assertTrue(new RobotFollowCall(second).call(owner), WRONG_CALL);

    helper.assertValueEqual(follower(owner), Optional.of(second.getUUID()), WRONG_FOLLOWER);
    helper.assertValueEqual(second.powerState(), PowerState.ACTIVE, WRONG_STATE);
    helper.assertValueEqual(first.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void followCallsACubitOutOfTheDock(GameTestHelper helper) {
    ServerPlayer owner = DockScene.ownerBeside(helper);
    RobotEntity first = DockScene.placeRobot(helper, owner, FIRST_FLOOR, CHARGE);
    DockScene.placeDock(helper, owner, DockScene.FLOOR, STOCK);
    RobotEntity docked = DockScene.placeOnDock(helper, owner, CHARGE);
    helper.assertValueEqual(docked.powerState(), PowerState.HIBERNATING, WRONG_STATE);

    helper.assertTrue(new RobotFollowCall(docked).call(owner), WRONG_CALL);

    helper.assertValueEqual(follower(owner), Optional.of(docked.getUUID()), WRONG_FOLLOWER);
    helper.assertValueEqual(docked.powerState(), PowerState.ACTIVE, WRONG_STATE);
    helper.assertFalse(
        docked.docking().isDockedAt(helper.absolutePos(DockScene.DOCK)), WRONG_STATE);
    helper.assertValueEqual(first.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void theCubitThatFollowsAndOneWithoutChargeCannotBeCalled(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity following = DockScene.placeRobot(helper, owner, FIRST_FLOOR, CHARGE);
    RobotEntity empty = DockScene.placeRobot(helper, owner, SECOND_FLOOR, Energy.ZERO);

    helper.assertValueEqual(
        new RobotFollowCall(following).goal(), FollowCall.FOLLOWING, WRONG_CALL);
    helper.assertValueEqual(new RobotFollowCall(empty).goal(), FollowCall.NO_CHARGE, WRONG_CALL);
    helper.assertFalse(new RobotFollowCall(empty).call(owner), WRONG_CALL);
    helper.assertValueEqual(follower(owner), Optional.of(following.getUUID()), WRONG_FOLLOWER);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aStrangerCannotCallTheCubit(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    ServerPlayer stranger = GameTestRobots.onlinePlayer(helper);
    DockScene.placeRobot(helper, owner, FIRST_FLOOR, CHARGE);
    RobotEntity second = DockScene.placeRobot(helper, owner, SECOND_FLOOR, CHARGE);

    helper.assertFalse(new RobotFollowCall(second).call(stranger), WRONG_CALL);
    helper.assertValueEqual(second.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_STATE);
    helper.succeed();
  }

  private static Optional<UUID> follower(ServerPlayer owner) {
    return owner.getData(CubitAttachments.FOLLOWING_ROBOT).robot();
  }
}
