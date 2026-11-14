package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.domain.robot.TabletApp;
import io.github.marcelofabianov.cubit.item.TabletItem;
import io.github.marcelofabianov.cubit.network.AreaMarkPayload;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

/**
 * What the Area app shows on the client: the box from the first corner to the block in sight while
 * marking, green when it fits the tier and red when not, and then the area of the chosen Cubit
 * (answer 1056). Only with the Tablet in hand and the Area app active.
 */
final class TabletAreaMarks {
  private static AreaMarkPayload current = AreaMarkPayload.NOTHING;

  private TabletAreaMarks() {}

  static void show(AreaMarkPayload payload) {
    current = payload;
  }

  /** The box being marked, or the area of the chosen Cubit. */
  record Shown(AABB box, boolean marking, boolean fits, Size size) {}

  /** The size of the box, as the action bar says it. */
  record Size(int width, int height, int depth) {}

  static Optional<Shown> shown() {
    if (!holdsAreaApp()) {
      return Optional.empty();
    }
    if (current.first().isPresent()) {
      BlockPos first = current.first().get();
      return Optional.of(marking(first, aimed().orElse(first)));
    }
    return current
        .area()
        .map(area ->
            new Shown(box(area.min(), area.max()), false, true, sizeOf(area.min(), area.max())));
  }

  private static Shown marking(BlockPos first, BlockPos second) {
    BlockPos min = BlockPos.min(first, second);
    BlockPos max = BlockPos.max(first, second);
    Size size = sizeOf(min, max);
    AreaMarkPayload.Limits limits = current.limits();
    boolean fits =
        limits.size() > 0
            && size.width() <= limits.size()
            && size.depth() <= limits.size()
            && size.height() <= limits.height();
    return new Shown(box(min, max), true, fits, size);
  }

  private static Size sizeOf(BlockPos min, BlockPos max) {
    return new Size(
        max.getX() - min.getX() + 1, max.getY() - min.getY() + 1, max.getZ() - min.getZ() + 1);
  }

  private static AABB box(BlockPos min, BlockPos max) {
    return new AABB(
        min.getX(), min.getY(), min.getZ(), max.getX() + 1, max.getY() + 1, max.getZ() + 1);
  }

  private static Optional<BlockPos> aimed() {
    HitResult hit = Minecraft.getInstance().hitResult;
    return hit instanceof BlockHitResult block && hit.getType() == HitResult.Type.BLOCK
        ? Optional.of(block.getBlockPos())
        : Optional.empty();
  }

  private static boolean holdsAreaApp() {
    LocalPlayer player = Minecraft.getInstance().player;
    if (player == null) {
      return false;
    }
    for (InteractionHand hand : InteractionHand.values()) {
      ItemStack held = player.getItemInHand(hand);
      if (held.is(CubitItems.TABLET.get()) && TabletItem.appOf(held) == TabletApp.AREA) {
        return true;
      }
    }
    return false;
  }
}
