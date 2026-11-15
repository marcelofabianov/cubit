package io.github.marcelofabianov.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import io.github.marcelofabianov.cubit.entity.RobotJobLook;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

/**
 * The fishing rod of the game, only for the looks: in the backpack, and in the right claw while the
 * Fisher fishes at its stand (answers 1196 and 1199). The catch never uses it.
 */
final class RobotRodLayer extends BlockAndItemGeoLayer<RobotEntity> {
  private static final String BACKPACK_BONE = "fisher_backpack_rod";
  private static final String CLAW_BONE = "right_claw_item";
  private static final ItemStack ROD = new ItemStack(Items.FISHING_ROD);

  /** The rod of the game is a whole block tall, and the Cubit is small. */
  private static final float SCALE = 0.6F;

  private static final float CLAW_TURN_DEGREES = -90F;

  RobotRodLayer(GeoRenderer<RobotEntity> renderer) {
    super(renderer);
  }

  @Override
  protected ItemStack getStackForBone(GeoBone bone, RobotEntity robot) {
    if (!RobotJobLook.fisher(robot)) {
      return null;
    }
    boolean fishing = RobotJobLook.fishing(robot);
    if (bone.getName().equals(CLAW_BONE) && fishing
        || bone.getName().equals(BACKPACK_BONE) && !fishing) {
      return ROD;
    }
    return null;
  }

  @Override
  protected ItemDisplayContext getTransformTypeForStack(
      GeoBone bone, ItemStack stack, RobotEntity robot) {
    return bone.getName().equals(CLAW_BONE)
        ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
        : ItemDisplayContext.FIXED;
  }

  @Override
  protected void renderStackForBone(
      PoseStack poseStack,
      GeoBone bone,
      ItemStack stack,
      RobotEntity robot,
      MultiBufferSource buffers,
      float partialTick,
      int packedLight,
      int packedOverlay) {
    poseStack.scale(SCALE, SCALE, SCALE);
    if (bone.getName().equals(CLAW_BONE)) {
      poseStack.mulPose(Axis.XP.rotationDegrees(CLAW_TURN_DEGREES));
    }
    super.renderStackForBone(
        poseStack, bone, stack, robot, buffers, partialTick, packedLight, packedOverlay);
  }
}
