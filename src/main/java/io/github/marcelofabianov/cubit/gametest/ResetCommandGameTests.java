package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The command goes through the real dispatcher, as if the player had typed it. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ResetCommandGameTests {
  private static final String EMPTY_TEMPLATE = "empty";
  private static final String COMMAND = "cubit reset";
  private static final int OPERATOR_LEVEL = 2;
  private static final BlockPos FLOOR = new BlockPos(0, 0, 0);
  private static final BlockPos BESIDE_FLOOR = new BlockPos(2, 1, 2);
  private static final UUID LOST_ROBOT = new UUID(0, 1);
  private static final String WRONG_LINK = "Wrong following robot";
  private static final String WRONG_POWER = "Wrong power state";
  private static final String WRONG_CHARGE = "The command changed the charge";
  private static final String HAS_PERMISSION = "The test player should not be an operator";

  private ResetCommandGameTests() {}

  @GameTest(template = EMPTY_TEMPLATE)
  public static void aPlayerWithoutPermissionClearsALostLink(GameTestHelper helper) {
    ServerPlayer player = ownerBeside(helper);
    linkTo(player, LOST_ROBOT);
    helper.assertFalse(player.hasPermissions(OPERATOR_LEVEL), HAS_PERMISSION);

    runCommand(player);

    helper.assertValueEqual(follower(player), Optional.empty(), WRONG_LINK);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theNextRobotFollowsAfterALostLinkIsCleared(GameTestHelper helper) {
    ServerPlayer player = ownerBeside(helper);
    linkTo(player, LOST_ROBOT);

    runCommand(player);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, player, FLOOR);

    helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
    helper.assertValueEqual(follower(player), Optional.of(robot.getUUID()), WRONG_LINK);
    helper.succeed();
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theRobotThatFollowedSwitchesOffAndAClickBringsItBack(GameTestHelper helper) {
    ServerPlayer player = ownerBeside(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, player, FLOOR);
    Energy charge = robot.state().energy();

    runCommand(player);

    helper.succeedWhen(() -> {
      helper.assertValueEqual(robot.powerState(), PowerState.INACTIVE_SWITCHED_OFF, WRONG_POWER);
      helper.assertValueEqual(robot.state().energy(), charge, WRONG_CHARGE);
      robot.interact(player, InteractionHand.MAIN_HAND);
      helper.assertValueEqual(robot.powerState(), PowerState.ACTIVE, WRONG_POWER);
      helper.assertValueEqual(follower(player), Optional.of(robot.getUUID()), WRONG_LINK);
    });
  }

  @GameTest(template = EMPTY_TEMPLATE)
  public static void theLinkOfAnotherPlayerIsNotTouched(GameTestHelper helper) {
    ServerPlayer player = ownerBeside(helper);
    ServerPlayer other = ownerBeside(helper);
    linkTo(player, LOST_ROBOT);
    linkTo(other, LOST_ROBOT);

    runCommand(player);

    helper.assertValueEqual(follower(other), Optional.of(LOST_ROBOT), WRONG_LINK);
    helper.succeed();
  }

  private static ServerPlayer ownerBeside(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 beside = helper.absoluteVec(Vec3.atBottomCenterOf(BESIDE_FLOOR));
    owner.moveTo(beside.x, beside.y, beside.z);
    return owner;
  }

  private static void runCommand(ServerPlayer player) {
    player
        .getServer()
        .getCommands()
        .performPrefixedCommand(player.createCommandSourceStack(), COMMAND);
  }

  private static void linkTo(Player player, UUID robot) {
    player.setData(CubitAttachments.FOLLOWING_ROBOT, new FollowingRobotLink(Optional.of(robot)));
  }

  private static Optional<UUID> follower(Player player) {
    return player.getData(CubitAttachments.FOLLOWING_ROBOT).robot();
  }
}
