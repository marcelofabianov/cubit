package io.github.marcelofabianov.cubit.block;

import com.mojang.serialization.MapCodec;
import io.github.marcelofabianov.cubit.domain.dock.DockRemoval;
import io.github.marcelofabianov.cubit.menu.InductionDockMenu;
import io.github.marcelofabianov.cubit.registry.CubitBlockEntities;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The charging base of the robots, with an owner. It takes two blocks of height, like a door: the
 * lower part holds everything, and the upper part only keeps the space free.
 */
public final class InductionDockBlock extends BaseEntityBlock {
  private static final String STATUS_NAME = "status";
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
  public static final EnumProperty<DockStatusProperty> STATUS =
      EnumProperty.create(STATUS_NAME, DockStatusProperty.class);
  private static final MapCodec<InductionDockBlock> CODEC = simpleCodec(InductionDockBlock::new);
  private static final float CANNOT_BREAK = 0f;
  private static final int OPERATOR_LEVEL = 2;
  private static final int REMOVE_WITHOUT_DROPS = 35;

  public InductionDockBlock(Properties properties) {
    super(properties);
    registerDefaultState(
        stateDefinition
            .any()
            .setValue(FACING, Direction.NORTH)
            .setValue(HALF, DoubleBlockHalf.LOWER)
            .setValue(STATUS, DockStatusProperty.OFF));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, HALF, STATUS);
  }

  private static boolean isLower(BlockState state) {
    return state.getValue(HALF) == DoubleBlockHalf.LOWER;
  }

  /** The block entity lives in the lower part, and the upper part answers for it. */
  public static InductionDockBlockEntity dockAt(BlockGetter level, BlockPos pos, BlockState state) {
    BlockPos lower = isLower(state) ? pos : pos.below();
    return level.getBlockEntity(lower) instanceof InductionDockBlockEntity dock ? dock : null;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return isLower(state) ? new InductionDockBlockEntity(pos, state) : null;
  }

  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return RenderShape.MODEL;
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return DockShapes.of(state.getValue(FACING), isLower(state));
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level level, BlockState state, BlockEntityType<T> type) {
    return level.isClientSide() || !isLower(state)
        ? null
        : createTickerHelper(
            type, CubitBlockEntities.INDUCTION_DOCK.get(), InductionDockBlockEntity::serverTick);
  }

  /** Without the space above, nothing is placed, and the item stays in the hand. */
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockPos above = context.getClickedPos().above();
    Level level = context.getLevel();
    if (above.getY() >= level.getMaxBuildHeight()
        || !level.getBlockState(above).canBeReplaced(context)) {
      return null;
    }
    return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
  }

  @Override
  public void setPlacedBy(
      Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), UPDATE_ALL);
    if (placer instanceof Player player
        && level.getBlockEntity(pos) instanceof InductionDockBlockEntity dock) {
      dock.setOwner(player);
    }
  }

  /** Each part only exists with the other, as in a door. */
  @Override
  protected BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighbor,
      LevelAccessor level,
      BlockPos pos,
      BlockPos neighborPos) {
    Direction other = isLower(state) ? Direction.UP : Direction.DOWN;
    if (direction == other
        && !(neighbor.is(this) && neighbor.getValue(HALF) != state.getValue(HALF))) {
      return Blocks.AIR.defaultBlockState();
    }
    return state;
  }

  @Override
  protected ItemInteractionResult useItemOn(
      ItemStack stack,
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit) {
    InductionDockBlockEntity dock = dockAt(level, pos, state);
    if (stack.getItem() instanceof DockableRobotItem) {
      return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
    }
    if (level.isClientSide() || dock == null) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    if (dock.isOwner(player) && dock.insert(stack)) {
      return ItemInteractionResult.CONSUME;
    }
    return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  }

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    InductionDockBlockEntity dock = dockAt(level, pos, state);
    if (dock == null || !dock.isOwner(player)) {
      return InteractionResult.PASS;
    }
    if (player.isShiftKeyDown()) {
      player.openMenu(state.getMenuProvider(level, pos));
    } else if (level instanceof ServerLevel serverLevel) {
      callOrRelease(serverLevel, dock, player);
    }
    return InteractionResult.CONSUME;
  }

  private static void callOrRelease(
      ServerLevel level, InductionDockBlockEntity dock, Player player) {
    Optional<UUID> occupant = dock.occupant();
    if (occupant.isPresent()) {
      if (level.getEntity(occupant.get()) instanceof DockableRobot robot) {
        robot.releaseFromDock();
      }
      return;
    }
    DockFinder.nearestRobot(level, dock.getBlockPos(), player.getUUID())
        .ifPresent(robot -> robot.callToDock(dock.getBlockPos()));
  }

  @Override
  protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
    InductionDockBlockEntity dock = dockAt(level, pos, state);
    return dock == null
        ? null
        : new SimpleMenuProvider(
            (containerId, inventory, player) ->
                InductionDockMenu.onServer(containerId, inventory, dock),
            Component.translatable(getDescriptionId()));
  }

  private static boolean canBreak(
      BlockGetter level, BlockPos pos, BlockState state, Player player) {
    InductionDockBlockEntity dock = dockAt(level, pos, state);
    return DockRemoval.allows(
        player.isCreative(),
        player.hasPermissions(OPERATOR_LEVEL),
        dock == null ? Optional.empty() : dock.owner(),
        player.getUUID());
  }

  @Override
  protected float getDestroyProgress(
      BlockState state, Player player, BlockGetter level, BlockPos pos) {
    return canBreak(level, pos, state, player)
        ? super.getDestroyProgress(state, player, level, pos)
        : CANNOT_BREAK;
  }

  /** The creative mode ignores the destroy progress, so the rule is checked here as well. */
  @Override
  public boolean onDestroyedByPlayer(
      BlockState state,
      Level level,
      BlockPos pos,
      Player player,
      boolean willHarvest,
      FluidState fluid) {
    return canBreak(level, pos, state, player)
        && super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
  }

  /** A player in creative gets no drops, and the energy of the dock must not be lost. */
  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    InductionDockBlockEntity dock = dockAt(level, pos, state);
    if (!level.isClientSide()
        && player.isCreative()
        && dock != null
        && canBreak(level, pos, state, player)) {
      ItemStack item = new ItemStack(this);
      item.applyComponents(dock.collectComponents());
      popResource(level, dock.getBlockPos(), item);
      if (!isLower(state)) {
        level.setBlock(pos.below(), Blocks.AIR.defaultBlockState(), REMOVE_WITHOUT_DROPS);
      }
    }
    return super.playerWillDestroy(level, pos, state, player);
  }

  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    if (!state.is(newState.getBlock())
        && isLower(state)
        && level.getBlockEntity(pos) instanceof InductionDockBlockEntity dock) {
      Containers.dropContents(level, pos, dock.slot());
    }
    super.onRemove(state, level, pos, newState, movedByPiston);
  }

  @Override
  public boolean canEntityDestroy(
      BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
    return false;
  }

  /** Pistons cannot move a dock, and the block properties make it resist explosions. */
  @Override
  public PushReaction getPistonPushReaction(BlockState state) {
    return PushReaction.BLOCK;
  }
}
