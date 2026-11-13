package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.FetchMemory;
import io.github.marcelofabianov.cubit.domain.behavior.FetchRules;
import io.github.marcelofabianov.cubit.domain.behavior.PickupRules;
import io.github.marcelofabianov.cubit.domain.behavior.ScanRules;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.PickupEntry;
import io.github.marcelofabianov.cubit.domain.robot.PickupFilter;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.menu.PickupControl;
import io.github.marcelofabianov.cubit.menu.PickupDisplay;
import io.github.marcelofabianov.cubit.menu.RobotStorage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Picks up the items the robot touches, pulls the ones in the magnet radius and chooses the item to
 * fetch (answers 876 to 887 and 936 to 942). The rules live in the domain. Only the settings are
 * saved, so the items given up on are forgotten when the world closes.
 */
public final class RobotPickup implements PickupControl {
  private static final double TOUCH_REACH_BLOCKS = 1;
  private static final double PULL_SPEED = 0.5;

  private final RobotEntity robot;
  private final Map<UUID, ItemEntity> pulled = new HashMap<>();
  private PickupSettings settings = PickupSettings.INITIAL;
  private FetchMemory fetchMemory = FetchMemory.EMPTY;

  RobotPickup(RobotEntity robot) {
    this.robot = robot;
  }

  @Override
  public PickupSettings settings() {
    return settings;
  }

  @Override
  public PickupSpec spec() {
    return robot.spec().pickup();
  }

  @Override
  public Optional<RobotTier> magnetUnlockTier() {
    return Arrays.stream(RobotTier.values())
        .filter(tier -> robot.specAt(tier).pickup().magnetUnlocked())
        .findFirst();
  }

  @Override
  public Optional<RobotTier> fetchUnlockTier() {
    return Arrays.stream(RobotTier.values())
        .filter(tier -> robot.specAt(tier).pickup().fetchUnlocked())
        .findFirst();
  }

