package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.DeathGuideCall;
import io.github.marcelofabianov.cubit.domain.behavior.DeathGuideProgress;
import io.github.marcelofabianov.cubit.domain.behavior.DeathGuideStep;
import io.github.marcelofabianov.cubit.domain.behavior.DeathRecord;
import io.github.marcelofabianov.cubit.domain.energy.PowerState;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.menu.DeathKeys;
import io.github.marcelofabianov.cubit.menu.DeathPointControl;
import io.github.marcelofabianov.cubit.menu.RobotSwitch;
import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalLong;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The death point of the Cubit: its switch, the text when the owner respawns and the guide to the
 * spot where the owner fell (answers 1111 to 1123). The order of the guide stays out of the saves,
 * so picking the Cubit up, leaving or unloading cancels it (answer 1123).
 */
public final class RobotDeathGuide implements RobotSwitch, DeathPointControl {
  private static final String TOLD_NAME = "death_point_%d";
  private static final String ARRIVED_NAME = "death_point_arrived_%d";
  private static final String STUCK_NAME = "death_point_stuck_%d";
  private static final double ROUTE_SPEED_MODIFIER = 1;

  private final RobotEntity robot;
  private boolean switchedOn = true;
  private DeathRecord target;
  private DeathGuideProgress progress;
  private int repathCountdown;

  RobotDeathGuide(RobotEntity robot) {
    this.robot = robot;
  }

  boolean switchedOn() {
    return switchedOn;
  }

  void setSwitchedOn(boolean switchedOn) {
    this.switchedOn = switchedOn;
  }

  @Override
  public boolean isOn() {
    return switchedOn;
  }

  /** Locked on a tier that does not mark the spot, as the torches are (answer 1118). */
  @Override
  public boolean isAvailable() {
    return robot.spec().deathPoint().marks();
  }

  @Override
  public void toggle() {
    switchedOn = !switchedOn;
    if (!switchedOn) {
      cancel();
    }
  }

  @Override
  public Optional<RobotTier> unlockTier() {
    return Arrays.stream(RobotTier.values())
        .filter(tier -> robot.specAt(tier).deathPoint().marks())
        .findFirst();
  }

  /** The first tier that guides, for the button of the Deaths tab (answer 1117). */
  public Optional<RobotTier> guideTier() {
    return Arrays.stream(RobotTier.values())
        .filter(tier -> robot.specAt(tier).deathPoint().guides())
        .findFirst();
  }

  /** Whether this Cubit can guide its owner to the death now, or why not. */
  public DeathGuideCall callFor(DeathRecord death) {
    boolean following =
        RobotInteractions.followedPlayer(robot) != null && robot.powerState() == PowerState.ACTIVE;
    return DeathGuideCall.of(
        robot.spec().deathPoint(),
        switchedOn,
        death.inOverworld(),
        robot.level().dimension() == Level.OVERWORLD,
        following);
  }

  /** Starts the guide when the call is ready, and says whether it did. */
  public boolean start(DeathRecord death) {
    if (!callFor(death).ready()) {
      return false;
    }
    target = death;
    progress = DeathGuideProgress.start(distanceTo(death));
    repathCountdown = 0;
    return true;
  }

  /** The time of the death it is guiding to, if any. */
  public OptionalLong guiding() {
    return target == null ? OptionalLong.empty() : OptionalLong.of(target.dayTime());
  }

  /** Stops the guide, without a text, and tells the screen of the owner. */
  public void cancel() {
    if (target != null) {
      end();
    }
  }

  /** From Advanced on, with the switch on, the Cubit that follows says where (answer 1111). */
  public void tellOnRespawn(DeathRecord death) {
    if (DeathGuideCall.tellsOnRespawn(robot.spec().deathPoint(), switchedOn, true)) {
      Component text =
          Component.translatable(
              DeathKeys.NOTICE_FELL,
              death.x(),
              death.y(),
              death.z(),
              DeathKeys.dimension(death.dimension()));
      robot.notices().showNamed(TOLD_NAME.formatted(death.dayTime()), text);
    }
  }

  boolean guidingNow() {
    return target != null;
  }

  /** Called on every tick of the guide goal, while the Cubit is active and on no other trip. */
  void walk() {
    Player owner = RobotInteractions.followedPlayer(robot);
    if (owner == null || owner.level() != robot.level() || !callFor(target).ready()) {
      cancel();
      return;
    }
    double toSpot = distanceTo(target);
    DeathGuideStep step =
        DeathGuideStep.of(
            toSpot, robot.distanceTo(owner), progress, CubitMod.CONFIG.deathGuideSpec());
    switch (step) {
      case ARRIVED -> finish(ARRIVED_NAME, DeathKeys.NOTICE_ARRIVED);
      case STUCK -> finish(STUCK_NAME, DeathKeys.NOTICE_STUCK);
      case WAIT -> waitFor(owner, toSpot);
      case MOVE -> moveOn(toSpot);
    }
  }

  private void waitFor(Player owner, double toSpot) {
    progress = progress.after(toSpot, true);
    robot.getNavigation().stop();
    robot.getLookControl().setLookAt(owner);
    repathCountdown = 0;
  }

  /**
   * The route goes to the spot itself: past the range of the route, the game walks the part of the
   * way it finds, without loading a chunk (rule 22).
   */
  private void moveOn(double toSpot) {
    progress = progress.after(toSpot, false);
    if (--repathCountdown > 0) {
      return;
    }
    repathCountdown = CubitMod.CONFIG.followRepathTicks();
    BlockPos spot = new BlockPos(target.x(), target.y(), target.z());
    robot.getNavigation().moveTo(spot.getX(), spot.getY(), spot.getZ(), ROUTE_SPEED_MODIFIER);
  }

  private void finish(String name, String key) {
    long dayTime = target.dayTime();
    end();
    robot.notices().showNamed(name.formatted(dayTime), Component.translatable(key));
  }

  private void end() {
    target = null;
    robot.getNavigation().stop();
    if (RobotInteractions.followedPlayer(robot) instanceof ServerPlayer owner) {
      DeathLogView.send(owner);
    }
  }

  private double distanceTo(DeathRecord death) {
    BlockPos spot = new BlockPos(death.x(), death.y(), death.z());
    return Math.sqrt(spot.distToCenterSqr(robot.position()));
  }
}
