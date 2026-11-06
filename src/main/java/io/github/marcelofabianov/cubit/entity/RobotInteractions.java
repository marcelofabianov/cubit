package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingOutcome;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** What the owner does to a robot with a click. The rules live in the domain. */
final class RobotInteractions {
  private RobotInteractions() {}

  static InteractionResult openInventory(RobotEntity robot, Player player) {
    RobotMenu.open(
        player,
        robot.getDisplayName(),
        robot.inventory(),
        robot.energySlot(),
        robot::batteryPercent,
        robot::canUseInventory);
    return InteractionResult.CONSUME;
  }

  /**
   * A robot in an unloaded chunk misses a swap or a reset, so it finds out when it loads again.
   * Without this, it would stay active and still, and a click only turns on a switched-off robot.
   */
  static void switchOffWithoutThePlace(RobotEntity robot) {
    Player owner = robot.onlineOwner();
    if (robot.powerState() == PowerState.ACTIVE
        && owner != null
        && !RobotEntity.policy().holdsThePlace(robot.getUUID(), RobotEntity.follower(owner))) {
      robot.setPowerState(PowerState.INACTIVE_SWITCHED_OFF);
      robot.getNavigation().stop();
    }
  }

  /** Applied again on load, so a config change reaches the robots already in the world. */
  static void applyAttributes(RobotEntity robot) {
    TierSpec spec = robot.spec();
    robot.getAttribute(Attributes.MAX_HEALTH).setBaseValue(spec.maxHealth());
    robot
        .getAttribute(Attributes.MOVEMENT_SPEED)
        .setBaseValue(CubitMod.CONFIG.baseMovementSpeed() * spec.speedMultiplier());
    robot
        .getAttribute(Attributes.FOLLOW_RANGE)
        .setBaseValue(CubitMod.CONFIG.followDecision().teleportDistanceBlocks());
  }

  static InteractionResult use(RobotEntity robot, Player player, InteractionHand hand) {
    if (!robot.ownerId().filter(player.getUUID()::equals).isPresent()) {
      return InteractionResult.PASS;
    }
    if (player.isShiftKeyDown()) {
      return collect(robot, player);
    }
    if (robot.powerState().isInteractive()) {
      return openInventory(robot, player);
    }
    if (robot.robotInventory().energySlot().insert(player.getItemInHand(hand))) {
      return InteractionResult.CONSUME;
    }
    return turnOn(robot, player);
  }

  static InteractionResult collect(RobotEntity robot, Player player) {
    ItemStack stack = new ItemStack(robot.itemType());
    stack.set(CubitDataComponents.ROBOT_STATE.get(), robot.state());
    robot.robotInventory().writeTo(stack);
    robot.robotInventory().clear();
    player.setData(
        CubitAttachments.FOLLOWING_ROBOT,
        new FollowingRobotLink(
            RobotEntity.policy().release(robot.getUUID(), RobotEntity.follower(player))));
    robot.discard();
    // Drops the item at the player's feet when the inventory is full, so the robot is never lost.
    player.getInventory().placeItemBackInInventory(stack);
    return InteractionResult.CONSUME;
  }

  static InteractionResult turnOn(RobotEntity robot, Player player) {
    FollowingOutcome outcome =
        RobotEntity.policy()
            .onTurnOn(
                robot.getUUID(), RobotEntity.follower(player), robot.powerState(), robot.battery());
    if (outcome.state() == robot.powerState()) {
      return InteractionResult.PASS;
    }
    robot.setPowerState(outcome.state());
    player.setData(CubitAttachments.FOLLOWING_ROBOT, new FollowingRobotLink(outcome.follower()));
    outcome.robotToSwitchOff().ifPresent(other -> switchOff(robot, other));
    return InteractionResult.CONSUME;
  }

  private static void switchOff(RobotEntity robot, UUID other) {
    MinecraftServer server = robot.getServer();
    if (server != null) {
      server
          .getAllLevels()
          .forEach(loaded -> {
            if (loaded.getEntity(other) instanceof RobotEntity target) {
              target.setPowerState(PowerState.INACTIVE_SWITCHED_OFF);
            }
          });
    }
  }
}
