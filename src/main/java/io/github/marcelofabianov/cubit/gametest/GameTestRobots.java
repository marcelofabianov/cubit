package io.github.marcelofabianov.cubit.gametest;

import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitEntities;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/** Places robots the way a player does: by using the item on a block. */
final class GameTestRobots {
  private GameTestRobots() {}

  static RobotEntity placeNewRobot(GameTestHelper helper, Player player, BlockPos floor) {
    return placeRobot(helper, player, new ItemStack(CubitItems.GENERAL_ROBOT.get()), floor);
  }

  static RobotEntity placeRobot(
      GameTestHelper helper, Player player, ItemStack stack, BlockPos floor) {
    List<RobotEntity> before = robots(helper);
    use(helper, player, stack, floor);
    return robots(helper).stream()
        .filter(robot -> !before.contains(robot))
        .findFirst()
        .orElseThrow();
  }

  static void use(GameTestHelper helper, Player player, ItemStack stack, BlockPos floor) {
    helper.setBlock(floor, Blocks.STONE);
    BlockPos absolute = helper.absolutePos(floor);
    BlockHitResult hit =
        new BlockHitResult(Vec3.atCenterOf(absolute), Direction.UP, absolute, false);
    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
    stack.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
  }

  static List<RobotEntity> robots(GameTestHelper helper) {
    return helper.getEntities(CubitEntities.GENERAL_ROBOT.get());
  }
}
