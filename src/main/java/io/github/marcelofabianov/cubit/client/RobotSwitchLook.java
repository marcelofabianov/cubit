package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.item.TierLook;
import io.github.marcelofabianov.cubit.menu.BehaviorKeys;
import io.github.marcelofabianov.cubit.menu.DefenseKeys;
import io.github.marcelofabianov.cubit.menu.PickupScreenValues;
import io.github.marcelofabianov.cubit.menu.RobotMenu;
import io.github.marcelofabianov.cubit.menu.ScreenKeys;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerData;

/**
 * How each switch of a robot shows, by the button the server knows it by: on or off, whether it can
 * change, its detail and the tier that unlocks it. The tabs of the robot screen and of the Tablet
 * read the switches here, so they always agree (answer 1037).
 */
final class RobotSwitchLook {
  private final RobotMenu menu;

  RobotSwitchLook(RobotMenu menu) {
    this.menu = menu;
  }

  private ContainerData pickup() {
    return menu.pickupValues();
  }

  boolean on(int button) {
    return switch (button) {
      case RobotMenu.PICKUP_BUTTON -> PickupScreenValues.pickupOn(pickup());
      case RobotMenu.MAGNET_BUTTON -> PickupScreenValues.magnetOn(pickup());
      case RobotMenu.FETCH_BUTTON -> PickupScreenValues.fetchOn(pickup());
      default -> menu.switchOn(button);
    };
  }

  boolean available(int button) {
    return switch (button) {
      case RobotMenu.PICKUP_BUTTON -> true;
      case RobotMenu.MAGNET_BUTTON -> PickupScreenValues.magnetAvailable(pickup());
      case RobotMenu.FETCH_BUTTON -> PickupScreenValues.fetchAvailable(pickup());
      default -> menu.switchAvailable(button);
    };
  }

  /** The tier that unlocks a switch the tier of the robot does not have yet. */
  Optional<RobotTier> lockedUntil(int button) {
    return switch (button) {
      case RobotMenu.MAGNET_BUTTON ->
          PickupScreenValues.magnetUnlocked(pickup())
              ? Optional.empty()
              : PickupScreenValues.magnetUnlockTier(pickup());
      case RobotMenu.FETCH_BUTTON ->
          PickupScreenValues.fetchRadius(pickup()) > 0
              ? Optional.empty()
              : PickupScreenValues.fetchUnlockTier(pickup());
      case RobotMenu.TORCHES_BUTTON ->
          menu.switchAvailable(button) ? Optional.empty() : menu.torchUnlockTier();
      default -> Optional.empty();
    };
  }

  /** The detail of the tooltip: the numbers of the tier, or the tier that unlocks the switch. */
  Component detail(int button) {
    Optional<RobotTier> locked = lockedUntil(button);
    if (locked.isPresent()) {
      return unlocksAt(locked.get());
    }
    return switch (button) {
      case RobotMenu.LAMP_BUTTON -> RobotTexts.lampDetail(menu);
      case RobotMenu.SCANNERS_BUTTON -> RobotTexts.scannersDetail(menu);
      case RobotMenu.SOLAR_BUTTON -> RobotTexts.solarDetail(menu);
      case RobotMenu.TORCHES_BUTTON ->
          Component.translatable(ScreenKeys.TORCHES_DETAIL, menu.torchCost().amount());
      case RobotMenu.PICKUP_BUTTON -> Component.translatable(BehaviorKeys.PICKUP_DETAIL);
      case RobotMenu.MAGNET_BUTTON ->
          Component.translatable(
              BehaviorKeys.MAGNET_DETAIL,
              PickupScreenValues.radius(pickup()),
              PickupScreenValues.cost(pickup()).amount());
      case RobotMenu.FETCH_BUTTON ->
          Component.translatable(
              BehaviorKeys.FETCH_DETAIL, PickupScreenValues.fetchRadius(pickup()));
      default -> tripDetail(button);
    };
  }

  /** The switches of the trips to the Dock and to the chests, and the chatter. */
  private Component tripDetail(int button) {
    return switch (button) {
      case RobotMenu.AUTO_RETURN_BUTTON ->
          Component.translatable(ScreenKeys.AUTO_RETURN_DETAIL, menu.opening().returnCharge());
      case RobotMenu.UNLOAD_BUTTON -> Component.translatable(ScreenKeys.UNLOAD_DETAIL);
      default -> Component.translatable(ScreenKeys.CHATTER_DETAIL);
    };
  }

  static Component unlocksAt(RobotTier tier) {
    return Component.translatable(DefenseKeys.UNLOCKS_AT, TierLook.name(tier));
  }
}
