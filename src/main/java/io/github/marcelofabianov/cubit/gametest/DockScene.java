package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/** Builds the scenes of the tests of a robot using the base. */
final class DockScene {
  static final int FLOOR_Y = 1;
  static final int MIDDLE_Z = 2;
  static final int DOCK_X = 12;
  static final BlockPos FLOOR = new BlockPos(DOCK_X, FLOOR_Y, MIDDLE_Z);
  static final BlockPos DOCK = FLOOR.above();
  private static final int WALL_HEIGHT = 7;
  private static final float HEALTH = 25f;

  private DockScene() {}

  /** The owner stays by the base, so the robot has no reason to teleport after the trip. */
  static ServerPlayer ownerBeside(GameTestHelper helper) {
    ServerPlayer owner = GameTestRobots.onlinePlayer(helper);
    Vec3 beside = helper.absoluteVec(Vec3.atBottomCenterOf(DOCK.south(2)));
    owner.moveTo(beside.x, beside.y, beside.z);
    return owner;
  }

  static void buildWallAt(GameTestHelper helper, int x) {
    for (int z = 0; z <= WALL_HEIGHT; z++) {
      for (int y = FLOOR_Y + 1; y <= WALL_HEIGHT; y++) {
        helper.setBlock(new BlockPos(x, y, z), Blocks.BEDROCK);
      }
    }
  }

  static InductionDockBlockEntity placeDock(
      GameTestHelper helper, Player owner, BlockPos floor, Energy energy) {
    ItemStack item = new ItemStack(CubitItems.INDUCTION_DOCK.get());
    item.set(CubitDataComponents.DOCK_ENERGY.get(), energy.amount());
    GameTestRobots.use(helper, owner, item, floor);
    return helper.getBlockEntity(floor.above());
  }

  static ItemStack robotItem(Energy energy) {
    ItemStack item = new ItemStack(CubitItems.GENERAL_ROBOT.get());
    item.set(CubitDataComponents.ROBOT_STATE.get(), new RobotState(RobotTier.BASE, energy, HEALTH));
    return item;
  }

  static RobotEntity placeRobot(
      GameTestHelper helper, Player owner, BlockPos floor, Energy energy) {
    return GameTestRobots.placeRobot(helper, owner, robotItem(energy), floor);
  }

  static RobotEntity placeOnDock(GameTestHelper helper, Player owner, Energy energy) {
    List<RobotEntity> before = GameTestRobots.robots(helper);
    useOnDock(helper, owner, robotItem(energy));
    return GameTestRobots.robots(helper).stream()
        .filter(robot -> !before.contains(robot))
        .findFirst()
        .orElseThrow();
  }

  /** Goes the way of the game: the item acts only after the block lets it through. */
  static void useOnDock(GameTestHelper helper, Player player, ItemStack stack) {
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    BlockPos absolute = helper.absolutePos(DOCK);
    BlockHitResult hit =
        new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false);
    stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
  }

  /** The click of the game with the item in the main hand, which the block handles first. */
  static void clickWithHeldItem(GameTestHelper helper, Player player, BlockPos dock) {
    BlockPos absolute = helper.absolutePos(dock);
    BlockHitResult hit =
        new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false);
    helper
        .getBlockState(dock)
        .useItemOn(
            player.getItemInHand(InteractionHand.MAIN_HAND),
            helper.getLevel(),
            player,
            InteractionHand.MAIN_HAND,
            hit);
  }

  static void click(GameTestHelper helper, Player player, BlockPos position) {
    BlockPos absolute = helper.absolutePos(position);
    BlockHitResult hit =
        new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false);
    helper.getBlockState(position).useWithoutItem(helper.getLevel(), player, hit);
  }
}
