package io.github.zenndg.cubit.client;

import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.block.InductionDockBlock;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.TierLook;
import io.github.zenndg.cubit.registry.CubitBlocks;
import io.github.zenndg.cubit.registry.CubitDataComponents;
import io.github.zenndg.cubit.registry.CubitItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

/**
 * The band of the dock in the color of its tier, on the block and on the item, and the netherite
 * body of the Ultimate on the item (answers 1379, 1380 and 1382).
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class DockTierLook {
  private static final int BAND_LAYER = 0;
  private static final int NO_TINT = -1;
  private static final float ULTIMATE = 1f;
  private static final float OTHER = 0f;

  private DockTierLook() {}

  @SubscribeEvent
  public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
    event.register(
        (state, level, pos, layer) ->
            layer == BAND_LAYER && state.hasProperty(InductionDockBlock.TIER)
                ? band(state.getValue(InductionDockBlock.TIER).tier())
                : NO_TINT,
        CubitBlocks.INDUCTION_DOCK.get());
  }

  @SubscribeEvent
  public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
    event.register(
        (stack, layer) -> layer == BAND_LAYER ? band(tierOf(stack)) : NO_TINT,
        CubitItems.INDUCTION_DOCK.get());
  }

  @SubscribeEvent
  public static void setup(FMLClientSetupEvent event) {
    event.enqueueWork(() ->
        ItemProperties.register(
            CubitItems.INDUCTION_DOCK.get(),
            InductionDockBlock.ULTIMATE_PROPERTY,
            (stack, level, entity, seed) ->
                tierOf(stack) == RobotTier.ULTIMATE ? ULTIMATE : OTHER));
  }

  private static int band(RobotTier tier) {
    return FastColor.ARGB32.opaque(TierLook.color(tier));
  }

  private static RobotTier tierOf(ItemStack stack) {
    return stack.getOrDefault(CubitDataComponents.DOCK_TIER.get(), RobotTier.BASE);
  }
}
