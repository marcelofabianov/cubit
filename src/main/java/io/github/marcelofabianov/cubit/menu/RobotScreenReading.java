package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.Battery;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import io.github.marcelofabianov.cubit.domain.energy.RechargeSource;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus;
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
    RobotTier tier) {

  /** The health, whether it follows its owner and the face it shows now. */
  public record Vitals(
      float health, float maxHealth, boolean following, RobotExpression expression) {}
}