  @Override
  public boolean press(Player player, PickupButton button) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID())) {
      return false;
    }
    PickupSettings next =
        switch (button) {
          case PICKUP -> settings.toggledPickup();
          case MAGNET ->
              spec().magnetUnlocked() && settings.pickupOn() ? settings.toggledMagnet() : settings;
          case MODE -> settings.withFilter(settings.filter().toggledMode());
          case FETCH ->
              spec().fetchUnlocked() && settings.pickupOn() ? settings.toggledFetch() : settings;
        };
    return change(next);
  }

  @Override
  public boolean place(Player player, int slot, ItemStack carried) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID())) {
      return false;
    }
    PickupFilter filter = settings.filter();
    PickupFilter next =
        carried.isEmpty()
            ? filter.cleared(slot)
            : filter.placed(slot, PickupEntry.item(PickupDisplay.idOf(carried)), spec());
    return change(settings.withFilter(next));
  }

  @Override
  public boolean cycle(Player player, int slot) {
    List<PickupEntry> entries = settings.filter().entries();
    if (!Ownership.owns(robot.ownerId(), player.getUUID()) || slot < 0 || slot >= entries.size()) {
      return false;
    }
    PickupEntry entry = entries.get(slot);
    ItemStack shown = PickupDisplay.stackOf(entry);
    String itemId = entry.tag() ? PickupDisplay.idOf(shown) : entry.id();
    PickupFilter next =
        settings.filter().cycled(slot, itemId, PickupDisplay.sortedTags(shown), spec());
    return change(settings.withFilter(next));
  }

  private boolean change(PickupSettings next) {
    if (next.equals(settings)) {
      return false;
    }
    settings = next;
    return true;
  }

  public void setSettings(PickupSettings next) {
    settings = next;
  }

  void tick() {
    if (!PickupRules.collects(robot.powerState(), settings.pickupOn())) {
      pulled.clear();
      return;
    }
    steerPulled();
    if (ScanRules.isDue(robot.tickCount, CubitMod.CONFIG.pickupCheckTicks())) {
      collectTouching();
    }
    if (ScanRules.isDue(robot.tickCount, CubitMod.CONFIG.magnetCheckTicks())) {
      pullInRadius(robot.spec().pickup());
    }
  }

  private void collectTouching() {
    wanted(reach()).forEach(this::collect);
  }

  private AABB reach() {
    return robot.getBoundingBox().inflate(TOUCH_REACH_BLOCKS);
  }

  private void collect(ItemEntity item) {
    ItemStack stack = item.getItem().copy();
    if (RobotStorage.insert(robot.inventory(), stack)) {
      robot.take(item, item.getItem().getCount() - stack.getCount());
      pulled.remove(item.getUUID());
      if (stack.isEmpty()) {
        item.discard();
      } else {
        item.setItem(stack);
      }
    }
  }

  /**
   * A paid item is guided every tick and taken as soon as it touches, without waiting for the next
   * check, so it arrives in a fraction of a second, still moving (answer 928).
   */
  private void steerPulled() {
    pulled.values().removeIf(item -> !item.isAlive());
    AABB reach = reach();
    for (ItemEntity item : List.copyOf(pulled.values())) {
      if (item.getBoundingBox().intersects(reach)) {
        collect(item);
      } else {
        Vec3 toRobot = robot.position().subtract(item.position());
        item.setDeltaMovement(toRobot.normalize().scale(Math.min(PULL_SPEED, toRobot.length())));
      }
    }
  }

  /** Each item is paid once, however many checks it takes to arrive (answer 877). */
  private void pullInRadius(PickupSpec spec) {
    Set<UUID> inRange = new HashSet<>();
    for (ItemEntity item : wanted(robot.getBoundingBox().inflate(spec.magnetRadiusBlocks()))) {
      boolean paid = pulled.containsKey(item.getUUID());
      if (!paid && !payForPull(spec)) {
        continue;
      }
      inRange.add(item.getUUID());
      pulled.put(item.getUUID(), item);
    }
    pulled.keySet().retainAll(inRange);
  }

  private boolean payForPull(PickupSpec spec) {
    if (!PickupRules.magnetPulls(
        robot.powerState(), settings.pickupOn(), settings.magnetOn(), spec, robot.battery())) {
      return false;
    }
    robot.setBattery(PickupRules.payForPull(robot.battery(), spec));
    return true;
  }

  /**
   * The item on the ground nearest to the owner, in the fetch radius around the owner and not given
   * up on. Only loaded entities are seen, so no chunk is loaded (rule 22).
   */
  Optional<ItemEntity> fetchTarget(Player owner) {
    PickupSpec spec = spec();
    if (!spec.fetchUnlocked()) {
      return Optional.empty();
    }
    long now = robot.level().getGameTime();
    List<ItemEntity> candidates =
        robot
            .level()
            .getEntitiesOfClass(
                ItemEntity.class,
                owner.getBoundingBox().inflate(spec.fetchRadiusBlocks()),
                item -> wants(item) && !fetchMemory.ignores(item.getUUID(), now));
    return FetchRules.target(candidates, owner::distanceTo, spec);
  }

  /** The target is still worth the walk: still wanted and still in reach of the owner. */
  boolean stillFetching(ItemEntity item, Player owner) {
    return wants(item) && FetchRules.inReach(owner.distanceTo(item), spec());
  }

  /** The robot did not reach the item in time or found no route, so it leaves it for a while. */
  void giveUp(ItemEntity item) {
    fetchMemory =
        fetchMemory.ignore(
            item.getUUID(), robot.level().getGameTime(), CubitMod.CONFIG.fetchIgnoreTicks());
  }

  /** Alive, past the delay of the game, allowed by the filter and with room (answers 885, 886). */
  private List<ItemEntity> wanted(AABB area) {
    return robot.level().getEntitiesOfClass(ItemEntity.class, area, this::wants);
  }

  private boolean wants(ItemEntity item) {
    return item.isAlive()
        && !item.hasPickUpDelay()
        && settings.filter().allows(PickupDisplay.itemOf(item.getItem()), spec())
        && RobotStorage.hasRoomFor(robot.inventory(), item.getItem());
  }
}
