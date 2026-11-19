package io.github.zenndg.cubit.entity;

import com.mojang.logging.LogUtils;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.behavior.AwayHibernation;
import io.github.zenndg.cubit.domain.behavior.HibernationReason;
import io.github.zenndg.cubit.domain.energy.PowerState;
import io.github.zenndg.cubit.domain.ownership.Ownership;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.menu.WorkControl;
import io.github.zenndg.cubit.menu.WorkKeys;
import io.github.zenndg.cubit.network.CubitNetwork;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

/**
 * The job of the Cubit, from the card in its slot, and the hibernation away from the Dock: with a
 * card, in its area, full and with nowhere to unload, it falls asleep until Resume, Follow or Go to
 * Dock (answers 1164 to 1171). It looks only once per interval (rule 21).
 */
public final class RobotWork implements WorkControl {
  private static final Logger LOGGER = LogUtils.getLogger();
  private static final String REASON_KEY = "HibernationReason";
  private static final String INVALID_REASON = "Robot {} has an invalid hibernation reason: {}";

  private final RobotEntity robot;
  private final RobotFishing fishing;
  private final RobotFarming farming;
  private HibernationReason reason = HibernationReason.INVENTORY_FULL;
  private long nextCheck = Long.MIN_VALUE;

  RobotWork(RobotEntity robot) {
    this.robot = robot;
    this.fishing = new RobotFishing(robot);
    this.farming = new RobotFarming(robot);
  }

  /** The work of the card of the Fisher, with its Keep junk switch. */
  public RobotFishing fishing() {
    return fishing;
  }

  /** The work of the card of the Farmer, with its Use bone meal switch. */
  public RobotFarming farming() {
    return farming;
  }

  /** The card slot, for the screen. */
  public Container slot() {
    return robot.robotInventory().cardSlot().container();
  }

  /** The fertilizer slot of the Farmer, for the screen (answer 1244). */
  public Container fertilizerSlot() {
    return robot.robotInventory().fertilizerSlot().container();
  }

  @Override
  public Optional<RobotTier> fertilizerUnlockTier() {
    return Arrays.stream(RobotTier.values())
        .filter(tier -> robot.specAt(tier).jobs().farmer().boneMeal())
        .findFirst();
  }

  @Override
  public int unloadIntervalMinutes() {
    return robot.docking().unload().intervalMinutes();
  }

  @Override
  public boolean stepUnloadInterval(Player player, int direction) {
    return robot.docking().unload().stepInterval(player, direction);
  }

  @Override
  public boolean fertilizerUnlocked() {
    return robot.spec().jobs().farmer().boneMeal();
  }

  @Override
  public Optional<RobotJob> job() {
    return robot.robotInventory().cardSlot().job();
  }

  @Override
  public Optional<HibernationReason> hibernation() {
    return asleep() ? Optional.of(reason) : Optional.empty();
  }

  @Override
  public Optional<RobotTier> cardUnlockTier() {
    return Arrays.stream(RobotTier.values())
        .filter(tier -> robot.specAt(tier).jobs().unlocked())
        .findFirst();
  }

  @Override
  public Set<RobotJob> allowedJobs() {
    return robot.spec().jobs().allowed();
  }

  @Override
  public Optional<RobotTier> tierFor(RobotJob job) {
    return Arrays.stream(RobotTier.values())
        .filter(tier -> robot.specAt(tier).jobs().accepts(job))
        .findFirst();
  }

  /** Only the owner. A reason that did not go away puts it back to sleep and says it again. */
  @Override
  public boolean resume(Player player) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || !asleep()) {
      return false;
    }
    wake();
    fishing.restart();
    reasonNow().ifPresent(this::hibernate);
    return true;
  }

  /** Follow and Go to Dock also wake it, before their own order (answer 1170). */
  public void wake() {
    if (asleep()) {
      robot.setPowerState(PowerState.ACTIVE);
      nextCheck = robot.level().getGameTime() + CubitMod.CONFIG.followRepathTicks();
    }
  }

  /** Called on the server on every tick. Without the card, the sleep ends (answer 1171). */
  void tick() {
    showJob();
    fishing.tick();
    farming.tick();
    if (asleep()) {
      if (job().isEmpty()) {
        wake();
      }
      return;
    }
    long now = robot.level().getGameTime();
    if (robot.powerState() != PowerState.ACTIVE || now < nextCheck) {
      return;
    }
    nextCheck = now + CubitMod.CONFIG.followRepathTicks();
    reasonNow().ifPresent(this::hibernate);
  }

  private Optional<HibernationReason> reasonNow() {
    RobotUnload unload = robot.docking().unload();
    boolean canUnload = unload.switchedOn() && (unload.onTrip() || unload.nextChest().isPresent());
    return AwayHibernation.reason(
        job().isPresent(),
        robot.workArea().working(),
        robot.robotInventory().isFull(),
        canUnload,
        fishing.reason());
  }

  private void hibernate(HibernationReason why) {
    reason = why;
    robot.getNavigation().stop();
    robot.setPowerState(PowerState.HIBERNATING_AWAY);
    tellOwner();
  }

  /** In its area the Cubit follows nobody, so the text goes to the owner online (answer 1174). */
  private void tellOwner() {
    if (RobotInteractions.onlineOwner(robot) instanceof ServerPlayer owner) {
      Component text = Component.translatable(WorkKeys.notice(reason));
      CubitNetwork.sendNotice(owner, Component.translatable(RobotNotices.FORMAT_KEY, text));
    }
  }

  /** The job of the card for the looks of the client, which only change with it (answer 1197). */
  private void showJob() {
    byte shown = job().map(job -> (byte) (job.ordinal() + 1)).orElse(RobotSyncedData.NO_JOB);
    robot.getEntityData().set(RobotSyncedData.JOB, shown);
  }

  private boolean asleep() {
    return robot.powerState() == PowerState.HIBERNATING_AWAY;
  }

  void save(CompoundTag tag) {
    if (asleep()) {
      tag.putString(REASON_KEY, reason.key());
    }
  }

  /** An unknown reason is the one of the base, and the log says so (answer 1175). */
  void load(CompoundTag tag) {
    if (!asleep()) {
      return;
    }
    String key = tag.getString(REASON_KEY);
    reason =
        HibernationReason.fromKey(key)
            .orElseGet(() -> {
              LOGGER.warn(INVALID_REASON, robot.getUUID(), key);
              return HibernationReason.INVENTORY_FULL;
            });
  }
}
