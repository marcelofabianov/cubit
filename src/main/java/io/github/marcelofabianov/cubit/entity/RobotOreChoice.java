package io.github.marcelofabianov.cubit.entity;

import io.github.marcelofabianov.cubit.domain.robot.OreFilter;
import io.github.marcelofabianov.cubit.domain.robot.OreKind;
import io.github.marcelofabianov.cubit.network.CubitNetwork;
import io.github.marcelofabianov.cubit.registry.CubitSounds;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * The owner chooses what the scanner looks for by clicking with an ore in hand. The item stays in
 * the hand, and the answer has no interval, because it replies to a click.
 */
final class RobotOreChoice {
  private RobotOreChoice() {}

  static InteractionResult apply(RobotEntity robot, Player player, ItemStack held) {
    List<OreKind> kinds =
        OreKind.kindsOf(held.getTags().map(tag -> tag.location().toString()).toList());
    if (kinds.isEmpty()) {
      return InteractionResult.PASS;
    }
    OreKind kind = kinds.getFirst();
    RobotScanners scanners = robot.scanners();
    OreFilter next = scanners.oreFilter().toggled(kind, robot.spec().scanners().oreFilterSlots());
    scanners.setOreFilter(next);
    robot.playSound(CubitSounds.EXPRESSION_BEEP.get());
    if (player instanceof ServerPlayer serverPlayer) {
      CubitNetwork.sendNotice(serverPlayer, answer(kind, next));
    }
    return InteractionResult.CONSUME;
  }

  private static Component answer(OreKind kind, OreFilter filter) {
    Component text =
        filter.contains(kind)
            ? Component.translatable(RobotScanners.LOOKING_KEY, OreNames.of(kind))
            : Component.translatable(RobotScanners.CLEARED_KEY);
    return Component.translatable(RobotNotices.FORMAT_KEY, text);
  }
}
