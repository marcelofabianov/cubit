package io.github.marcelofabianov.cubit.menu;

import io.github.marcelofabianov.cubit.domain.behavior.RobotExpression;
import io.github.marcelofabianov.cubit.domain.behavior.RobotScreenStatus;
import io.github.marcelofabianov.cubit.domain.energy.Energy;
import io.github.marcelofabianov.cubit.domain.energy.EnergyUseCause;
import io.github.marcelofabianov.cubit.domain.energy.RechargeSource;
import io.github.marcelofabianov.cubit.domain.energy.SolarStatus;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import java.util.List;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;

/**
 * The numbers of the robot screen as the client reads them, shared by the screen of the robot and
 * the one of the Tablet (answer 1023). The menu that extends this owns the slots and the buttons.
 */
public abstract class RobotMenuReading extends AbstractContainerMenu {
  private static final int TORCH_SWITCH = 4;
  private static final int[] SWITCH_INDEXES = {
    RobotScreenValues.LAMP,
    RobotScreenValues.SCANNERS,
    RobotScreenValues.CHATTER,
    RobotScreenValues.SOLAR_SWITCH,
    RobotScreenValues.TORCHES
  };

  protected final ContainerData values;

  protected RobotMenuReading(MenuType<?> type, int containerId, ContainerData values) {
    super(type, containerId);
    this.values = values;
  }

  public Energy charge() {
    return RobotScreenValues.energy(values, RobotScreenValues.CHARGE);
  }

  public Energy capacity() {
    return RobotScreenValues.energy(values, RobotScreenValues.CAPACITY);
  }

  public int health() {
    return values.get(RobotScreenValues.HEALTH);
  }

  public int maxHealth() {
    return values.get(RobotScreenValues.MAX_HEALTH);
  }

  public RobotScreenStatus status() {
    return ScreenEnums.of(RobotScreenStatus.values(), values.get(RobotScreenValues.STATUS));
  }

  public RechargeSource recharge() {
    return ScreenEnums.of(RechargeSource.values(), values.get(RobotScreenValues.RECHARGE));
  }

  public Energy rechargeRate() {
    return RobotScreenValues.energy(values, RobotScreenValues.RECHARGE_RATE);
  }

  public SolarStatus solar() {
    return ScreenEnums.of(SolarStatus.values(), values.get(RobotScreenValues.SOLAR));
  }

  public Energy solarRate() {
    return RobotScreenValues.energy(values, RobotScreenValues.SOLAR_RATE);
  }

  public Energy use() {
    return RobotScreenValues.energy(values, RobotScreenValues.USE);
  }

  public List<EnergyUseCause> causes() {
    return RobotScreenValues.causes(values);
  }

  public boolean following() {
    return RobotScreenValues.isTrue(values, RobotScreenValues.FOLLOWING);
  }

  public RobotExpression expression() {
    return ScreenEnums.of(RobotExpression.values(), values.get(RobotScreenValues.EXPRESSION));
  }

  public boolean powerAvailable() {
    return RobotScreenValues.isTrue(values, RobotScreenValues.POWER);
  }

  public DefenseMode defenseMode() {
    return ScreenEnums.of(DefenseMode.values(), values.get(RobotScreenValues.DEFENSE));
  }

  /** The shell color, for the portrait in every screen (answer 1011). */
  public RobotColor shellColor() {
    return ScreenEnums.of(RobotColor.values(), values.get(RobotScreenValues.COLOR));
  }

  public int blockX() {
    return RobotScreenValues.intAt(values, RobotScreenValues.POS_X);
  }

  public int blockY() {
    return RobotScreenValues.intAt(values, RobotScreenValues.POS_Y);
  }

  public int blockZ() {
    return RobotScreenValues.intAt(values, RobotScreenValues.POS_Z);
  }

  /** A switched-off robot with charge to wake, which the Power of the Tablet turns on (1033). */
  public boolean canTurnOn() {
    return RobotScreenValues.isTrue(values, RobotScreenValues.TURN_ON);
  }

  public RobotTier tier() {
    return ScreenEnums.of(RobotTier.values(), values.get(RobotScreenValues.TIER));
  }

  /** By the button of the switch: {@link #LAMP_BUTTON}, {@link #SCANNERS_BUTTON} or chatter. */
  public boolean switchOn(int button) {
    return RobotScreenValues.isTrue(values, SWITCH_INDEXES[switchIndex(button)]);
  }

  public boolean switchAvailable(int button) {
    return RobotScreenValues.isAvailable(values, SWITCH_INDEXES[switchIndex(button)]);
  }

  /** The first four switches go by their button, and the torches come after them. */
  static int switchIndex(int button) {
    return button == RobotMenu.TORCHES_BUTTON ? TORCH_SWITCH : button;
  }
}
