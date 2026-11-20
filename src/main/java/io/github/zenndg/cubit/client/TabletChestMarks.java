package io.github.zenndg.cubit.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.zenndg.cubit.CubitMod;
import io.github.zenndg.cubit.domain.robot.TabletApp;
import io.github.zenndg.cubit.item.TabletAppLook;
import io.github.zenndg.cubit.menu.TabletKeys;
import io.github.zenndg.cubit.network.OrganizerMarksPayload;
import io.github.zenndg.cubit.network.UnloadChestsPayload;
import io.github.zenndg.cubit.registry.CubitEntities;
import io.github.zenndg.cubit.registry.CubitItems;
import java.util.List;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

/**
 * The arrows over the marked chests of the Cubits of the player, in the color of the Unload app,
 * with the name of the Cubit and the slots of the chest under them, and over the marks of its
 * Organizers, in the color of the Mark app, with the name and the number of the mark, only with the
 * Tablet in hand and through blocks (answers 1086, 1090 to 1092 and 1297). The server sends them
 * once a second.
 */
@EventBusSubscriber(modid = CubitMod.MOD_ID, value = Dist.CLIENT)
public final class TabletChestMarks {
  private static final float ALPHA = 0.9f;
  private static final float SCALE = 1.2f;
  private static final double HALF = 0.5;
  private static final double FIRST_LINE = 1.55;
  private static final double LINE_GAP = 0.27;
  private static final float TEXT_SCALE = 0.025f;
  private static final int TEXT_COLOR = 0xFFFFFFFF;
  private static final int TEXT_BACKGROUND = 0x66000000;

  private static List<UnloadChestsPayload.Chest> chests = List.of();
  private static List<OrganizerMarksPayload.Mark> marks = List.of();

  private TabletChestMarks() {}

  static void show(UnloadChestsPayload payload) {
    chests = payload.chests();
  }

  static void showMarks(OrganizerMarksPayload payload) {
    marks = payload.marks();
  }

  @SubscribeEvent
  public static void render(RenderLevelStageEvent event) {
    LocalPlayer player = Minecraft.getInstance().player;
    if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL
        || player == null
        || (chests.isEmpty() && marks.isEmpty())
        || !holdsTablet(player)) {
      return;
    }
    float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);
    double ticks = player.level().getGameTime() + partialTick;
    Matrix4fStack view = RenderSystem.getModelViewStack();
    view.pushMatrix();
    view.mul(event.getModelViewMatrix());
    RenderSystem.applyModelViewMatrix();
    MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
    List<Vec3> tops = chests.stream().map(TabletChestMarks::topOf).toList();
    MobMarker.Style style = MobMarker.Style.of(TabletAppLook.color(TabletApp.UNLOAD), SCALE);
    MobMarker.drawOver(buffers, event.getCamera(), tops, style, ticks, ALPHA);
    for (UnloadChestsPayload.Chest chest : chests) {
      label(buffers, event.getCamera(), chest);
    }
    drawMarks(buffers, event.getCamera(), ticks);
    buffers.endBatch();
    view.popMatrix();
    RenderSystem.applyModelViewMatrix();
  }

  /** The marks of the Organizers, in the color of the Mark app, with the Cubit and the number. */
  private static void drawMarks(
      MultiBufferSource.BufferSource buffers, Camera camera, double ticks) {
    List<Vec3> tops = marks.stream().map(mark -> topOf(mark.pos())).toList();
    MobMarker.Style style = MobMarker.Style.of(TabletAppLook.color(TabletApp.MARK), SCALE);
    MobMarker.drawOver(buffers, camera, tops, style, ticks, ALPHA);
    for (OrganizerMarksPayload.Mark mark : marks) {
      Vec3 top = topOf(mark.pos());
      line(buffers, camera, top.add(0, FIRST_LINE, 0), cubitName(mark.cubit()));
      line(
          buffers,
          camera,
          top.add(0, FIRST_LINE - LINE_GAP, 0),
          Component.translatable(TabletKeys.MARK_NUMBER, mark.number()));
    }
  }

  private static Vec3 topOf(BlockPos pos) {
    return new Vec3(pos.getX() + HALF, pos.getY() + 1, pos.getZ() + HALF);
  }

  private static Component cubitName(String cubit) {
    return cubit.isEmpty()
        ? Component.translatable(CubitEntities.GENERAL_ROBOT.get().getDescriptionId())
        : Component.literal(cubit);
  }

  private static Vec3 topOf(UnloadChestsPayload.Chest chest) {
    return new Vec3(chest.pos().getX() + HALF, chest.pos().getY() + 1, chest.pos().getZ() + HALF);
  }

  /** The name of the Cubit and the slots of the chest, facing the camera, over the arrow. */
  private static void label(
      MultiBufferSource.BufferSource buffers, Camera camera, UnloadChestsPayload.Chest chest) {
    Component name =
        chest.cubit().isEmpty()
            ? Component.translatable(CubitEntities.GENERAL_ROBOT.get().getDescriptionId())
            : Component.literal(chest.cubit());
    Component slots = Component.translatable(TabletKeys.CHEST_SLOTS, chest.used(), chest.total());
    Vec3 top = topOf(chest);
    line(buffers, camera, top.add(0, FIRST_LINE, 0), name);
    line(buffers, camera, top.add(0, FIRST_LINE - LINE_GAP, 0), slots);
  }

  private static void line(
      MultiBufferSource.BufferSource buffers, Camera camera, Vec3 at, Component text) {
    Vec3 relative = at.subtract(camera.getPosition());
    PoseStack pose = new PoseStack();
    pose.translate(relative.x, relative.y, relative.z);
    pose.mulPose(camera.rotation());
    pose.scale(TEXT_SCALE, -TEXT_SCALE, TEXT_SCALE);
    Font font = Minecraft.getInstance().font;
    Matrix4f matrix = pose.last().pose();
    float x = -font.width(text) / 2f;
    font.drawInBatch(
        text,
        x,
        0,
        TEXT_COLOR,
        false,
        matrix,
        buffers,
        Font.DisplayMode.SEE_THROUGH,
        TEXT_BACKGROUND,
        LightTexture.FULL_BRIGHT);
  }

  private static boolean holdsTablet(LocalPlayer player) {
    return player.getMainHandItem().is(CubitItems.TABLET.get())
        || player.getOffhandItem().is(CubitItems.TABLET.get());
  }
}
