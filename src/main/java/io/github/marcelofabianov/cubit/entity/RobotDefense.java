package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.DefenseRules;
import io.github.marcelofabianov.cubit.domain.behavior.FocusTarget;
import io.github.marcelofabianov.cubit.domain.behavior.RetaliationMemory;
import io.github.marcelofabianov.cubit.domain.behavior.ShockRules;
import io.github.marcelofabianov.cubit.domain.behavior.ShockSignals;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.tier.ShockSpec;
import io.github.marcelofabianov.cubit.menu.DefenseControl;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.Comparator;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

/**
 * The defense mode of a robot and its shock. The search only runs when a shock is ready, once per
 * interval of the tier, so it never runs every tick (rule 21).
 */
public final class RobotDefense implements DefenseControl {
  private final RobotEntity robot;
  private DefenseMode mode = DefenseMode.INITIAL;
  private RetaliationMemory memory = RetaliationMemory.EMPTY;
  private long lastAttemptTick;
  private long engagedUntilTick;
  private final RobotFocus focus;

  RobotDefense(RobotEntity robot) {
    this.robot = robot;
    this.focus = new RobotFocus(robot);
  }

  public FocusTarget focusTarget() {
    return focus.target();
  }

  @Override
  public DefenseMode mode() {
    return mode;
  }

  /** A mode the tier does not allow is ignored. Changing the mode forgets the attacker. */
  void setMode(DefenseMode next) {
    if (robot.spec().shock().allows(next) && next != mode) {
      mode = next;
      memory = memory.forget();
      focus.clear();
    }
  }

  /** From the screen: only the owner, and only a mode of the tier (answers 829 and 834). */
  @Override
  public boolean choose(Player player, DefenseMode next) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID())
        || !robot.spec().shock().allows(next)
        || next == mode) {
      return false;
    }
    setMode(next);
    if (next == DefenseMode.AGGRESSIVE) {
      robot.playSound(CubitSounds.AGGRESSIVE_MODE.get());
    }
    return true;
  }

  /** The robot or its owner was hurt. Retaliate remembers the attacker for a while. */
  void hurtBy(LivingEntity attacker) {
    memory =
        memory.hurtBy(
            attacker.getUUID(),
            robot.level().getGameTime(),
            CubitMod.CONFIG.retaliateMemoryTicks());
  }

  /** The owner hurt a mob. Only Focus cares, and it takes the mob as its target. */
  void ownerHurt(LivingEntity mob) {
    if (mode == DefenseMode.FOCUS) {
      focus.ownerHurt(mob);
    }
  }

  void tick() {
    focus.tick();
    long now = robot.level().getGameTime();
    ShockSpec spec = robot.spec().shock();
    if (!ShockRules.ready(robot.powerState(), mode, spec, robot.battery(), lastAttemptTick, now)) {
      return;
    }
    lastAttemptTick = now;
    List<LivingEntity> targets = ShockRules.targets(spec, candidates(spec, now));
    if (!targets.isEmpty()) {
      RobotShock.strike(robot, targets, spec);
      engagedUntilTick = ShockRules.engagedUntil(now, spec);
    }
  }

  /** Between two strikes, the robot stays to defend instead of fetching (answer 939). */
  boolean engaged() {
    return ShockRules.engaged(engagedUntilTick, robot.level().getGameTime());
  }

  /** Nearest first, in range, in sight, and only who the mode shocks. */
  private List<LivingEntity> candidates(ShockSpec spec, long now) {
    Player owner = robot.onlineOwner();
    double range = spec.rangeBlocks();
    return robot
        .level()
        .getEntitiesOfClass(
            LivingEntity.class,
            robot.getBoundingBox().inflate(range),
            entity ->
                entity != robot
                    && entity.isAlive()
                    && robot.distanceTo(entity) <= range
                    && robot.hasLineOfSight(entity)
                    && DefenseRules.shocks(
                        mode,
                        ShockCandidates.of(robot, entity),
                        new ShockSignals(
                            ShockCandidates.angryAtOwner(owner, entity),
                            memory.remembers(entity.getUUID(), now),
                            focus.target().is(entity.getUUID()),
                            mode == DefenseMode.FOCUS && focus.target().hasTarget())))
        .stream()
        .sorted(Comparator.comparingDouble(robot::distanceToSqr))
        .toList();
  }
}
