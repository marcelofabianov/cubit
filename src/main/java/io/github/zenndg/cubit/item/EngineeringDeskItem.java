package io.github.zenndg.cubit.item;

import io.github.zenndg.cubit.registry.CubitDataComponents;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * The item of the Engineering Desk, which keeps its tier, energy, inventories and order (answer
 * 1432). With items inside it never goes into another container (answer 1481).
 */
public final class EngineeringDeskItem extends BlockItem implements GeoItem {
  public static final String TIER_KEY = "tooltip.cubit.engineering_desk.tier";
  public static final String ENERGY_KEY = "tooltip.cubit.engineering_desk.energy";
  public static final String ORDER_KEY = "tooltip.cubit.engineering_desk.order";
  public static final String ITEMS_KEY = "tooltip.cubit.engineering_desk.items";

  private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

  public EngineeringDeskItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    // The item stays still: the animations belong to the placed desk (answer 1494).
  }

  @Override
  public AnimatableInstanceCache getAnimatableInstanceCache() {
    return animationCache;
  }

  /** Empty, it goes anywhere. With items inside, it never nests (answer 1481). */
  public static boolean holdsItems(ItemStack stack) {
    return count(stack.get(CubitDataComponents.DESK_INPUT.get())) > 0
        || count(stack.get(CubitDataComponents.DESK_OUTPUT.get())) > 0;
  }

  @Override
  public boolean canFitInsideContainerItems(ItemStack stack) {
    return !holdsItems(stack);
  }

  @Override
  public void appendHoverText(
      ItemStack stack, TooltipContext context, List<Component> lines, TooltipFlag flag) {
    EngineeringDeskTooltip.append(stack, lines);
  }

  static int count(ItemContainerContents contents) {
    if (contents == null) {
      return 0;
    }
    int total = 0;
    for (ItemStack stack : contents.nonEmptyItems()) {
      total += stack.getCount();
    }
    return total;
  }
}
