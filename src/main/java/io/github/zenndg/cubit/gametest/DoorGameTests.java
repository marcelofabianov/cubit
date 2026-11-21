package io.github.zenndg.cubit.gametest;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.DoorMarks;
import io.github.zenndg.cubit.domain.robot.MarkedDoor;
import io.github.zenndg.cubit.domain.robot.TabletApp;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.item.TabletItem;
import io.github.zenndg.cubit.player.SavedDoorMarks;
import io.github.zenndg.cubit.player.TabletUse;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/** The doors the owner marks with the Tablet, which every Cubit of the owner opens (1359-1371). */
@GameTestHolder(CubitMod.MOD_ID)
@PrefixGameTestTemplate(false)
public final class DoorGameTests {
  private static final String ARENA = "arena";
  private static final int TIMEOUT = 400;
  private static final Direction ACROSS_THE_WALL = Direction.EAST;
  private static final int SHUT_WAIT = 160;
  private static final int WALL_X = 8;
  private static final int WALL_TOP = 4;
  private static final int LANE_Z = 2;
  private static final int LAST_Z = 4;
  private static final int FLOOR_Y = 1;
  private static final int STAND_Y = 2;
  private static final BlockPos ROBOT_FLOOR = new BlockPos(3, FLOOR_Y, LANE_Z);
  private static final BlockPos DOOR = new BlockPos(WALL_X, STAND_Y, LANE_Z);
  private static final BlockPos STONE = new BlockPos(2, STAND_Y, 0);
  private static final double OWNER_X = 14.5;
  private static final double OWNER_Z = 2.5;
  private static final double HALF = 0.5;
  private static final int START_X = 3;
  private static final double NEAR_X = 10.5;
  private static final int OWNER_WAIT = 100;
  private static final int THIRD_STEP = 3;
  private static final int ONE_MARK = 1;
  private static final String NOT_PASSED = "The robot should have passed the marked door";
  private static final String AT = " · robot at ";
  private static final String OPEN = " · door open: ";
  private static final String NOT_SHUT = "The door should be shut again";
  private static final String PASSED = "The robot should not pass an unmarked door";
  private static final String WRONG_MARKS = "Wrong marked doors";

  private DoorGameTests() {}

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotOpensAMarkedIronDoorAndShutsItBehind(GameTestHelper helper) {
    walledLane(helper, Blocks.IRON_DOOR);
    ServerPlayer owner = ownerBehindTheWall(helper);
    mark(helper, owner, DOOR);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, ROBOT_FLOOR);

