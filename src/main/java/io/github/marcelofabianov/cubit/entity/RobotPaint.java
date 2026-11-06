package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.robot.PaintRules;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;

/** What the owner does to a robot with a dye. The rules live in the domain. */
final class RobotPaint {
  private RobotPaint() {}

  /** The click is always used, so a dye of the same color does not open the inventory. */
  static InteractionResult apply(RobotEntity robot, Player player, ItemStack stack, DyeItem dye) {
    RobotColor.fromKey(dye.getDyeColor().getName())
        .filter(color -> PaintRules.paints(RobotLooks.color(robot), color))
        .ifPresent(color -> {
          RobotLooks.setColor(robot, color);
          stack.consume(1, player);
          react(robot);
        });
    return InteractionResult.CONSUME;
  }

  static void react(RobotEntity robot) {
    if (PaintRules.reacts(robot.powerState())) {
      robot.face().onInteraction();
    }
  }
}
