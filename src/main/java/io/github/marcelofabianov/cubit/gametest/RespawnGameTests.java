package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotRespawn;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The robot comes back with an owner who respawned. The real death is in the manual script. */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class RespawnGameTests {
  private static final String ARENA = "arena";
  private static final int FLOOR_Y = 1;
  private static final int MIDDLE_Z = 2;
  private static final int FIRST_X = 3;
  private static final int SECOND_X = 6;
  private static final BlockPos FLOOR = new BlockPos(FIRST_X, FLOOR_Y, MIDDLE_Z);
  private static final BlockPos SECOND_FLOOR = new BlockPos(SECOND_X, FLOOR_Y, MIDDLE_Z);
  private static final double FAR_AWAY = 100;
  private static final double CLOSE_BY = 8;
  private static final double AT_THE_OWNER = 1;
  private static final double MOVED = 0.5;
  private static final int NETHER_CHUNK = 0;
  private static final int CHUNK_LOAD_TIMEOUT = 600;
  private static final Vec3 NETHER_SPOT = new Vec3(8.5, 64, 8.5);
  private static final String NOT_THERE = "The robot should be loaded in the Nether";
  private static final String STAYED = "The robot should have come back";
  private static final String CAME_BACK = "The robot should have stayed";
  private static final String LOST = "The robot should be in the dimension of the owner";
  private static final String LOST_ITEM = "The robot lost its inventory on the way";
  private static final String LOST_LINK = "The robot should still be active";

  private RespawnGameTests() {}

  @GameTest(template = ARENA)
  public static void anActiveRobotFarAwayComesBack(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    owner.moveTo(robot.position().add(FAR_AWAY, 0, 0));

    RobotRespawn.bringTo(owner);

    helper.assertTrue(robot.distanceTo(owner) < AT_THE_OWNER, STAYED);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aRobotCloseByStaysAndWalks(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    Vec3 before = robot.position();
    owner.moveTo(before.add(CLOSE_BY, 0, 0));

    RobotRespawn.bringTo(owner);

    helper.assertTrue(robot.position().distanceTo(before) < MOVED, CAME_BACK);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aRobotSwitchedOffStaysWhereItIs(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    RobotEntity second = GameTestRobots.placeNewRobot(helper, owner, SECOND_FLOOR);
    Vec3 before = second.position();
    owner.moveTo(before.add(FAR_AWAY, 0, 0));

    RobotRespawn.bringTo(owner);

    helper.assertTrue(second.position().distanceTo(before) < MOVED, CAME_BACK);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aPlayerWithoutARobotRespawnsNormally(GameTestHelper helper) {
    RobotRespawn.bringTo(GameTestRobots.onlinePlayer(helper));
    helper.succeed();
  }

  /** The Nether chunk is forced only by this test, because a robot only comes back if loaded. */
  @GameTest(template = ARENA, timeoutTicks = CHUNK_LOAD_TIMEOUT)
  public static void aRobotInAnotherDimensionComesBackWithItsInventory(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, FLOOR);
    UUID id = robot.getUUID();
    robot.inventory().setItem(0, new ItemStack(Items.DIAMOND));
    ServerLevel nether = helper.getLevel().getServer().getLevel(Level.NETHER);
    nether.setChunkForced(NETHER_CHUNK, NETHER_CHUNK, true);
    robot.changeDimension(
        new DimensionTransition(
            nether, NETHER_SPOT, Vec3.ZERO, 0, 0, DimensionTransition.DO_NOTHING));

    helper.succeedWhen(() -> {
      helper.assertTrue(nether.getEntity(id) instanceof RobotEntity, NOT_THERE);
      RobotRespawn.bringTo(owner);
      nether.setChunkForced(NETHER_CHUNK, NETHER_CHUNK, false);

      helper.assertTrue(helper.getLevel().getEntity(id) instanceof RobotEntity, LOST);
      RobotEntity back = (RobotEntity) helper.getLevel().getEntity(id);
      helper.assertTrue(back.inventory().getItem(0).is(Items.DIAMOND), LOST_ITEM);
      helper.assertTrue(back.powerState() == PowerState.ACTIVE, LOST_LINK);
      back.discard();
    });
  }
}