    helper.succeedWhen(() -> {
      helper.assertTrue(passed(helper, robot), where(helper, robot));
      helper.assertFalse(isOpen(helper, DOOR), NOT_SHUT);
    });
  }

  /** The leaf of an open door takes 3/16 of the block on the side of its hinge. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void itPassesADoorHingedOnTheOtherSide(GameTestHelper helper) {
    crossing(helper, Direction.EAST, DoorHingeSide.RIGHT, LANE_Z, OWNER_Z);
  }

  /** Coming on the diagonal, from one side of the lane to the other. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void itPassesADoorReachedOnTheDiagonal(GameTestHelper helper) {
    crossing(helper, Direction.EAST, DoorHingeSide.LEFT, 0, LAST_Z + HALF);
  }

  /** A door that opens toward the Cubit, reached on the other diagonal. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void itPassesADoorThatOpensTowardIt(GameTestHelper helper) {
    crossing(helper, Direction.WEST, DoorHingeSide.RIGHT, LAST_Z, HALF);
  }

  /** The owner stops just behind the door, and then walks away to a corner of the arena. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void itPassesWhenTheOwnerStopsNearAndThenWalksOn(GameTestHelper helper) {
    walledLane(helper, Blocks.IRON_DOOR);
    ServerPlayer owner = ownerAt(helper, NEAR_X, OWNER_Z);
    mark(helper, owner, DOOR);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, ROBOT_FLOOR);
    helper.runAfterDelay(OWNER_WAIT, () -> moveOwner(helper, owner, OWNER_X, 0));
    helper.runAfterDelay(OWNER_WAIT * 2, () -> moveOwner(helper, owner, OWNER_X, LAST_Z + HALF));

    helper.runAfterDelay(
        OWNER_WAIT * THIRD_STEP,
        () -> {
          helper.assertTrue(passed(helper, robot), where(helper, robot));
          helper.assertFalse(isOpen(helper, DOOR), NOT_SHUT);
          helper.succeed();
        });
  }

  private static void crossing(
      GameTestHelper helper, Direction facing, DoorHingeSide hinge, int robotZ, double ownerZ) {
    walledLane(helper, Blocks.IRON_DOOR);
    placeDoor(helper, Blocks.IRON_DOOR, DOOR, facing, hinge);
    ServerPlayer owner = ownerAt(helper, ownerZ);
    mark(helper, owner, DOOR);
    RobotEntity robot =
        GameTestRobots.placeNewRobot(helper, owner, new BlockPos(START_X, FLOOR_Y, robotZ));

    helper.succeedWhen(() -> {
      helper.assertTrue(passed(helper, robot), where(helper, robot));
      helper.assertFalse(isOpen(helper, DOOR), NOT_SHUT);
    });
  }

  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void aRobotOpensAMarkedFenceGate(GameTestHelper helper) {
    walledLane(helper, Blocks.OAK_FENCE_GATE);
    ServerPlayer owner = ownerBehindTheWall(helper);
    mark(helper, owner, DOOR);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, ROBOT_FLOOR);

    helper.succeedWhen(() -> {
      helper.assertTrue(passed(helper, robot), where(helper, robot));
      helper.assertFalse(isOpen(helper, DOOR), NOT_SHUT);
    });
  }

  /** Even a wooden door, which the mobs of the game open, stays shut without a mark. */
  @GameTest(template = ARENA, timeoutTicks = TIMEOUT)
  public static void anUnmarkedDoorStaysShut(GameTestHelper helper) {
    walledLane(helper, Blocks.OAK_DOOR);
    ServerPlayer owner = ownerBehindTheWall(helper);
    RobotEntity robot = GameTestRobots.placeNewRobot(helper, owner, ROBOT_FLOOR);

    helper.runAfterDelay(
        SHUT_WAIT,
        () -> {
          helper.assertFalse(passed(helper, robot), PASSED);
          helper.assertFalse(isOpen(helper, DOOR), NOT_SHUT);
          helper.succeed();
        });
  }

  /** The upper half marks the door by its lower half, and a second click unmarks it. */
  @GameTest(template = ARENA)
  public static void aClickMarksTheWholeDoorAndAnotherUnmarksIt(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    placeDoor(helper, Blocks.IRON_DOOR, DOOR);

    mark(helper, owner, DOOR.above());

    helper.assertValueEqual(marksOf(helper, owner).doors(), List.of(door(helper)), WRONG_MARKS);
    mark(helper, owner, DOOR);
    helper.assertTrue(marksOf(helper, owner).isEmpty(), WRONG_MARKS);
    helper.succeed();
  }

  @GameTest(template = ARENA)
  public static void aBlockThatIsNotADoorIsNotMarked(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    helper.setBlock(STONE, Blocks.STONE);
    placeDoor(helper, Blocks.IRON_DOOR, DOOR);

    mark(helper, owner, STONE);
    mark(helper, owner, DOOR);

    helper.assertValueEqual(marksOf(helper, owner).doors().size(), ONE_MARK, WRONG_MARKS);
    helper.succeed();
  }

  /** A wall across the arena, with the door in the lane. */
  private static void walledLane(GameTestHelper helper, Block door) {
    for (int z = 0; z <= LAST_Z; z++) {
      for (int y = STAND_Y; y <= WALL_TOP; y++) {
        helper.setBlock(new BlockPos(WALL_X, y, z), Blocks.STONE);
      }
    }
    helper.setBlock(DOOR.above(), Blocks.AIR);
    placeDoor(helper, door, DOOR);
  }

  private static void placeDoor(GameTestHelper helper, Block block, BlockPos pos) {
    placeDoor(helper, block, pos, ACROSS_THE_WALL, DoorHingeSide.LEFT);
  }

  private static void placeDoor(
      GameTestHelper helper, Block block, BlockPos pos, Direction facing, DoorHingeSide hinge) {
    BlockState state =
        block.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, facing);
    if (block instanceof DoorBlock) {
      BlockState door = state.setValue(DoorBlock.HINGE, hinge);
      helper.setBlock(pos, door.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER));
      helper.setBlock(pos.above(), door.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER));
    } else {
      helper.setBlock(pos, state);
    }
  }

  private static ServerPlayer ownerBehindTheWall(GameTestHelper helper) {
    return ownerAt(helper, OWNER_Z);
  }

  private static ServerPlayer ownerAt(GameTestHelper helper, double z) {
    return ownerAt(helper, OWNER_X, z);
  }

  private static ServerPlayer ownerAt(GameTestHelper helper, double x, double z) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    moveOwner(helper, owner, x, z);
    return owner;
  }

  private static void moveOwner(GameTestHelper helper, ServerPlayer owner, double x, double z) {
    Vec3 position = helper.absoluteVec(new Vec3(x, STAND_Y, z));
    owner.moveTo(position.x, position.y, position.z);
  }

  private static void mark(GameTestHelper helper, ServerPlayer owner, BlockPos pos) {
    ItemStack tablet = new ItemStack(CubitItems.TABLET.get());
    TabletItem.setApp(tablet, TabletApp.DOORS);
    TabletUse.click(owner, tablet, new TabletUse.Block(helper.absolutePos(pos), Optional.empty()));
  }

  private static DoorMarks marksOf(GameTestHelper helper, ServerPlayer owner) {
    return SavedDoorMarks.of(helper.getLevel().getServer()).marksOf(owner.getUUID());
  }

  private static MarkedDoor door(GameTestHelper helper) {
    return new MarkedDoor(
        helper.absolutePos(DOOR).asLong(), helper.getLevel().dimension().location().toString());
  }

  private static String where(GameTestHelper helper, RobotEntity robot) {
    return NOT_PASSED + AT + helper.relativeVec(robot.position()) + OPEN + isOpen(helper, DOOR);
  }

  private static boolean passed(GameTestHelper helper, RobotEntity robot) {
    return robot.getX() > helper.absoluteVec(new Vec3(WALL_X + 1, 0, 0)).x;
  }

  private static boolean isOpen(GameTestHelper helper, BlockPos pos) {
    return helper.getBlockState(pos).getValue(BlockStateProperties.OPEN);
  }
}
