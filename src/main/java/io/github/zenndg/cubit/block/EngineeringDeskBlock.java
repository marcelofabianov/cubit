package io.github.zenndg.cubit.block;

import com.mojang.serialization.MapCodec;
import io.github.zenndg.cubit.domain.dock.DockRemoval;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.ModuleItem;
import io.github.zenndg.cubit.menu.EngineeringDeskMenu;
import io.github.zenndg.cubit.registry.CubitBlockEntities;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * The Engineering Desk, two blocks wide: the left half, the clicked one, holds everything, and the
 * right half goes to the right of the player (answer 1456). Anyone uses it, and only the owner
 * raises its tier and breaks it (answers 1431 and 1445).
 */
public final class EngineeringDeskBlock extends BaseEntityBlock {
  private static final String PART_NAME = "part";
  private static final String TIER_NAME = "tier";
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<DeskPart> PART = EnumProperty.create(PART_NAME, DeskPart.class);
  public static final EnumProperty<DockTierProperty> TIER =
      EnumProperty.create(TIER_NAME, DockTierProperty.class);

  private static final MapCodec<EngineeringDeskBlock> CODEC =
      simpleCodec(EngineeringDeskBlock::new);
  private static final double TOP_PIXELS = 14;
  private static final double FULL_PIXELS = 16;
  private static final VoxelShape SHAPE = Block.box(0, 0, 0, FULL_PIXELS, TOP_PIXELS, FULL_PIXELS);
  private static final float CANNOT_BREAK = 0f;
  private static final int OPERATOR_LEVEL = 2;

  public EngineeringDeskBlock(Properties properties) {
    super(properties);
    registerDefaultState(
        stateDefinition
            .any()
            .setValue(FACING, Direction.NORTH)
            .setValue(PART, DeskPart.LEFT)
            .setValue(TIER, DockTierProperty.BASE));
  }

