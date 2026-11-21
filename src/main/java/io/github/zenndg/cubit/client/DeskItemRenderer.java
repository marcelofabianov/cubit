package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.zenndg.cubit.domain.tier.RobotTier;
import io.github.zenndg.cubit.item.EngineeringDeskItem;
import io.github.zenndg.cubit.item.TierLook;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoItemRenderer;

/** The item of the desk, drawn by the same model, with the tier kept in the item (answer 1476). */
public final class DeskItemRenderer extends GeoItemRenderer<EngineeringDeskItem> {
  /** The desk is two blocks wide, so the item shows it at half size, centered on its block. */
  private static final float FIT = 0.5f;

  private static final float HALF = 0.5f;
  private static final float LIFT = 0.1f;

  public DeskItemRenderer() {
    super(new ItemModel());
  }

  private RobotTier tier() {
    return currentItemStack == null ? RobotTier.BASE : TierLook.deskTierOf(currentItemStack);
  }

  /** The texture follows the tier of the stack being drawn, read on the render thread. */
  @Override
  public void renderByItem(
      ItemStack stack,
      ItemDisplayContext context,
      PoseStack poseStack,
      MultiBufferSource bufferSource,
      int packedLight,
      int packedOverlay) {
    ItemModel.drawn = TierLook.deskTierOf(stack);
    poseStack.pushPose();
    poseStack.translate(HALF, 0, HALF);
    poseStack.scale(FIT, FIT, FIT);
    poseStack.translate(0, LIFT, -HALF);
    super.renderByItem(stack, context, poseStack, bufferSource, packedLight, packedOverlay);
    poseStack.popPose();
  }

  @Override
  public void renderRecursively(
      PoseStack poseStack,
      EngineeringDeskItem item,
      GeoBone bone,
      RenderType renderType,
      MultiBufferSource bufferSource,
      VertexConsumer buffer,
      boolean isReRender,
      float partialTick,
      int packedLight,
      int packedOverlay,
      int colour) {
    super.renderRecursively(
        poseStack,
        item,
        bone,
        renderType,
        bufferSource,
        buffer,
        isReRender,
        partialTick,
        packedLight,
        packedOverlay,
        DeskBand.colour(bone, tier(), colour));
  }

  private static final class ItemModel extends DeskGeoModel<EngineeringDeskItem> {
    private static RobotTier drawn = RobotTier.BASE;

    @Override
    RobotTier tierOf(EngineeringDeskItem item) {
      return drawn;
    }
  }
}
