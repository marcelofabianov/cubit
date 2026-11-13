package io.github.marcelofabianov.cubit.menu;

import java.util.function.Predicate;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

/** What only the server gives the robot menu: the checks, the values and the controls. */
record RobotMenuServer(
    Predicate<Player> stillValid,
    ContainerData values,
    Runnable refresh,
    RobotSwitch[] switches,
    DefenseControl defense,
    PowerControl power,
    PickupControl pickup) {
  private static final RobotSwitch[] NO_SWITCHES = {
    RobotSwitch.NONE, RobotSwitch.NONE, RobotSwitch.NONE, RobotSwitch.NONE, RobotSwitch.NONE
  };

  static RobotMenuServer onClient() {
    return new RobotMenuServer(
        player -> true,
        new SimpleContainerData(RobotScreenValues.COUNT),
        () -> {},
        NO_SWITCHES,
        DefenseControl.NONE,
        PowerControl.NONE,
        PickupControl.NONE);
  }
}
