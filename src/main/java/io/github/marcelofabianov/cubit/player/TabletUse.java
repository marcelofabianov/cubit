package io.github.marcelofabianov.cubit.player;

import io.github.marcelofabianov.cubit.block.InductionDockBlockEntity;
import io.github.marcelofabianov.cubit.domain.ownership.Ownership;
import io.github.marcelofabianov.cubit.domain.robot.DefenseMode;
import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.domain.robot.TabletApps;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotTablet;
import io.github.marcelofabianov.cubit.item.TabletItem;
import io.github.marcelofabianov.cubit.menu.TabletKeys;
import io.github.marcelofabianov.cubit.registry.CubitAttachments;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

/**
 * What the right click of the Tablet does by its active app, on the server, and the switch of the
 * app with Shift and the wheel (answers 1006, 1007 and 1043 to 1047). An app that stopped making
 * sense goes back to Open before the click.
 */
public final class TabletUse {
  private TabletUse() {}

  /** Where the click landed: nothing, a block or an entity. */
  public sealed interface Click permits Air, Block, Living {}

  /** A click in the air, or on a block that is not a Dock of the player. */
  public record Air() implements Click {}

  /** A click on a block, with the Dock under it when there is one. */
  public record Block(BlockPos pos, Optional<InductionDockBlockEntity> dock) implements Click {}

  /** A click on an entity. */
  public record Living(Entity entity) implements Click {}

  public static void click(ServerPlayer player, ItemStack tablet, Click click) {
    Optional<RobotEntity> chosen = chosen(player);
    TabletApp app = effectiveApp(player, tablet, chosen);
    switch (app) {
      case OPEN -> open(player, click);
      case TARGET -> TabletTargetApp.click(player, chosen.orElseThrow(), click);
      case DOCK -> TabletDockApp.click(player, chosen, click);
      case AREA -> TabletAreaApp.click(player, click);
    }
  }

  /** The app of the item, or Open with a notice when the active one stopped making sense. */
  private static TabletApp effectiveApp(
      ServerPlayer player, ItemStack tablet, Optional<RobotEntity> chosen) {
    TabletApp active = TabletItem.appOf(tablet);
    TabletApp effective = appsFor(chosen).effective(active);
    if (effective != active) {
      TabletItem.setApp(tablet, effective);
      player.displayClientMessage(Component.translatable(TabletKeys.BACK_TO_OPEN), true);
    }
    return effective;
  }

  private static void open(ServerPlayer player, Click click) {
    if (click instanceof Living living
        && living.entity() instanceof RobotEntity robot
        && Ownership.owns(robot.ownerId(), player.getUUID())) {
      TabletSelection.chooseAndOpen(player, robot.getUUID());
    } else {
      TabletSelection.open(player);
    }
  }

  /** Shift and the wheel go to the next app that makes sense, and the action bar shows it. */
  public static void switchApp(ServerPlayer player, int direction) {
    for (InteractionHand hand : InteractionHand.values()) {
      ItemStack held = player.getItemInHand(hand);
      if (held.is(CubitItems.TABLET.get())) {
        TabletItem.setApp(held, appsFor(chosen(player)).next(TabletItem.appOf(held), direction));
        player.displayClientMessage(held.getHoverName(), true);
        TabletAreaApp.forget(player);
        return;
      }
    }
  }

  private static TabletApps appsFor(Optional<RobotEntity> chosen) {
    return new TabletApps(
        chosen.filter(robot -> robot.defense().mode() == DefenseMode.FOCUS).isPresent());
  }

  /** The chosen Cubit, only while it is loaded. */
  static Optional<RobotEntity> chosen(ServerPlayer player) {
    return player
        .getData(CubitAttachments.CHOSEN_ROBOT)
        .robot()
        .flatMap(robot -> RobotTablet.find(player, robot));
  }
}
