package io.github.marcelofabianov.cubit.client;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.RobotType;
import io.github.marcelofabianov.cubit.entity.RobotEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

/** Draws a robot with the model files named after its type. */
final class RobotRenderer extends GeoEntityRenderer<RobotEntity> {
  RobotRenderer(EntityRendererProvider.Context context, RobotType robotType) {
    super(
        context,
        new RobotGeoModel(ResourceLocation.fromNamespaceAndPath(CubitMod.MOD_ID, robotType.id())));
    addRenderLayer(new RobotTrackLayer(this, robotType));
    addRenderLayer(new RobotScreenLayer(this, robotType));
    addRenderLayer(new RobotCollarLayer(this, robotType));
    addRenderLayer(new RobotBatteryLayer(this, robotType));
    addRenderLayer(new RobotAntennaGlowLayer(this, robotType));
    addRenderLayer(new RobotScanHaloLayer(this, robotType));
    addRenderLayer(new RobotSolarPanelLayer(this, robotType));
  }
}
