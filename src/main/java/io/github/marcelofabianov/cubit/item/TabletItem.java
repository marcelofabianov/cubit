package io.github.marcelofabianov.cubit.item;

import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.player.TabletUse;
import io.github.marcelofabianov.cubit.registry.CubitDataComponents;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

/**
 * The Cubit Tablet, which replaces the Cubit Remote (answers 998 to 1026). Its right click does
 * what its active app does, on the server.
 */
public final class TabletItem extends Item {
  private static final String APP_PREFIX = "gui.cubit.tablet.app.";

  public TabletItem(Properties properties) {
    super(properties);
  }

  public static TabletApp appOf(ItemStack stack) {
    return TabletApp.fromKeyOrOpen(
        stack.getOrDefault(CubitDataComponents.TABLET_APP.get(), TabletApp.OPEN.key()));
  }

  public static void setApp(ItemStack stack, TabletApp app) {
    stack.set(CubitDataComponents.TABLET_APP.get(), app.key());
  }

  public static String appKey(TabletApp app) {
    return APP_PREFIX + app.key();
  }

  /** The active app shows in the name, as `Cubit Tablet · Open` (answer 1006). */
  @Override
  public Component getName(ItemStack stack) {
    return Component.translatable(
        TabletKeys.WITH_APP, super.getName(stack), Component.translatable(appKey(appOf(stack))));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
    if (player instanceof ServerPlayer server) {
      TabletUse.click(server, player.getItemInHand(hand), new TabletUse.Air());
    }
    return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
  }

  /** Before the block, so a click on a chest or a door never uses the block (answer 1007). */
  @Override
  public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
    if (context.getPlayer() instanceof ServerPlayer server) {
      Optional<InductionDockBlockEntity> dock =
          context.getLevel().getBlockEntity(context.getClickedPos())
                  instanceof InductionDockBlockEntity found
              ? Optional.of(found)
              : Optional.empty();
      TabletUse.click(server, stack, new TabletUse.Block(context.getClickedPos(), dock));
    }
    return InteractionResult.SUCCESS;
  }
}
