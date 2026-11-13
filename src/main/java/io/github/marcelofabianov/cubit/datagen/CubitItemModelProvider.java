package io.github.marcelofabianov.cubit.datagen;

import io.github.marcelofabianov.cubit.CubitMod;
import io.github.marcelofabianov.cubit.domain.robot.RobotColor;
import io.github.marcelofabianov.cubit.item.RobotItem;
import io.github.marcelofabianov.cubit.registry.CubitItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

final class CubitItemModelProvider extends ItemModelProvider {
  private static final String COLOR_SUFFIX = "_";
  private static final String GENERATED_PARENT = "item/generated";
  private static final String LAYER = "layer0";
  private static final String COLOR_TEXTURE = "item/%s_color/%s";
  private static final String COLLAR_LAYER = "layer1";
  private static final String COLLAR_TEXTURE = "item/%s_collar";

  CubitItemModelProvider(PackOutput output, ExistingFileHelper existingFiles) {
    super(output, CubitMod.MOD_ID, existingFiles);
  }

  @Override
  protected void registerModels() {
    robotWithColors();
    basicItem(CubitItems.DAMAGED_GENERAL_ROBOT.get()).texture(COLLAR_LAYER, collarTexture());
    basicItem(CubitItems.TOOLBOX.get());
    basicItem(CubitItems.TABLET.get());
    basicItem(CubitItems.ADVANCED_MODULE.get());
    basicItem(CubitItems.ELITE_MODULE.get());
    basicItem(CubitItems.ULTIMATE_MODULE.get());
    basicItem(CubitItems.BASE_POWER_BANK.get());
    basicItem(CubitItems.ADVANCED_POWER_BANK.get());
    basicItem(CubitItems.ELITE_POWER_BANK.get());
    basicItem(CubitItems.ULTIMATE_POWER_BANK.get());
  }

  /** The collar is white, and the client tints it with the color of the tier (answer 803). */
  private ResourceLocation collarTexture() {
    String name = BuiltInRegistries.ITEM.getKey(CubitItems.GENERAL_ROBOT.get()).getPath();
    return modLoc(COLLAR_TEXTURE.formatted(name));
  }

  /** The game uses the last override that matches, so they go in the order of the numbers. */
  private void robotWithColors() {
    ItemModelBuilder robot =
        basicItem(CubitItems.GENERAL_ROBOT.get()).texture(COLLAR_LAYER, collarTexture());
    String name = BuiltInRegistries.ITEM.getKey(CubitItems.GENERAL_ROBOT.get()).getPath();
    for (RobotColor color : RobotColor.values()) {
      if (color != RobotColor.WHITE) {
        ItemModelBuilder colored =
            withExistingParent(name + COLOR_SUFFIX + color.key(), mcLoc(GENERATED_PARENT))
                .texture(LAYER, modLoc(COLOR_TEXTURE.formatted(name, color.key())))
                .texture(COLLAR_LAYER, collarTexture());
        robot.override().predicate(RobotItem.COLOR_PROPERTY, color.ordinal()).model(colored).end();
      }
    }
  }
}
