package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.ExpressionRules;
import io.github.marcelofabianov.cubit.domain.behavior.ExpressionTimer;
import io.github.marcelofabianov.cubit.domain.behavior.GazeDirection;
import io.github.marcelofabianov.cubit.domain.behavior.OwnerGaze;
import io.github.marcelofabianov.cubit.domain.behavior.OwnerLook;
import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotNotice;
import io.github.marcelofabianov.cubit.domain.behavior.RobotPose;
import io.github.marcelofabianov.cubit.domain.behavior.RobotSituation;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import net.minecraft.world.entity.player.Player;

/**
 * Decides the expression of a robot on the server and sends it to the client, which draws it. It
 * stays out of the saves because it is recalculated, starting dark so that loading does not beep.
 */
public final class RobotFace {
  private final RobotEntity robot;
  private ExpressionTimer happy = ExpressionTimer.STOPPED;
  private ExpressionTimer hurt = ExpressionTimer.STOPPED;
  private ExpressionTimer worried = ExpressionTimer.STOPPED;
  private ExpressionTimer dying = ExpressionTimer.STOPPED;
  private ExpressionTimer armsRaised = ExpressionTimer.STOPPED;
  private int lastHurtTime;
  private int lastOwnerHurtTime;
  private OwnerGaze ownerGaze = OwnerGaze.LEVEL;
  private double lastOwnerY = Double.NaN;

  RobotFace(RobotEntity robot) {
    this.robot = robot;
  }

  public static RobotExpression expression(RobotEntity robot) {
    return RobotExpression.values()[robot.getEntityData().get(RobotSyncedData.EXPRESSION)];
  }

  public static boolean armsRaised(RobotEntity robot) {
    return robot.getEntityData().get(RobotSyncedData.ARMS_RAISED);
  }

  public static GazeDirection ownerGaze(RobotEntity robot) {
    return GazeDirection.values()[robot.getEntityData().get(RobotSyncedData.OWNER_GAZE)];
  }

  public static GazeDirection ownerLevel(RobotEntity robot) {
    return GazeDirection.values()[robot.getEntityData().get(RobotSyncedData.OWNER_LEVEL)];
  }

  void onInteraction() {
    happy = ExpressionTimer.started(CubitMod.CONFIG.happyExpressionTicks());
  }

  void tick() {
    updateHurtTimer();
    updateWorriedTimer();
    RobotExpression next = ExpressionRules.decide(robot.powerState(), situation());
    happy = happy.tick();
    hurt = hurt.tick();
    worried = worried.tick();
    dying = dying.tick();
    robot
        .getEntityData()
        .set(RobotSyncedData.ARMS_RAISED, RobotPose.raisesArms(next, armsRaised.isRunning()));
    armsRaised = armsRaised.tick();
    if (ExpressionRules.beeps(expression(robot), next)) {
      robot.playSound(CubitSounds.EXPRESSION_BEEP.get());
    }
    robot.getEntityData().set(RobotSyncedData.EXPRESSION, (byte) next.ordinal());
    updateOwnerGaze();
  }

  /** Only the server knows where the owner is for every player who sees this robot. */
  private void updateOwnerGaze() {
    Player owner = robot.onlineOwner();
    boolean near =
        owner != null
            && OwnerLook.applies(
                robot.powerState(), owner.level() == robot.level(), robot.distanceTo(owner));
    GazeDirection level = GazeDirection.LEVEL;
    if (near) {
      double heightAbove = owner.getY() - robot.getY();
      ownerGaze = ownerGaze.next(owner.getY() - lastOwnerY, heightAbove);
      level = OwnerGaze.levelOf(heightAbove);
      lastOwnerY = owner.getY();
    } else {
      ownerGaze = OwnerGaze.LEVEL;
      lastOwnerY = Double.NaN;
    }
    robot.getEntityData().set(RobotSyncedData.OWNER_LEVEL, (byte) level.ordinal());
    robot.getEntityData().set(RobotSyncedData.OWNER_GAZE, (byte) ownerGaze.direction().ordinal());
  }

  /** The game restarts this counter on every hit, so a rise means the robot was just hurt. */
  private void updateHurtTimer() {
    if (robot.hurtTime > lastHurtTime) {
      hurt = ExpressionTimer.started(CubitMod.CONFIG.hurtExpressionTicks());
      dying = ExpressionTimer.started(CubitMod.CONFIG.dyingExpressionTicks());
      armsRaised = ExpressionTimer.started(RobotPose.ARMS_RAISED_TICKS);
      robot.notices().show(RobotNotice.HURT);
    }
    lastHurtTime = robot.hurtTime;
  }

  /** Only the robot that follows reacts, and it reads the owner without searching the world. */
  private void updateWorriedTimer() {
    Player owner = RobotInteractions.followedPlayer(robot);
    int ownerHurtTime = owner == null ? 0 : owner.hurtTime;
    if (ownerHurtTime > lastOwnerHurtTime && isNear(owner)) {
      worried = ExpressionTimer.started(CubitMod.CONFIG.worriedExpressionTicks());
      robot.notices().show(RobotNotice.OWNER_HURT);
    }
    lastOwnerHurtTime = ownerHurtTime;
  }

  private boolean isNear(Player owner) {
    int radius = CubitMod.CONFIG.worriedRadiusBlocks();
    return owner.level() == robot.level() && owner.distanceToSqr(robot) <= (double) radius * radius;
  }

  private RobotSituation situation() {
    return new RobotSituation(
        dying.isRunning()
            && ExpressionRules.isDying(
                robot.getHealth(), robot.getMaxHealth(), CubitMod.CONFIG.dyingHealth()),
        hurt.isRunning(),
        worried.isRunning(),
        false,
        false,
        robot.battery().hasAtMost(CubitMod.CONFIG.criticalCharge()),
        happy.isRunning());
  }
}
