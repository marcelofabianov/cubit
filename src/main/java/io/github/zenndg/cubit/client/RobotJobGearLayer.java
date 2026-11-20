package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.RobotJob;
import io.github.zenndg.cubit.domain.robot.RobotType;
import io.github.zenndg.cubit.entity.RobotEntity;
import io.github.zenndg.cubit.entity.RobotJobLook;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

/**
 * The hat and the backpack of the job, always with its card: the bucket hat of the Fisher, the
 * straw hat of the Farmer and the warehouse cap of the Organizer, all with the same backpack
 * (answers 1196, 1197, 1200, 1216, 1229 and 1307). The book and quill of the Organizer is part of
 * the model in the backpack, because the flat item of the game looked wrong there (answer 1312).
 * They stay hidden in the body and in the other layers, and only this layer shows them, with a
 * picture of each job that is transparent everywhere but on them. The hat covers the solar panel,
 * which still charges (answer 1201).
 */
final class RobotJobGearLayer extends GeoRenderLayer<RobotEntity> {
  private static final String TEXTURE_PATH = "textures/entity/%s_%s.png";
  private static final String BACKPACK = "job_backpack";
  private static final String FISHER_HAT = "fisher_hat";
  private static final String FARMER_HAT = "farmer_hat";
  private static final String ORGANIZER_CAP = "organizer_cap";
  private static final String ORGANIZER_BOOK = "organizer_book";
  private static final String ORGANIZER_QUILL = "organizer_quill";

  /**
   * The book and quill of the Organizer, part of the model, which leave when it goes to the claw.
   */
  private static final List<String> ORGANIZER_TOOL = List.of(ORGANIZER_BOOK, ORGANIZER_QUILL);

  private static final Map<RobotJob, List<String>> BONES =
      Map.of(
          RobotJob.FISHER, List.of(FISHER_HAT, BACKPACK),
          RobotJob.FARMER, List.of(FARMER_HAT, BACKPACK),
          RobotJob.ORGANIZER, List.of(ORGANIZER_CAP, BACKPACK, ORGANIZER_BOOK, ORGANIZER_QUILL));
  private static final int NO_TINT = -1;

  private final Map<RobotJob, ResourceLocation> textures = new EnumMap<>(RobotJob.class);

  RobotJobGearLayer(GeoRenderer<RobotEntity> renderer, RobotType robotType) {
    super(renderer);
    for (RobotJob job : BONES.keySet()) {
      textures.put(
          job,
          ResourceLocation.fromNamespaceAndPath(
              CubitMod.MOD_ID, TEXTURE_PATH.formatted(robotType.id(), job.key())));
    }
  }

  /** Called with each frame of the model, before the body and every layer draw it. */
  static void hideIn(Function<String, GeoBone> bones) {
    BONES.values().forEach(names -> setHidden(bones, names, true));
  }

  @Override
  public void render(
      PoseStack poseStack,
      RobotEntity robot,
      BakedGeoModel model,
      RenderType bodyType,
      MultiBufferSource buffers,
      VertexConsumer bodyBuffer,
      float partialTick,
      int packedLight,
      int packedOverlay) {
    RobotJob job = RobotJobLook.job(robot).filter(BONES::containsKey).orElse(null);
    if (job == null) {
      return;
    }
    Function<String, GeoBone> bones = name -> model.getBone(name).orElse(null);
    setHidden(bones, BONES.get(job), false);
    if (job == RobotJob.ORGANIZER && RobotJobLook.toolInClaw(robot)) {
      setHidden(bones, ORGANIZER_TOOL, true);
    }
    RenderType type = RenderType.entityCutoutNoCull(textures.get(job));
    getRenderer()
        .reRender(
            model,
            poseStack,
            buffers,
            robot,
            type,
            buffers.getBuffer(type),
            partialTick,
            packedLight,
            packedOverlay,
            NO_TINT);
    setHidden(bones, BONES.get(job), true);
  }

  private static void setHidden(
      Function<String, GeoBone> bones, List<String> names, boolean hidden) {
    for (String name : names) {
      GeoBone bone = bones.apply(name);
      if (bone != null) {
        bone.setHidden(hidden);
      }
    }
  }
}
