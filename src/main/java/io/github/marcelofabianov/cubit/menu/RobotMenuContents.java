package io.github.marcelofabianov.cubit.menu;

import net.minecraft.world.Container;

/** The containers of the robot and what travels when its screen opens. */
public record RobotMenuContents(
    Container inventory, Container energySlot, Container torchSlot, RobotScreenOpening opening) {}
