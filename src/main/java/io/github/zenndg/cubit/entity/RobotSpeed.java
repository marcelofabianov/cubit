package io.github.zenndg.cubit.entity;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.ownership.Ownership;
import io.github.zenndg.cubit.domain.robot.SpeedChoice;
import io.github.zenndg.cubit.menu.SpeedControl;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

/**
 * The speed the player chose with − and +, as a percentage of the speed of the tier, in every walk
 * but in danger (answers 1259, 1266 and 1268). The modifier multiplies the total, so it combines
 * with the one of the full inventory by multiplication. The rule lives in {@link SpeedChoice}.
 */
public final class RobotSpeed implements SpeedControl {
  private static final String MODIFIER_PATH = "chosen_speed";
  private static final ResourceLocation MODIFIER_ID =
      ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, MODIFIER_PATH);

  private final RobotEntity robot;
  private SpeedChoice choice = SpeedChoice.FULL;

  RobotSpeed(RobotEntity robot) {
    this.robot = robot;
  }

  public SpeedChoice choice() {
    return choice;
  }

  /** Fitted to the config, which may have changed while the Cubit was an item (answer 1266). */
  void setChoice(SpeedChoice next) {
    choice = next.fitted(CubitMod.CONFIG.speedSteps());
  }

  @Override
  public int percent() {
    return choice.percent();
  }

  @Override
  public int minPercent() {
    return CubitMod.CONFIG.speedSteps().minPercent();
  }

  /** The buttons − and +, only for the owner (rule 20). */
  @Override
  public boolean step(Player player, int direction) {
    if (!Ownership.owns(robot.ownerId(), player.getUUID())) {
      return false;
    }
    choice = choice.step(direction, CubitMod.CONFIG.speedSteps());
    return true;
  }

  /** Not saved with the robot: each tick applies both modifiers again from its state. */
  void tick(boolean fullInventory) {
    RobotLoadSpeed.update(robot, fullInventory);
    boolean inDanger = robot.isOnFire() || robot.isInLava() || DamagingBlocks.hurt(robot);
    double factor = choice.factor(inDanger);
    AttributeInstance speed = robot.getAttribute(Attributes.MOVEMENT_SPEED);
    AttributeModifier current = speed.getModifier(MODIFIER_ID);
    double amount = factor - 1;
    if (current != null && current.amount() == amount) {
      return;
    }
    speed.removeModifier(MODIFIER_ID);
    if (amount != 0) {
      speed.addTransientModifier(
          new AttributeModifier(
              MODIFIER_ID, amount, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }
  }
}
