package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import io.github.marcelofabianov.cubit.domain.energy.RechargeSource;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.List;

/** What the robot screen shows that changes while it is open, read on the server. */
public record RobotScreenReading(
    Battery battery,
    Vitals vitals,
    RobotScreenStatus status,
    RechargeSource recharge,
    Energy rechargeRate,
    SolarStatus solar,
    Energy solarRate,
    Energy use,
    List<EnergyUseCause> causes,
    RobotTier tier,
    Place place) {

  /**
   * The shell color, the block and whether the robot could be turned on now, for the header of the
   * Tablet and the portrait (answers 1011 and 1033).
   */
  public record Place(RobotColor color, int x, int y, int z, boolean canTurnOn) {}

  /** The health, whether it follows its owner and the face it shows now. */
  public record Vitals(
      float health, float maxHealth, boolean following, RobotExpression expression) {}
}
