package io.github.zenndg.cubit.menu;

import io.github.zenndg.cubit.domain.behavior.HibernationReason;
import io.github.zenndg.cubit.domain.behavior.RobotExpression;
import io.github.zenndg.cubit.domain.behavior.RobotScreenStatus;
import io.github.zenndg.cubit.domain.energy.Energy;
import io.github.zenndg.cubit.domain.energy.EnergyUseCause;
import io.github.zenndg.cubit.domain.energy.RechargeSource;
import io.github.zenndg.cubit.domain.energy.SolarStatus;
import io.github.zenndg.cubit.domain.robot.DefenseMode;
import io.github.zenndg.cubit.domain.robot.RobotColor;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import java.util.List;
import java.util.Optional;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;

/**
 * The numbers of the robot screen as the client reads them, shared by the screen of the robot and
 * the one of the Tablet (answer 1023). The menu that extends this owns the slots and the buttons.
 */
public abstract class RobotMenuReading extends AbstractContainerMenu {
  private static final int TORCH_SWITCH = 4;
  private static final int AUTO_RETURN_SWITCH = 5;
  private static final int UNLOAD_SWITCH = 6;
  private static final int DEATH_POINT_SWITCH = 7;
  private static final int KEEP_JUNK_SWITCH = 8;
  private static final int[] SWITCH_INDEXES = {
    RobotScreenValues.LAMP,
    RobotScreenValues.SCANNERS,
    RobotScreenValues.CHATTER,
    RobotScreenValues.SOLAR_SWITCH,
    RobotScreenValues.TORCHES,
    RobotScreenValues.AUTO_RETURN,
    RobotScreenValues.UNLOAD,
    RobotScreenValues.DEATH_POINT,
    RobotScreenValues.KEEP_JUNK
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

  /** The job of the card in the slot, if any (answer 1162). */
  public Optional<RobotJob> job() {
    return ofIndex(RobotJob.values(), values.get(RobotScreenValues.JOB));
  }

  /** Why the Cubit hibernates away from the Dock, if it does (answer 1164). */
  public Optional<HibernationReason> hibernationReason() {
    return ofIndex(HibernationReason.values(), values.get(RobotScreenValues.HIBERNATION_REASON));
  }

  /** The first tier that takes any card, for the locked slot (answer 1162). */
  /** The unload interval of the Work tab, in minutes, with zero for only when full (1252). */
  public int unloadIntervalMinutes() {
    return values.get(RobotScreenValues.UNLOAD_INTERVAL);
  }

  /** The percentage of the speed of the tier, and the lowest one, for the speed row (1269). */
  public int speedPercent() {
    return values.get(RobotScreenValues.SPEED);
  }

  public int speedMinPercent() {
    return values.get(RobotScreenValues.SPEED_MIN);
  }

  /** Whether the tier of this Cubit takes fertilizer in the slot of the Farmer (1244). */
  public boolean fertilizerUnlocked() {
    return RobotScreenValues.isTrue(values, RobotScreenValues.FERTILIZER_UNLOCKED);
  }

  public Optional<RobotTier> fertilizerUnlockTier() {
    int value = values.get(RobotScreenValues.FERTILIZER_UNLOCK_TIER);
    return value == 0
        ? Optional.empty()
        : Optional.of(ScreenEnums.of(RobotTier.values(), value - 1));
  }

  public Optional<RobotTier> cardUnlockTier() {
    return ofIndex(RobotTier.values(), values.get(RobotScreenValues.CARD_UNLOCK_TIER));
  }

  /** Whether this tier takes any card, so the card slot is not locked (answer 1162). */
  public boolean cardsUnlocked() {
    return values.get(RobotScreenValues.ALLOWED_JOBS) != 0;
  }

  /** Whether the tier of this Cubit takes the job, for the card slot (answer 1163). */
  public boolean acceptsJob(RobotJob job) {
    return (values.get(RobotScreenValues.ALLOWED_JOBS) & (1 << job.ordinal())) != 0;
  }

  /** The first tier that takes the job, for the hint of a card that does not fit (1162). */
  public Optional<RobotTier> tierFor(RobotJob job) {
    return ofIndex(RobotTier.values(), RobotScreenValues.jobTier(values, job));
  }

  /** A value of the server is the order plus one, with zero for none. */
  private static <T> Optional<T> ofIndex(T[] all, int value) {
    return value <= 0 || value > all.length ? Optional.empty() : Optional.of(all[value - 1]);
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

  /** The first four switches go by their button, and the torches and the return come after. */
  static int switchIndex(int button) {
    return switch (button) {
      case RobotMenu.TORCHES_BUTTON -> TORCH_SWITCH;
      case RobotMenu.AUTO_RETURN_BUTTON -> AUTO_RETURN_SWITCH;
      case RobotMenu.UNLOAD_BUTTON -> UNLOAD_SWITCH;
      case RobotMenu.DEATH_POINT_BUTTON -> DEATH_POINT_SWITCH;
      case RobotMenu.KEEP_JUNK_BUTTON -> KEEP_JUNK_SWITCH;
      default -> button;
    };
  }
}
