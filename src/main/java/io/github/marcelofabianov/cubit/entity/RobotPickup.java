package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.behavior.PickupRules;
import io.github.marcelofabianov.cubit.domain.behavior.ScanRules;
import io.github.marcelofabianov.cubit.domain.robot.PickupItem;
import io.github.marcelofabianov.cubit.domain.robot.PickupSettings;
import io.github.marcelofabianov.cubit.domain.tier.PickupSpec;
import io.github.marcelofabianov.cubit.menu.RobotStorage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Picks up the items the robot touches and pulls the ones in the magnet radius (answers 876 to
 * 887). The rules live in the domain. Only the settings are saved.
 */
public final class RobotPickup {
  private static final double TOUCH_REACH_BLOCKS = 1;
  private static final double PULL_SPEED = 0.3;

  private final RobotEntity robot;
  private final Set<UUID> paidPulls = new HashSet<>();
  private PickupSettings settings = PickupSettings.INITIAL;

  RobotPickup(RobotEntity robot) {
    this.robot = robot;
  }

  public PickupSettings settings() {
    return settings;
  }

  public void setSettings(PickupSettings next) {
    settings = next;
  }

  void tick() {
    if (!PickupRules.collects(robot.powerState(), settings.pickupOn())) {
      paidPulls.clear();
      return;
    }
    if (ScanRules.isDue(robot.tickCount, CubitMod.CONFIG.pickupCheckTicks())) {
      collectTouching();
    }
    if (ScanRules.isDue(robot.tickCount, CubitMod.CONFIG.magnetCheckTicks())) {
      pullInRadius(robot.spec().pickup());
    }
  }

  private void collectTouching() {
    AABB reach = robot.getBoundingBox().inflate(TOUCH_REACH_BLOCKS);
    for (ItemEntity item : wanted(reach)) {
      ItemStack stack = item.getItem().copy();
      if (RobotStorage.insert(robot.inventory(), stack)) {
        robot.take(item, item.getItem().getCount() - stack.getCount());
        paidPulls.remove(item.getUUID());
        if (stack.isEmpty()) {
          item.discard();
        } else {
          item.setItem(stack);
        }
      }
    }
  }

  /** Each item is paid once, however many checks it takes to arrive (answer 877). */
  private void pullInRadius(PickupSpec spec) {
    Set<UUID> inRange = new HashSet<>();
    for (ItemEntity item : wanted(robot.getBoundingBox().inflate(spec.magnetRadiusBlocks()))) {
      boolean paid = paidPulls.contains(item.getUUID());
      if (!paid && !payForPull(spec)) {
        continue;
      }
      inRange.add(item.getUUID());
      paidPulls.add(item.getUUID());
      Vec3 toRobot = robot.position().subtract(item.position()).normalize();
      item.setDeltaMovement(toRobot.scale(PULL_SPEED));
    }
    paidPulls.retainAll(inRange);
  }

  private boolean payForPull(PickupSpec spec) {
    if (!PickupRules.magnetPulls(
        robot.powerState(), settings.pickupOn(), settings.magnetOn(), spec, robot.battery())) {
      return false;
    }
    robot.setBattery(PickupRules.payForPull(robot.battery(), spec));
    return true;
  }

  /** Alive, past the delay of the game, allowed by the filter and with room (answers 885, 886). */
  private List<ItemEntity> wanted(AABB area) {
    PickupSpec spec = robot.spec().pickup();
    return robot
        .level()
        .getEntitiesOfClass(
            ItemEntity.class,
            area,
            item ->
                item.isAlive()
                    && !item.hasPickUpDelay()
                    && settings.filter().allows(viewOf(item.getItem()), spec)
                    && RobotStorage.hasRoomFor(robot.inventory(), item.getItem()));
  }

  static PickupItem viewOf(ItemStack stack) {
    Set<String> tags =
        stack.getTags().map(TagKey::location).map(Object::toString).collect(Collectors.toSet());
    return new PickupItem(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString(), tags);
  }
}
