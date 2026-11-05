package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.RobotState;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.domain.tier.RobotTier;
import io.github.marcelofabianov.cubit.domain.tier.TierSpec;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/** A robot stored as an item. It keeps the whole state, so collecting and placing lose nothing. */
public final class RobotItem extends Item {
  private static final int BAR_FULL_WIDTH = 13;
  private static final int BAR_COLOR = 0x55FF55;
  private static final float FACE_THE_PLAYER_DEGREES = 180f;

  private final RobotType robotType;
  private final Supplier<EntityType<RobotEntity>> entityType;

  public RobotItem(
      Properties properties, RobotType robotType, Supplier<EntityType<RobotEntity>> entityType) {
    super(properties);
    this.robotType = robotType;
    this.entityType = entityType;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    Player player = context.getPlayer();
    if (player == null) {
      return InteractionResult.PASS;
    }
    BlockPos target = context.getClickedPos().relative(context.getClickedFace());
    RobotEntity robot = spawnRobot(level, target, player.getYRot() + FACE_THE_PLAYER_DEGREES);
    if (robot == null) {
      return InteractionResult.FAIL;
    }
    ItemStack stack = context.getItemInHand();
    robot.placedBy(player, stateOf(stack), stack);
    // Always spent, even in creative: the item carries the state, and a copy would duplicate it.
    stack.shrink(1);
    return InteractionResult.CONSUME;
  }

  /** The robot leaves the repair with part of its health and an empty battery. */
  ItemStack repaired(RobotState damaged) {
    ItemStack stack = new ItemStack(this);
    RobotState repaired = damaged.repaired(specOf(damaged.tier()), CubitMod.CONFIG.repairHealth());
    stack.set(CubitDataComponents.ROBOT_STATE.get(), repaired);
    return stack;
  }

  @Override
  public boolean canFitInsideContainerItems() {
    return false;
  }

  @Override
  public boolean isBarVisible(ItemStack stack) {
    return true;
  }

  @Override
  public int getBarWidth(ItemStack stack) {
    RobotState state = stateOf(stack);
    return state.batteryFor(specOf(state.tier())).level(BAR_FULL_WIDTH);
  }

  @Override
  public int getBarColor(ItemStack stack) {
    return BAR_COLOR;
  }

  private RobotEntity spawnRobot(Level level, BlockPos target, float yaw) {
    Vec3 position = Vec3.atBottomCenterOf(target);
    EntityType<RobotEntity> type = entityType.get();
    if (!level.noCollision(type.getSpawnAABB(position.x, position.y, position.z))) {
      return null;
    }
    RobotEntity robot = type.create(level);
    if (robot == null) {
      return null;
    }
    robot.moveTo(position.x, position.y, position.z, yaw, 0);
    robot.setYHeadRot(yaw);
    robot.setYBodyRot(yaw);
    return level.addFreshEntity(robot) ? robot : null;
  }

  /** An item without a saved state, like the one in the creative tab, is a new robot. */
  private RobotState stateOf(ItemStack stack) {
    RobotState saved = stack.get(CubitDataComponents.ROBOT_STATE.get());
    if (saved != null) {
      return saved;
    }
    return RobotState.initial(
        RobotTier.BASE, specOf(RobotTier.BASE), CubitMod.CONFIG.initialCharge());
  }

  private TierSpec specOf(RobotTier tier) {
    return CubitMod.CONFIG.tierCatalog().spec(robotType, tier);
  }
}
