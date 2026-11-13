package io.github.marcelofabianov.cubit.menu;

import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.world.entity.player.Player;

/** What the server reads from the robot: the screen values, the switches and who may use it. */
public record RobotMenuSource(
    Supplier<RobotScreenReading> reading,
    RobotSwitch[] switches,
    DefenseControl defense,
    PowerControl power,
    PickupControl pickup,
    TorchControl torches,
    Predicate<Player> stillValid) {}
