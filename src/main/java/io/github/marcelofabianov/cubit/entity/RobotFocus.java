package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.FocusTarget;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.network.FocusMarkPayload;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * The target of Focus in the world: the mob the owner hurt, followed until it dies, leaves the
 * dimension or gets far from the owner. The rules live in {@link FocusTarget}.
 */
public final class RobotFocus {
  public static final String TARGET_KEY = "notice.cubit.focus_target";

  private final RobotEntity robot;
  private FocusTarget target = FocusTarget.NONE;

  RobotFocus(RobotEntity robot) {
    this.robot = robot;
  }

  FocusTarget target() {
    return target;
  }

  void clear() {
    change(FocusTarget.NONE, null);
  }

  void ownerHurt(LivingEntity mob) {
    change(target.hurtByOwner(mob.getUUID(), ShockCandidates.of(robot, mob)), mob);
  }

  /** A lookup by id, only while there is a target, so it costs nothing most of the time. */
  void tick() {
    if (!target.hasTarget() || !(robot.level() instanceof ServerLevel level)) {
      return;
    }
    Entity mob = target.target().map(level::getEntity).orElse(null);
    Player owner = robot.onlineOwner();
    Optional<Double> distance =
        mob != null && mob.isAlive() && owner != null && mob.level() == owner.level()
            ? Optional.of((double) mob.distanceTo(owner))
            : Optional.empty();
    change(target.followed(distance, CubitMod.CONFIG.focusForgetDistanceBlocks()), null);
  }

  /** Tells the owner, with a text when a new target comes and the arrow on any change. */
  private void change(FocusTarget next, LivingEntity mob) {
    if (next.equals(target)) {
      return;
    }
    boolean announce = next.changedFrom(target) && mob != null;
    target = next;
    if (!(robot.onlineOwner() instanceof ServerPlayer owner)) {
      return;
    }
    if (announce) {
      Component text = Component.translatable(TARGET_KEY, mob.getDisplayName());
      CubitNetwork.sendNotice(owner, Component.translatable(RobotNotices.FORMAT_KEY, text));
    }
    int mobId = target.hasTarget() && mob != null ? mob.getId() : FocusMarkPayload.NO_MOB;
    CubitNetwork.sendFocusMark(owner, new FocusMarkPayload(robot.getId(), mobId));
  }
}
