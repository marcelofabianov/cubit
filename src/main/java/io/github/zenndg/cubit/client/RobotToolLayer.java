package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.entity.RobotJobLook;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

/**
 * The tool of the job, the one of the game and only for the looks: the fishing rod of the Fisher,
 * the iron hoe of the Farmer or the book and quill of the Organizer, in the backpack, and in the
 * right claw while the Cubit works (answers 1196, 1199, 1216, 1229 and 1308), and the stick of the
 * game in the straw hat of the Farmer (answer 1242). The work never uses them.
 */
final class RobotToolLayer extends BlockAndItemGeoLayer<RobotEntity> {
  private static final String BACKPACK_BONE = "job_backpack_tool";
  private static final String CLAW_BONE = "right_claw_item";
  private static final String SPRIG_BONE = "farmer_hat_sprig";
  private static final ItemStack ROD = new ItemStack(Items.FISHING_ROD);
  private static final ItemStack HOE = new ItemStack(Items.IRON_HOE);
  private static final ItemStack STICK = new ItemStack(Items.STICK);
  private static final ItemStack BOOK = new ItemStack(Items.WRITABLE_BOOK);

  /** The rod of the game is a whole block tall, and the Cubit is small. */
  private static final float SCALE = 0.6F;

  private static final float CLAW_TURN_DEGREES = -90F;

  /** The stick in the straw hat is a small sprig, smaller than a tool. */
  private static final float SPRIG_SCALE = 0.35F;

  RobotToolLayer(GeoRenderer<RobotEntity> renderer) {
    super(renderer);
  }

  @Override
  protected ItemStack getStackForBone(GeoBone bone, RobotEntity robot) {
    if (bone.getName().equals(SPRIG_BONE)) {
      return RobotJobLook.farmer(robot) ? STICK : null;
    }
    ItemStack tool = toolOf(robot);
    if (tool == null) {
      return null;
    }
    boolean inClaw = RobotJobLook.toolInClaw(robot);
    if (bone.getName().equals(CLAW_BONE) && inClaw
        || bone.getName().equals(BACKPACK_BONE) && !inClaw) {
      return tool;
    }
    return null;
  }

  private static ItemStack toolOf(RobotEntity robot) {
    if (RobotJobLook.fisher(robot)) {
      return ROD;
    }
    if (RobotJobLook.organizer(robot)) {
      return BOOK;
    }
    return RobotJobLook.farmer(robot) ? HOE : null;
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
    float scale = bone.getName().equals(SPRIG_BONE) ? SPRIG_SCALE : SCALE;
    poseStack.scale(scale, scale, scale);
    if (bone.getName().equals(CLAW_BONE)) {
      poseStack.mulPose(Axis.XP.rotationDegrees(CLAW_TURN_DEGREES));
    }
    super.renderStackForBone(
        poseStack, bone, stack, robot, buffers, partialTick, packedLight, packedOverlay);
  }
}
