package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.BurningAction;
import io.github.marcelofabianov.cubit.domain.behavior.DamagingBlockReaction;
import io.github.marcelofabianov.cubit.domain.behavior.FetchRules;
import io.github.marcelofabianov.cubit.domain.behavior.FollowAction;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

/**
 * Walks to the item on the ground nearest to the owner, so the touch pickup takes it, and then lets
 * the robot follow again (answers 927 and 936 to 942). The rules live in {@link FetchRules}.
 */
final class FetchItemGoal extends Goal {
  /** The robot speed is already in its movement attribute, so the route does not change it. */
  private static final double ROUTE_SPEED_MODIFIER = 1;

  private static final float LOOK_YAW_SPEED = 10f;

  private final RobotEntity robot;
  private ItemEntity target;
  private long startTick;
  private long nextCheckTick;
  private int repathCountdown;
  private boolean noRoute;

  FetchItemGoal(RobotEntity robot) {
    this.robot = robot;
    setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
  }

  /**
   * Looks for an item only every few ticks, by the game time, because the game asks this on every
   * other tick only (rule 21).
   */
  @Override
  public boolean canUse() {
    long now = robot.level().getGameTime();
    if (now < nextCheckTick) {
      return false;
    }
    nextCheckTick = now + CubitMod.CONFIG.fetchCheckTicks();
    Player owner = RobotInteractions.followedPlayer(robot);
    if (!fetches(owner)) {
      return false;
    }
    target = robot.pickup().fetchTarget(owner).orElse(null);
    return target != null;
  }

  @Override
  public boolean canContinueToUse() {
    Player owner = RobotInteractions.followedPlayer(robot);
    return target != null
        && !noRoute
        && !gaveUp()
        && fetches(owner)
        && robot.pickup().stillFetching(target, owner);
  }

  @Override
  public void start() {
    startTick = robot.level().getGameTime();
    repathCountdown = 0;
    noRoute = false;
  }

  @Override
  public void tick() {
    robot.getLookControl().setLookAt(target, LOOK_YAW_SPEED, robot.getMaxHeadXRot());
    repathCountdown--;
    if (repathCountdown > 0) {
      return;
    }
    repathCountdown = adjustedTickDelay(CubitMod.CONFIG.followRepathTicks());
    noRoute = !robot.getNavigation().moveTo(target, ROUTE_SPEED_MODIFIER);
  }

  /** Only a robot that gave up or found no route ignores the item. One that lost it does not. */
  @Override
  public void stop() {
    if (target != null && target.isAlive() && (noRoute || gaveUp())) {
      robot.pickup().giveUp(target);
    }
    target = null;
    robot.getNavigation().stop();
  }

  private boolean gaveUp() {
    return FetchRules.gaveUp(
        startTick, robot.level().getGameTime(), CubitMod.CONFIG.fetchGiveUpTicks());
  }

  private boolean fetches(Player owner) {
    boolean following =
        owner != null
            && owner.level() == robot.level()
            && CubitMod.CONFIG
                    .followDecision()
                    .decide(robot.powerState(), robot.distanceTo(owner), true)
                != FollowAction.TELEPORT;
    return FetchRules.fetches(
        robot.powerState(), robot.pickup().settings(), robot.pickup().spec(), following, busy());
  }

  /** The dock trip, fire, blocks that hurt and the shock all come before fetching (answer 939). */
  private boolean busy() {
    boolean burning =
        BurningAction.decide(robot.powerState(), robot.isOnFire(), robot.isInLava(), false)
            != BurningAction.NONE;
    boolean hurting =
        DamagingBlockReaction.shouldLeave(
            robot.powerState(), DamagingBlocks.hurt(robot), robot.isOnFire(), robot.isInLava());
    return robot.docking().hasTrip() || burning || hurting || robot.defense().engaged();
  }
}