  @Override
  protected MapCodec<? extends BaseEntityBlock> codec() {
    return CODEC;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING, PART, TIER);
  }

  /** Seen from the front, the right half sits to the right of the left one. */
  public static Direction towardRight(BlockState state) {
    return state.getValue(FACING).getCounterClockWise();
  }

  public static BlockPos leftOf(BlockPos pos, BlockState state) {
    return state.getValue(PART) == DeskPart.LEFT ? pos : pos.relative(towardRight(state), -1);
  }

  private static BlockPos otherHalf(BlockPos pos, BlockState state) {
    Direction right = towardRight(state);
    return state.getValue(PART) == DeskPart.LEFT ? pos.relative(right) : pos.relative(right, -1);
  }

  /** The block entity lives in the left half, and the right half answers for it. */
  public static EngineeringDeskBlockEntity deskAt(
      BlockGetter level, BlockPos pos, BlockState state) {
    return level.getBlockEntity(leftOf(pos, state)) instanceof EngineeringDeskBlockEntity desk
        ? desk
        : null;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return state.getValue(PART) == DeskPart.LEFT
        ? new EngineeringDeskBlockEntity(pos, state)
        : null;
  }

  /** The left half draws the whole model, and the right one only keeps the space. */
  @Override
  protected RenderShape getRenderShape(BlockState state) {
    return state.getValue(PART) == DeskPart.LEFT
        ? RenderShape.ENTITYBLOCK_ANIMATED
        : RenderShape.INVISIBLE;
  }

  @Override
  protected VoxelShape getShape(
      BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return SHAPE;
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
      Level level, BlockState state, BlockEntityType<T> type) {
    return level.isClientSide() || state.getValue(PART) != DeskPart.LEFT
        ? null
        : createTickerHelper(
            type,
            CubitBlockEntities.ENGINEERING_DESK.get(),
            EngineeringDeskBlockEntity::serverTick);
  }

  /** Without room for both halves, nothing is placed, and the item stays in the hand. */
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockState state =
        defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    BlockPos right = context.getClickedPos().relative(towardRight(state));
    Level level = context.getLevel();
    boolean free =
        level.getWorldBorder().isWithinBounds(right)
            && level.getBlockState(right).canBeReplaced(context);
    return free ? state : null;
  }

  @Override
  public void setPlacedBy(
      Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    RobotTier tier = stack.getOrDefault(CubitDataComponents.DESK_TIER.get(), RobotTier.BASE);
    BlockState placed = state.setValue(TIER, DockTierProperty.of(tier));
    level.setBlock(pos, placed, UPDATE_ALL);
    level.setBlock(
        pos.relative(towardRight(state)), placed.setValue(PART, DeskPart.RIGHT), UPDATE_ALL);
    if (placer instanceof Player player
        && level.getBlockEntity(pos) instanceof EngineeringDeskBlockEntity desk) {
      desk.setOwner(player);
      DeskFirstNotice.show(player);
    }
  }

  /** Each half only exists with the other, as in a bed. */
  @Override
  protected BlockState updateShape(
      BlockState state,
      Direction direction,
      BlockState neighbor,
      LevelAccessor level,
      BlockPos pos,
      BlockPos neighborPos) {
    if (neighborPos.equals(otherHalf(pos, state))
        && !(neighbor.is(this) && neighbor.getValue(PART) != state.getValue(PART))) {
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
    if (!(stack.getItem() instanceof ModuleItem)) {
      return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
    EngineeringDeskBlockEntity desk = deskAt(level, pos, state);
    if (!level.isClientSide() && desk != null && desk.isOwner(player)) {
      DeskUpgrade.apply(desk, player, stack);
    }
    return ItemInteractionResult.sidedSuccess(level.isClientSide());
  }

  @Override
  protected InteractionResult useWithoutItem(
      BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    EngineeringDeskBlockEntity desk = deskAt(level, pos, state);
    if (desk == null) {
      return InteractionResult.PASS;
    }
    player.openMenu(
        state.getMenuProvider(level, pos), data -> EngineeringDeskMenu.writeOpening(data, desk));
    return InteractionResult.CONSUME;
  }

  @Override
  protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
    EngineeringDeskBlockEntity desk = deskAt(level, pos, state);
    return desk == null
        ? null
        : new SimpleMenuProvider(
            (containerId, inventory, player) ->
                EngineeringDeskMenu.onServer(containerId, inventory, desk),
            Component.translatable(getDescriptionId()));
  }

  /** A redstone signal on either half pauses the order (answer 1448). */
  static boolean hasSignal(Level level, BlockPos pos, BlockState state) {
    return level.hasNeighborSignal(pos) || level.hasNeighborSignal(otherHalf(pos, state));
  }

  /** Both halves show the tier, which picks the band and the body (answer 1439). */
  static void showTier(Level level, BlockPos left, BlockState state, RobotTier tier) {
    DockTierProperty property = DockTierProperty.of(tier);
    for (BlockPos pos : new BlockPos[] {left, otherHalf(left, state)}) {
      BlockState current = level.getBlockState(pos);
      if (current.getBlock() instanceof EngineeringDeskBlock
          && current.getValue(TIER) != property) {
        level.setBlock(pos, current.setValue(TIER, property), UPDATE_ALL);
      }
    }
  }

  @Override
  protected boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  /** What is left of the order, from 15 to 0 (answer 1453). */
  @Override
  protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
    EngineeringDeskBlockEntity desk = deskAt(level, pos, state);
    return desk == null ? 0 : desk.signal();
  }

  private static boolean canBreak(
      BlockGetter level, BlockPos pos, BlockState state, Player player) {
    EngineeringDeskBlockEntity desk = deskAt(level, pos, state);
    return DockRemoval.allows(
        player.isCreative(),
        player.hasPermissions(OPERATOR_LEVEL),
        desk == null ? Optional.empty() : desk.owner(),
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

  /**
   * Only the left half drops the desk, with what it keeps. Breaking the right half breaks the left
   * one first, and a player in creative still gets the item, so nothing is lost (answer 1432).
   */
  @Override
  public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
    EngineeringDeskBlockEntity desk = deskAt(level, pos, state);
    if (!level.isClientSide() && desk != null && canBreak(level, pos, state, player)) {
      if (player.isCreative()) {
        ItemStack item = new ItemStack(this);
        item.applyComponents(desk.collectComponents());
        popResource(level, desk.getBlockPos(), item);
      } else if (state.getValue(PART) == DeskPart.RIGHT) {
        level.destroyBlock(desk.getBlockPos(), true, player);
      }
    }
    return super.playerWillDestroy(level, pos, state, player);
  }

  /** The redstone of the energy slot drops, as in the dock. The rest stays in the item. */
  @Override
  protected void onRemove(
      BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
    if (!state.is(newState.getBlock())
        && level.getBlockEntity(pos) instanceof EngineeringDeskBlockEntity desk) {
      Containers.dropContents(level, pos, desk.energySlot());
    }
    super.onRemove(state, level, pos, newState, movedByPiston);
  }

  @Override
  public boolean canEntityDestroy(
      BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
    return false;
  }

  @Override
  public PushReaction getPistonPushReaction(BlockState state) {
    return PushReaction.BLOCK;
  }
}
