package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.ownership.FollowingOutcome;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.RobotClick;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.menu.RobotScreenOpening;
import io.github.marcelofabianov.cubit.player.FollowingRobotLink;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import java.util.UUID;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

/** What the owner does to a robot with a click. The rules live in the domain. */
final class RobotInteractions {
  private RobotInteractions() {}

  /** Only the numbers in the descriptions and the filter travel once, when it opens. */
  static InteractionResult openInventory(RobotEntity robot, Player player) {
    RobotScreenOpening opening = RobotScreenReadout.opening(robot);
    player.openMenu(
        new SimpleMenuProvider(
            (containerId, inventory, opener) ->
                RobotScreenReadout.serverMenu(containerId, inventory, robot),
            robot.getDisplayName()),
        opening::write);
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

  /** The player this robot follows right now, when that player is online. */
  static Player followedPlayer(RobotEntity robot) {
    Player owner = robot.onlineOwner();
    if (owner == null || !RobotEntity.follower(owner).filter(robot.getUUID()::equals).isPresent()) {
      return null;
    }
    return owner;
  }

  static boolean canUseInventory(RobotEntity robot, Player player) {
    return robot.isAlive()
        && robot.powerState().isInteractive()
        && Ownership.owns(robot.ownerId(), player.getUUID())
        && player.canInteractWithEntity(robot, Container.DEFAULT_DISTANCE_BUFFER);
  }

  static InteractionResult use(RobotEntity robot, Player player, InteractionHand hand) {
    ItemStack held = player.getItemInHand(hand);
    RobotClick click =
        RobotClick.of(
            Ownership.owns(robot.ownerId(), player.getUUID()),
            false,
            player.isShiftKeyDown(),
            held.getItem() instanceof DyeItem,
            held.is(Tags.Items.ORES),
            robot.powerState());
    return switch (click) {
      case IGNORE, UPGRADE -> InteractionResult.PASS;
      case COLLECT -> collect(robot, player);
      case PAINT -> RobotPaint.apply(robot, player, held, (DyeItem) held.getItem());
      case CHOOSE_ORE -> RobotOreChoice.apply(robot, player, held);
      case OPEN -> {
        robot.face().onInteraction();
        yield openInventory(robot, player);
      }
      case CHARGE_OR_TURN_ON ->
          robot.robotInventory().energySlot().insert(held)
              ? InteractionResult.CONSUME
              : turnOn(robot, player);
    };
  }

  static InteractionResult collect(RobotEntity robot, Player player) {
    ItemStack stack = new ItemStack(robot.itemType());
    stack.set(CubitDataComponents.ROBOT_STATE.get(), robot.state());
    stack.set(DataComponents.CUSTOM_NAME, robot.getCustomName());
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
